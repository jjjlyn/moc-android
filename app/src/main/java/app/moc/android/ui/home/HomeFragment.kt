package app.moc.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.ItemMarginDecoration
import app.moc.android.R
import app.moc.android.databinding.HomeFragmentBinding
import app.moc.android.ui.career.CareerNavigationHandler
import app.moc.android.ui.career.detail.CareerDetailFragment
import app.moc.android.ui.career.detail.CareerDetailFragment.Companion.CAREER_DETAIL_ACTION_RESULT_KEY
import app.moc.android.ui.career.detail.CareerDetailFragment.Companion.CAREER_DETAIL_ACTION_TYPE
import app.moc.android.ui.career.history.CareerHistoryFragment.Companion.CAREER_HISTORY_ACTION_RESULT_KEY
import app.moc.android.ui.career.history.CareerHistoryFragment.Companion.CAREER_HISTORY_ACTION_TYPE
import app.moc.android.ui.career.toUIModel
import app.moc.android.ui.common.ComponentTitleUIModel
import app.moc.android.ui.talk.TalkActionHandler
import app.moc.android.util.dp
import app.moc.android.util.getDrawableCompat
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment), TalkActionHandler {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var todayCheckAdapter: TodayCheckAdapter
    private lateinit var mocTalkAdapter: MocTalkAdapter

    private val homeViewModel : HomeViewModel by viewModels()

    @Inject
    lateinit var navigationHandler: CareerNavigationHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(CAREER_HISTORY_ACTION_RESULT_KEY) { _, _ ->
            homeViewModel.getTodayChecks()
        }
        setFragmentResultListener(CAREER_DETAIL_ACTION_RESULT_KEY) { _, _ ->
            homeViewModel.getTodayChecks()
        }

        todayCheckAdapter = TodayCheckAdapter().apply {
            onItemClick = {
                navigationHandler.navigateToCareerHistory(it){ uiModel ->
                    findNavController().navigate(HomeFragmentDirections.toCareerHistory(uiModel))
                }
            }
        }
        mocTalkAdapter = MocTalkAdapter().apply {
            actionHandler = this@HomeFragment
        }
        binding = HomeFragmentBinding.bind(view).apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
            containerTodayCheck.setOnClick {
                navigationHandler.navigateToRegisterCareerDetail {
                    findNavController().navigate(HomeFragmentDirections.toCareerDetail(null))
                }
            }
            containerMocTalk.setOnClick {
                findNavController().navigate(HomeFragmentDirections.toTalkMain())
            }

            todayCheckTitleUIModel = ComponentTitleUIModel(
                image = requireActivity().getDrawableCompat(R.drawable.ic_today_check),
                text = "투데이 체크",
                button = "등록하기"
            )
            mocTalkTitleUIModel = ComponentTitleUIModel(
                image = requireActivity().getDrawableCompat(R.drawable.ic_moc_talk),
                text = "모크러 톡톡",
                button = "전체보기"
            )
            containerTodayCheck.list.apply {
                adapter = todayCheckAdapter
                addItemDecoration(ItemMarginDecoration(horizontal = 3.dp().toInt()))
            }
            containerMocTalk.list.apply {
                adapter = mocTalkAdapter
                addItemDecoration(ItemMarginDecoration(vertical = resources.getDimensionPixelOffset(R.dimen.stroke_small)))
            }
            mocTalkAdapter.addLoadStateListener { loadState ->
                binding.containerLoading.root.setVisible(loadState.refresh is LoadState.Loading)
//                binding.containerError.progressBar.setVisible(loadState.refresh is LoadState.Loading)
//                binding.containerError.buttonRefresh.setInvisible(loadState.refresh is LoadState.Loading)
//                manageErrors(loadState)
//                manageEmpty(loadState)
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                homeViewModel.latestCommunities.collectLatest {
                    mocTalkAdapter.submitData(it.map { community -> community.toUIModel() })
                }
            }

            launch {
                homeViewModel.todayCheckUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    when(result){
                        is Result.Success -> {
                            binding.containerTodayCheck.textNoData.setVisible(result.data.isEmpty())
                            todayCheckAdapter.submitList(result.data.map { it.toUIModel() })
                        }
                        is Result.Error -> {

                        }
                    }
                }
            }
        }
    }

    override fun navigateToDetail(uiModel: MocTalkItemUIModel) {
        findNavController().navigate(HomeFragmentDirections.toTalkDetail(uiModel))
    }
}