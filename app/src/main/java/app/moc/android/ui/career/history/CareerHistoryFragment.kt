package app.moc.android.ui.career.history

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.CareerHistoryFragmentBinding
import app.moc.android.ui.career.CareerHistoryActionHandler
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.detail.CareerDetailFragment.Companion.CAREER_DETAIL_ACTION_DATA
import app.moc.android.ui.career.detail.CareerDetailFragment.Companion.CAREER_DETAIL_ACTION_RESULT_KEY
import app.moc.android.ui.career.detail.CareerDetailFragment.Companion.CAREER_DETAIL_ACTION_TYPE
import app.moc.android.ui.career.history.calendar.CalendarAdapter
import app.moc.android.ui.career.history.calendar.CalendarHistoryItemUIModel
import app.moc.android.ui.career.history.calendar.CalendarHistoryListUIModel
import app.moc.android.util.fmt
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.android.util.tryOrDefault
import app.moc.model.DateTime
import app.moc.model.PlanCheckQueryInfo
import app.moc.shared.result.Result
import app.moc.shared.util.millisDiffToDays
import app.moc.shared.util.toLocalDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class CareerHistoryFragment : MainNavigationFragment(R.layout.career_history_fragment), CareerHistoryActionHandler {

    private lateinit var binding: CareerHistoryFragmentBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private val careerHistoryViewModel: CareerHistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener(CAREER_DETAIL_ACTION_RESULT_KEY) { _, bundle ->
            val type = bundle.get(CAREER_DETAIL_ACTION_TYPE) as String
            if(type == "modify"){
                val uiModel = bundle.get(CAREER_DETAIL_ACTION_DATA) as CareerItemUIModel?
                if(uiModel != null){
                    careerHistoryViewModel.updateCareerItemUIModel(uiModel)
                }
            }
        }
        val itemUIModel = careerHistoryViewModel.careerItemUIModel.value
        binding = CareerHistoryFragmentBinding.bind(view).apply {
            containerMemo.divider.visibility = View.GONE

            val startDate = DateTime(itemUIModel.startDate).toLocalDate()
            val endDate = DateTime(itemUIModel.endDate).toLocalDate()
            val monthDiff = endDate.monthValue - startDate.monthValue + 1
            val startOffset = LocalDate.now().monthValue - startDate.monthValue

            calendarAdapter = CalendarAdapter(this@CareerHistoryFragment, startDate, monthDiff)
            viewPager.adapter = calendarAdapter
            viewPager.setCurrentItem(startOffset, false)
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                careerHistoryViewModel.careerItemUIModel.collectLatest { itemUIModel ->
                    binding.apply {
                        with(header.toolbar){
                            setTitleTextAppearance(requireActivity(), R.style.TextAppearance_Moc_H5_2)
                            title = itemUIModel.title
                            background = null
                            setupCareerHistoryMenuItem(itemUIModel, this@CareerHistoryFragment)
                        }
                        uiModel = CalendarHistoryListUIModel(
                            type = CalendarHistoryItemUIModel("유형", itemUIModel.getTypeDisplayText()),
                            start = CalendarHistoryItemUIModel("시작", "yyyy년 MM월 dd일".fmt(DateTime(itemUIModel.startDate))),
                            end = CalendarHistoryItemUIModel("종료", "yyyy년 MM월 dd일".fmt(DateTime(itemUIModel.endDate))),
                            memo = CalendarHistoryItemUIModel("메모", itemUIModel.memo)
                        )
                    }
                    val now = LocalDate.now()
                    careerHistoryViewModel.getCareerChecks(
                        PlanCheckQueryInfo(
                            itemUIModel.id, now.year, now.monthValue
                        )
                    )
                }
            }

            launch {
                careerHistoryViewModel.careerChecksUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        val data = result.data
                        val satisfact = tryOrDefault(0) { data.maxOf { it.satisfact } }
                        val diffDays = millisDiffToDays(
                            itemUIModel.startDate,
                            itemUIModel.endDate
                        ).toFloat()
                        val progress = (data.count() / diffDays) * 100
                        binding.uiModel = binding.uiModel?.copy(
                            totalProgressDisplayText = "${round(progress).toInt()}%",
                            satisfactDisplayText = when(satisfact) {
                                1 -> "만족"
                                2 -> "보통"
                                3 -> "불만족"
                                else -> "없음"
                            }
                        )
                    }
                }
            }

            launch {
                careerHistoryViewModel.modifyCareerUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                }
            }

            launch {
                careerHistoryViewModel.deleteCareerUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        findNavController().navigateUp()
                        setFragmentResult(
                            CAREER_HISTORY_ACTION_RESULT_KEY,
                            bundleOf(CAREER_HISTORY_ACTION_TYPE to "delete")
                        )
                    }
                }
            }

            launch {
                careerHistoryViewModel.setCareerDoneUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        findNavController().navigateUp()
                        setFragmentResult(
                            CAREER_HISTORY_ACTION_RESULT_KEY,
                            bundleOf(CAREER_HISTORY_ACTION_TYPE to "setDone")
                        )
                    }
                }
            }
        }
    }

    override fun navigateToModifyCareerDetail() {
        val uiModel = careerHistoryViewModel.careerItemUIModel.value
        findNavController().navigate(CareerHistoryFragmentDirections.toCareerDetail(uiModel))
    }

    override fun delete() {
        val uiModel = careerHistoryViewModel.careerItemUIModel.value
        careerHistoryViewModel.deleteCareer(uiModel.id)
    }

    override fun setDone() {
        val uiModel = careerHistoryViewModel.careerItemUIModel.value
        careerHistoryViewModel.setCareerDone(uiModel.id)
    }

    companion object {
        const val CAREER_HISTORY_ACTION_RESULT_KEY = "career_history_action_result_key"
        const val CAREER_HISTORY_ACTION_TYPE = "career_history_action_type"
    }
}