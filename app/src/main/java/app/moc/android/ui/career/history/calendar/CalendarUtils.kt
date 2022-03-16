package app.moc.android.ui.career.history.calendar

import org.threeten.bp.LocalDate

object CalendarUtils {
    const val WEEKS_PER_MONTH = 6
    const val DAYS_PER_WEEK = 7

    fun getMonthList(localDate: LocalDate): List<LocalDate> {
        val list = mutableListOf<LocalDate>()

        val date = localDate.withDayOfMonth(1)
        val prev = getPrevOffset(date)

        val startValue = date.minusDays(prev.toLong())

        val totalDay = DAYS_PER_WEEK * WEEKS_PER_MONTH

        for(i in 0 until totalDay){
            list.add(startValue.plusDays(i.toLong()))
        }

        return list
    }

    private fun getPrevOffset(date: LocalDate): Int {
        var prevMonthTailOffset = date.dayOfWeek.value

        if(prevMonthTailOffset >= 7) prevMonthTailOffset %= 7

        return prevMonthTailOffset
    }

    fun isSameMonth(first: LocalDate, second: LocalDate): Boolean {
        return first.year == second.year && first.month == second.month
    }
}