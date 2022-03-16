package app.moc.android.ui.career.manage

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemMarginDecoration
import app.moc.android.R
import app.moc.android.databinding.CareerManageFragmentBinding
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.CareerManageActionHandler
import app.moc.android.ui.career.CareerManageItemActionHandler
import app.moc.android.ui.career.check.CareerCheckDialogFragment
import app.moc.android.util.isDialogShowing
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.model.Plan
import app.moc.shared.result.Result
import app.moc.shared.result.data
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CareerManageFragment: Fragment(R.layout.career_manage_fragment), CareerManageActionHandler {

    private lateinit var binding: CareerManageFragmentBinding
    private val careerManageViewModel: CareerManageViewModel by viewModels()
    private lateinit var careerManageAdapter: CareerManageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        careerManageAdapter = CareerManageAdapter().apply {
            actionHandler = object : CareerManageItemActionHandler {
                override fun showCareerCheck(uiModel: CareerItemUIModel) {
                    if(childFragmentManager.isDialogShowing()) return
                    CareerCheckDialogFragment().apply {
                        arguments = bundleOf("careerItemUIModel" to uiModel)
                    }.show(childFragmentManager, "career_check_dialog")
                }
                override fun navigateToCareerHistory(uiModel: CareerItemUIModel) {
                    findNavController().navigate(
                        CareerManageFragmentDirections.toCareerHistory(uiModel)
                    )
                }
            }
        }
        binding = CareerManageFragmentBinding.bind(view).apply {
            viewModel = careerManageViewModel
            lifecycleOwner = viewLifecycleOwner
            actionHandler = this@CareerManageFragment
            listCareerManage.apply {
                addItemDecoration(ItemMarginDecoration(vertical = resources.getDimensionPixelOffset(R.dimen.margin_small)))
                adapter = careerManageAdapter
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                careerManageViewModel.totalResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        careerManageAdapter.submitList(result.data)
                    }
                }
            }
        }
    }

    override fun navigateToCareerDetail() {
        findNavController().navigate(CareerManageFragmentDirections.toCareerDetail())
    }
}