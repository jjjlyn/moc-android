package app.moc.android.ui.career.history.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.moc.android.databinding.CalendarFragmentBinding
import app.moc.android.ui.career.history.CareerHistoryViewModel
import app.moc.android.ui.career.history.calendar.CalendarUtils.getMonthList
import app.moc.android.util.launchAndRepeatWithViewLifecycle
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
                        val localDate = DateTime(millis).toLocalDate()
                        val filteredValue = getMonthList(localDate).map { date ->
                            val hasSchedule = data.find { DateTime(it.date).toLocalDate() == date } != null
                            CalendarItemUIModel(date, hasSchedule)
                        }
                        binding.calendarView.initCalendar(localDate, filteredValue)
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