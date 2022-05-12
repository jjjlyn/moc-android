package app.moc.android.ui.career.history.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import app.moc.android.databinding.CalendarFragmentBinding
import app.moc.android.ui.career.detail.CareerDetailFragment
import app.moc.android.ui.career.history.CareerHistoryViewModel
import app.moc.android.ui.career.history.calendar.CalendarUtils.getMonthList
import app.moc.android.util.isDialogShowing
import app.moc.android.util.launchAndRepeatWithViewLifecycle
import app.moc.android.util.showToast
import app.moc.model.DateTime
import app.moc.shared.result.Result
import app.moc.shared.util.toLocalDate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CalendarFragment: Fragment() {

    var millis = 0L
    private lateinit var binding: CalendarFragmentBinding
    private lateinit var calendarDayOfWeekAdapter: CalendarDayOfWeekAdapter
    private val careerHistoryViewModel: CareerHistoryViewModel by viewModels({ requireParentFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            millis = it.getLong(MILLIS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        calendarDayOfWeekAdapter = CalendarDayOfWeekAdapter()
        binding = CalendarFragmentBinding.inflate(inflater, container, false).apply {
            listDayOfWeek.adapter = calendarDayOfWeekAdapter
            calendarView.showHistory = { date, hasSchedule ->
                if(hasSchedule){
                    val careerItemUIModel = careerHistoryViewModel.careerItemUIModel.value
                    val startDate = DateTime(careerItemUIModel.startDate).toLocalDate()
                    if(childFragmentManager.isDialogShowing().not()){
                        CalendarItemDialogFragment(
                            careerItemUIModel = careerItemUIModel,
                            calendarItemUIModel = CalendarItemUIModel(startDate, date, hasSchedule)
                        ).show(
                            childFragmentManager, CalendarItemDialogFragment::class.java.simpleName
                        )
                    }
                } else {
                    showToast("인증내역이 없어요.")
                }
            }
        }
        calendarDayOfWeekAdapter.submitList(listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithViewLifecycle {
            launch {
                careerHistoryViewModel.careerChecksUseCaseResult.collectLatest { result ->
                    if(result is Result.Success){
                        val data = result.data.map { it.copy(date = it.date * 1000) }
                        val date = DateTime(millis).toLocalDate()
                        val startDate = DateTime(careerHistoryViewModel.careerItemUIModel.value.startDate).toLocalDate()
                        val filteredValue = getMonthList(date).map { item ->
                            val hasSchedule = data.find { DateTime(it.date).toLocalDate() == item } != null
                            CalendarItemUIModel(
                                startDate = startDate,
                                date = item,
                                hasSchedule = hasSchedule
                            )
                        }
                        binding.calendarView.initCalendar(date, filteredValue)
                    }
                }
            }
        }
    }


    companion object {
        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long) = CalendarFragment().apply {
            arguments = bundleOf(MILLIS to millis)
        }
    }
}