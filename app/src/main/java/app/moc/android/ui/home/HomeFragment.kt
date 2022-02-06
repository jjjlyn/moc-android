package app.moc.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.ItemMarginDecoration
import app.moc.android.R
import app.moc.android.databinding.HomeFragmentBinding
import app.moc.android.util.getDrawableCompat
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {
    private lateinit var binding: HomeFragmentBinding
    private lateinit var todayCheckAdapter: TodayCheckAdapter
    private lateinit var mocTalkAdapter: MocTalkAdapter

    private val homeViewModel : HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayCheckAdapter = TodayCheckAdapter()
        mocTalkAdapter = MocTalkAdapter()
        binding = HomeFragmentBinding.bind(view).apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner

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
            }
            containerMocTalk.list.apply {
                adapter = mocTalkAdapter
                addItemDecoration(ItemMarginDecoration(vertical = resources.getDimensionPixelOffset(R.dimen.stroke_small)))
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
}