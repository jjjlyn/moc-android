package app.moc.android.ui.career.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import app.moc.model.DateTime
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CareerDetailFragment : Fragment(R.layout.career_detail_fragment), CareerDetailActionHandler {
    private lateinit var binding: CareerDetailFragmentBinding
    private lateinit var dayOfWeekAdapter: DayOfWeekAdapter
    private val careerDetailViewModel: CareerDetailViewModel by viewModels()
    private val args: CareerDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dayOfWeekAdapter = DayOfWeekAdapter(viewLifecycleOwner).apply {
            onDayOfWeekSelectionChanged = careerDetailViewModel::setDayOfWeeks
            currentSelectionsState = careerDetailViewModel.currentDayOfWeeksSelection
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
            val careerItemUIModel = args.uiModel
            textStartDate.isEnabled = careerItemUIModel == null
            textEndDate.isEnabled = careerItemUIModel == null
            header.textTitle.text = if(careerItemUIModel == null) "플랜 등록" else "플랜 수정"
            if(careerItemUIModel != null){
                careerDetailViewModel.apply {
                    onCareerNameChanged(careerItemUIModel.title)
                    setStartDate(DateTime(careerItemUIModel.startDate))
                    setEndDate(DateTime(careerItemUIModel.endDate))
                    setDayOfWeeks(careerItemUIModel.dayOfWeeks)
                    setCareerType(careerItemUIModel.type.toInt())
                    onMemoChanged(careerItemUIModel.memo ?: "")
                    onColorChanged(careerItemUIModel.color)
                }
            }
        }
        dayOfWeekAdapter.submitList(dayOfWeeks)

        launchAndRepeatWithViewLifecycle {
            launch {
                careerDetailViewModel.careerType.collectLatest { type ->
                    binding.chipGroup.check(
                        when(type){
                            1 -> R.id.chipCareerType1
                            2 -> R.id.chipCareerType2
                            3 -> R.id.chipCareerType3
                            else -> -1
                        }
                    )
                }
            }

            launch {
                careerDetailViewModel.registerResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if (result is Result.Success) {
                        findNavController().navigateUp()
                        setFragmentResult(
                            CAREER_DETAIL_ACTION_RESULT_KEY,
                            bundleOf(CAREER_DETAIL_ACTION_TYPE to "register")
                        )
                    }
                }
            }

            launch {
                careerDetailViewModel.modifyResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if (result is Result.Success) {
                        findNavController().navigateUp()
                        setFragmentResult(
                            CAREER_DETAIL_ACTION_RESULT_KEY,
                            bundleOf(CAREER_DETAIL_ACTION_TYPE to "modify")
                        )
                    }
                }
            }
        }
    }

    override fun showCancelDialog() {
        if(childFragmentManager.isDialogShowing()) return
        CommonTwoButtonDialogFragment().apply {
            onLeftClick = { dismiss() }
            onRightClick = { findNavController().navigateUp() }
            leftButton = "취소"
            rightButton = "나가기"
            message = "지금까지 입력하신 정보가 모두 삭제됩니다.\n그래도 나가시겠어요?"
        }.show(childFragmentManager, "career_detail_caution_dialog")
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

    override fun onClickConfirm() {
        val careerItemUIModel = args.uiModel
        if(careerItemUIModel == null){
            careerDetailViewModel.registerCareer()
        } else {
            careerDetailViewModel.modifyCareer(careerItemUIModel.id)
        }
    }

    companion object {
        val CAREER_DETAIL_ACTION_RESULT_KEY = "career_detail_action_result_key"
        val CAREER_DETAIL_ACTION_TYPE = "career_detail_action_type"
    }
}