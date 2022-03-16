package app.moc.android.ui.career.history.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import app.moc.android.databinding.CalendarFragmentBinding
import app.moc.android.ui.career.history.calendar.CalendarUtils.getMonthList
import app.moc.android.util.fmt
import app.moc.model.DateTime
import app.moc.shared.util.toLocalDate

class CalendarFragment: Fragment() {

    var millis = 0L
    private lateinit var binding: CalendarFragmentBinding
    private lateinit var calendarDayOfWeekAdapter: CalendarDayOfWeekAdapter

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
        val localDate = DateTime(millis).toLocalDate()
        binding = CalendarFragmentBinding.inflate(inflater, container, false).apply {
            calendarView.initCalendar(localDate, getMonthList(localDate))
            listDayOfWeek.adapter = calendarDayOfWeekAdapter
        }
        calendarDayOfWeekAdapter.submitList(listOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"))

        return binding.root
    }


    companion object {

        private const val MILLIS = "MILLIS"

        fun newInstance(millis: Long) = CalendarFragment().apply {
            arguments = bundleOf(MILLIS to millis)
        }
    }
}