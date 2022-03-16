package app.moc.android.ui.career.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.moc.android.MainNavigationFragment
import app.moc.android.R
import app.moc.android.databinding.CareerHistoryFragmentBinding
import app.moc.android.ui.career.CareerHistoryActionHandler
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.history.calendar.CalendarAdapter
import app.moc.android.ui.career.history.calendar.CalendarHistoryItemUIModel
import app.moc.android.ui.career.history.calendar.CalendarHistoryListUIModel
import app.moc.android.ui.career.manage.CareerManageViewModel
import app.moc.android.util.fmt
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.setVisible
import app.moc.model.DateTime
import app.moc.model.PlanCheckQueryInfo
import app.moc.shared.result.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class CareerHistoryFragment : MainNavigationFragment(R.layout.career_history_fragment), CareerHistoryActionHandler {

    private lateinit var binding: CareerHistoryFragmentBinding
    private lateinit var calendarAdapter: CalendarAdapter
    private val careerHistoryViewModel: CareerHistoryViewModel by viewModels()
    private val careerManageViewModel: CareerManageViewModel by viewModels()
    private val args: CareerHistoryFragmentArgs by navArgs()
    private lateinit var careerItemUIModel: CareerItemUIModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        careerItemUIModel = args.uiModel
        binding = CareerHistoryFragmentBinding.bind(view).apply {
            with(header.toolbar){
                title = careerItemUIModel.title
                setupCareerHistoryMenuItem(this@CareerHistoryFragment)
            }
            uiModel = CalendarHistoryListUIModel(
                type = CalendarHistoryItemUIModel("유형", careerItemUIModel.getTypeDisplayText()),
                start = CalendarHistoryItemUIModel("시작", "yyyy년 MM월 dd일".fmt(DateTime(careerItemUIModel.startDate))),
                end = CalendarHistoryItemUIModel("종료", "yyyy년 MM월 dd일".fmt(DateTime(careerItemUIModel.endDate))),
                memo = CalendarHistoryItemUIModel("메모", careerItemUIModel.memo)
            )
            containerMemo.divider.visibility = View.GONE
            calendarAdapter = CalendarAdapter(this@CareerHistoryFragment)
            viewPager.adapter = calendarAdapter
            viewPager.setCurrentItem(CalendarAdapter.START_POSITION, false)
        }
        val now = LocalDate.now()
        careerHistoryViewModel.getCareerChecks(
            PlanCheckQueryInfo(
                careerItemUIModel.id, now.year, now.monthValue
            )
        )

        launchAndRepeatWithViewLifecycle {
            launch {
                careerHistoryViewModel.careerChecksUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)

                }
            }

            launch {
                careerHistoryViewModel.modifyCareerUseCaseResult.collectLatest { result ->

                }
            }

            launch {
                careerHistoryViewModel.deleteCareerUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        careerManageViewModel.getCareers()
                    }
                }
            }

            launch {
                careerHistoryViewModel.setCareerDoneUseCaseResult.collectLatest { result ->
                    binding.containerLoading.root.setVisible(result.isLoading)
                    if(result is Result.Success){
                        careerManageViewModel.getCareers()
                    }
                }
            }

            launch {
                careerManageViewModel.totalResult.collectLatest { result ->
                    if(result is Result.Success){
                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    override fun modify() {
        careerHistoryViewModel.modifyCareer(careerItemUIModel)
    }

    override fun delete() {
        careerHistoryViewModel.deleteCareer(careerItemUIModel.id)
    }

    override fun setDone() {
        careerHistoryViewModel.setCareerDone(careerItemUIModel.id)
    }
}