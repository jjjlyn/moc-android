package app.moc.android.ui.career.history.calendar

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.moc.model.DateTime
import app.moc.shared.util.toMillis
import app.moc.shared.util.toLocalDate
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoField

class CalendarAdapter(fm: Fragment): FragmentStateAdapter(fm){

    private val start = LocalDate.now()
        .withDayOfMonth(1)
        .atStartOfDay()
        .toMillis()

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        val millis = getItemId(position)
        return CalendarFragment.newInstance(millis)
    }

    override fun getItemId(position: Int): Long {
        return DateTime(start)
            .toLocalDate()
            .plusMonths((position - START_POSITION).toLong())
            .atStartOfDay()
            .toMillis()
    }

//    override fun containsItem(itemId: Long): Boolean {
//        val date = DateTime(itemId).toLocalDate()
//        return date.dayOfMonth == 1 && date.getLong(ChronoField.MILLI_OF_DAY) == 0L
//    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}