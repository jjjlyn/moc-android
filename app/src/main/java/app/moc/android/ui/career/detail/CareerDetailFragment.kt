package app.moc.android.ui.career.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemGapDecoration
import app.moc.android.R
import app.moc.android.databinding.CareerDetailFragmentBinding
import app.moc.android.ui.career.CareerDetailActionHandler
import app.moc.android.ui.career.dayOfWeeks
import app.moc.android.ui.common.CommonDatePickerDialogFragment
import app.moc.android.ui.common.CommonTwoButtonDialogFragment
import app.moc.android.util.isDialogShowing
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CareerDetailFragment : Fragment(R.layout.career_detail_fragment), CareerDetailActionHandler {

    private lateinit var binding: CareerDetailFragmentBinding
    private lateinit var dayOfWeekAdapter: DayOfWeekAdapter
    private val careerDetailViewModel: CareerDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dayOfWeekAdapter = DayOfWeekAdapter().apply {
            onDayOfWeekSelectionChanged = careerDetailViewModel::setDayOfWeeks
        }
        binding = CareerDetailFragmentBinding.bind(view).apply {
            actionHandler = this@CareerDetailFragment
            viewModel = careerDetailViewModel
            lifecycleOwner = viewLifecycleOwner
            listDayOfWeek.apply {
                adapter = dayOfWeekAdapter
                addItemDecoration(ItemGapDecoration(horizontal = resources.getDimensionPixelOffset(R.dimen.spacing_normal)))
            }
            chipGroup.setOnCheckedChangeListener { _, checkedId ->
                careerDetailViewModel.setCareerType(
                    when(checkedId){
                        R.id.chipCareerType1 -> 1
                        R.id.chipCareerType2 -> 2
                        R.id.chipCareerType3 -> 3
                        else -> -1
                    }
                )
            }
        }
        dayOfWeekAdapter.submitList(dayOfWeeks)

        launchAndRepeatWithViewLifecycle {
            launch {
                careerDetailViewModel.registerResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        dismiss()
                    }
                }
            }
        }
    }

    override fun showCancelDialog() {
        if(childFragmentManager.isDialogShowing()) return
        CommonTwoButtonDialogFragment().apply {
            onLeftClick = { dismiss() }
            onRightClick = { this@CareerDetailFragment.dismiss() }
            leftButton = "취소"
            rightButton = "나가기"
            message = "지금까지 입력하신 정보가 모두 삭제됩니다.\n그래도 나가시겠어요?"
        }.show(childFragmentManager, "career_detail_caution_dialog")
    }

    override fun dismiss() {
        findNavController().navigateUp()
    }

    override fun showColorDialog() {
        if(childFragmentManager.isDialogShowing()) return
        ColorDialogFragment().show(childFragmentManager, "color_dialog")
    }

    override fun showStartDatePicker() {
        if(childFragmentManager.isDialogShowing()) return
        CommonDatePickerDialogFragment()
            .apply {
                onDateChanged = { c, _, _, _, _ ->
                    careerDetailViewModel.setStartDate(c.time)
                }
            }
            .show(childFragmentManager, "start_date_picker_fragment")
    }

    override fun showEndDatePicker() {
        if(childFragmentManager.isDialogShowing()) return
        CommonDatePickerDialogFragment()
            .apply {
                onDateChanged = { c, _, _, _, _ ->
                    careerDetailViewModel.setEndDate(c.time)
                }
            }
            .show(childFragmentManager, "end_date_picker_fragment")
    }

    override fun register() {
        careerDetailViewModel.apply {
            registerCareer()
        }
    }
}