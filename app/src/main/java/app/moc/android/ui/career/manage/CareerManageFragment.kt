package app.moc.android.ui.career.manage

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import app.moc.android.R
import app.moc.android.databinding.CareerManageFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.CareerNavigationHandler
import app.moc.android.ui.career.CareerManageItemActionHandler
import app.moc.android.ui.career.check.CareerCheckDialogFragment
import app.moc.android.ui.career.detail.CareerDetailFragment
import app.moc.android.ui.career.history.CareerHistoryFragment
import app.moc.android.ui.common.ComponentTitleUIModel
import app.moc.android.util.isDialogShowing
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CareerManageFragment: Fragment(R.layout.career_manage_fragment){

    private lateinit var binding: CareerManageFragmentBinding
    private val careerManageViewModel: CareerManageViewModel by viewModels()
    private lateinit var careerManageAdapter: ConcatAdapter
    private lateinit var careerManagerInProgressHeaderAdapter: CareerManageHeaderAdapter
    private lateinit var careerManageInProgressItemAdapter: CareerManageItemAdapter
    private lateinit var careerManagerCompletedHeaderAdapter: CareerManageHeaderAdapter
    private lateinit var careerManageCompletedItemAdapter: CareerManageItemAdapter

    @Inject
    lateinit var navigationHandler: CareerNavigationHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(CareerHistoryFragment.CAREER_HISTORY_ACTION_RESULT_KEY) { _, _ ->
            careerManageViewModel.getCareers()
        }
        setFragmentResultListener(CareerDetailFragment.CAREER_DETAIL_ACTION_RESULT_KEY) { _, _ ->
            careerManageViewModel.getCareers()
        }

        val careerManageItemActionHandler = object : CareerManageItemActionHandler {
            override fun showCareerCheck(uiModel: CareerItemUIModel) {
                if (childFragmentManager.isDialogShowing()) return
                CareerCheckDialogFragment().apply {
                    arguments = bundleOf("careerItemUIModel" to uiModel)
                }.show(childFragmentManager, "career_check_dialog")
            }
        }

        careerManagerInProgressHeaderAdapter = CareerManageHeaderAdapter()
        careerManagerCompletedHeaderAdapter = CareerManageHeaderAdapter().apply {
            onArrowClick = { isExpanded ->
                careerManagerCompletedHeaderAdapter.submitList(
                    currentList.toMutableList().apply {
                        set(0, ComponentTitleUIModel(null, "완료", if (isExpanded) "접기" else "펼치기"))
                    }
                )
                if(isExpanded) {
                    careerManageAdapter.addAdapter(careerManageCompletedItemAdapter)
                } else {
                    careerManageAdapter.removeAdapter(careerManageCompletedItemAdapter)
                }
            }
        }
        careerManageInProgressItemAdapter = CareerManageItemAdapter().apply {
            onItemClick = {
                navigationHandler.navigateToCareerHistory(it){ uiModel ->
                    findNavController().navigate(CareerManageFragmentDirections.toCareerHistory(uiModel))
                }
            }
            actionHandler = careerManageItemActionHandler
        }
        careerManageCompletedItemAdapter = CareerManageItemAdapter().apply {
            onItemClick = {
                navigationHandler.navigateToCareerHistory(it){ uiModel ->
                    findNavController().navigate(CareerManageFragmentDirections.toCareerHistory(uiModel))
                }
            }
            actionHandler = careerManageItemActionHandler
        }

        careerManageAdapter = ConcatAdapter().apply {
            addAdapter(careerManagerInProgressHeaderAdapter)
            addAdapter(careerManageInProgressItemAdapter)
            addAdapter(careerManagerCompletedHeaderAdapter)
            addAdapter(careerManageCompletedItemAdapter)
        }

        binding = CareerManageFragmentBinding.bind(view).apply {
            viewModel = careerManageViewModel
            lifecycleOwner = viewLifecycleOwner
            listCareerManage.apply {
                adapter = careerManageAdapter
            }
            bgRegister.setOnClickListener {
                navigationHandler.navigateToRegisterCareerDetail {
                    findNavController().navigate(CareerManageFragmentDirections.toCareerDetail(null))
                }
            }
            imageRegister.setOnClickListener {
                navigationHandler.navigateToRegisterCareerDetail {
                    findNavController().navigate(CareerManageFragmentDirections.toCareerDetail(null))
                }
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                careerManageViewModel.totalResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        val data = result.data
                        careerManagerInProgressHeaderAdapter.submitList(listOf(ComponentTitleUIModel(null,"진행중", "접기")))
                        careerManageInProgressItemAdapter.submitList(data[0].list)

                        careerManagerCompletedHeaderAdapter.submitList(listOf(ComponentTitleUIModel(null,"완료", "접기")))
                        careerManageCompletedItemAdapter.submitList(data[1].list)
                    }
                }
            }
        }
    }
}