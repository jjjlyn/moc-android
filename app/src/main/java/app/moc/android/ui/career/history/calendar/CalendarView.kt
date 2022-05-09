package app.moc.android.ui.career.history.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import app.moc.android.R
import app.moc.android.ui.career.history.calendar.CalendarUtils.DAYS_PER_WEEK
import app.moc.android.ui.career.history.calendar.CalendarUtils.WEEKS_PER_MONTH
import org.threeten.bp.LocalDate
import kotlin.math.max

class CalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = R.attr.calendarViewStyle,
    @StyleRes defStyleRes: Int = R.style.Widget_Moc_Calendar_CalendarViewStyle
) : ViewGroup(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private var _height: Float = 0f
    var showHistory: ShowHistory? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes){
           _height = getDimension(R.styleable.CalendarView_itemHeight, _height)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val h = paddingTop + paddingBottom + max(suggestedMinimumHeight, (_height * WEEKS_PER_MONTH).toInt())
        setMeasuredDimension(getDefaultSize(suggestedMinimumWidth, widthMeasureSpec), h)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val itemWidth = (width / DAYS_PER_WEEK).toFloat()
        val itemHeight = (height / WEEKS_PER_MONTH).toFloat()

        var index = 0
        children.forEach { view ->
            val left = (index % DAYS_PER_WEEK) * itemWidth
            val top = (index / DAYS_PER_WEEK) * itemHeight

            view.layout(left.toInt(), top.toInt(), (left + itemWidth).toInt(), (top + itemHeight).toInt())

            index++
        }
    }

    fun initCalendar(firstDayOfMonth: LocalDate, list: List<CalendarItemUIModel>){
        list.forEach {
            addView(DayItemView(
                context = context,
                startDate = it.startDate,
                date = it.date,
                firstDayOfMonth = firstDayOfMonth,
                hasSchedule = it.hasSchedule,
                isToday = it.date == LocalDate.now()
            ).apply {
                showHistory = this@CalendarView.showHistory
            })
        }
    }
}