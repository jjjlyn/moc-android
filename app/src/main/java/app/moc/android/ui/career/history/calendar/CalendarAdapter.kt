package app.moc.android.ui.career.history.calendar

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.moc.model.DateTime
import app.moc.shared.util.toMillis
import app.moc.shared.util.toLocalDate
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoField

class CalendarAdapter(fm: Fragment, private val startDate: LocalDate, private val monthDiff: Int) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return monthDiff
    }

    override fun createFragment(position: Int): Fragment {
        val millis = getItemId(position)
        return CalendarFragment.newInstance(millis)
    }

    override fun getItemId(position: Int): Long {
        return startDate
            .withDayOfMonth(1)
            .plusMonths(position.toLong())
            .atStartOfDay()
            .toMillis()
    }

    override fun containsItem(itemId: Long): Boolean {
        val date = DateTime(itemId).toLocalDate()
        return date.dayOfMonth == 1
    }
}