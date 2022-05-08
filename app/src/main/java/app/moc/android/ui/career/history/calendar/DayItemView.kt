package app.moc.android.ui.career.history.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import app.moc.android.R
import app.moc.android.ui.career.history.calendar.CalendarUtils.isSameMonth
import app.moc.android.util.dp
import app.moc.android.util.getColorCompat
import org.threeten.bp.LocalDate

class DayItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes private val defStyleAttr: Int = R.attr.itemViewStyle,
    @StyleRes private val defStyleRes: Int = R.style.Widget_Moc_Calendar_ItemViewStyle,
    private val date: LocalDate = LocalDate.now(),
    private val firstDayOfMonth: LocalDate = LocalDate.now(),
    private val hasSchedule: Boolean = false,
    private val isToday: Boolean = false
) : View(ContextThemeWrapper(context, defStyleRes), attrs, defStyleAttr) {

    private val bounds = Rect()

    private lateinit var textPaint: Paint
    private lateinit var dayItemBackgroundPaint: Paint
    private lateinit var todayItemPaint: Paint

    init {
        context.withStyledAttributes(attrs, R.styleable.CalendarView, defStyleAttr, defStyleRes){
            val itemTextSize = getDimensionPixelSize(R.styleable.CalendarView_itemTextSize, R.dimen.text_size_h5).toFloat()

            textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = itemTextSize
                color = context.getColorCompat(R.color.color_on_surface_600)
                if(isSameMonth(date, firstDayOfMonth).not()){
                    color = context.getColorCompat(R.color.color_on_surface_0)
                }
            }
            dayItemBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = context.getColorCompat(R.color.color_primary_A100)
                style = Paint.Style.FILL
            }
            todayItemPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeWidth = resources.getDimension(R.dimen.stroke_normal)
                color = context.getColorCompat(R.color.color_primary)
                style = Paint.Style.STROKE
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canvas == null) return

        val date = date.dayOfMonth.toString()
        textPaint.getTextBounds(date, 0, date.length, bounds)
        canvas.drawText(
            date,
            (width / 2 - bounds.width() / 2).toFloat() - 2,
            (height / 2 + bounds.height() / 2).toFloat(),
            textPaint
        )
        if(hasSchedule){
            addScheduledDayItem(canvas)
        }
        if(isToday){
            addTodayItem(canvas)
        }
    }

    private fun addTodayItem(canvas: Canvas){
        canvas.drawItem(todayItemPaint)
    }

    private fun addScheduledDayItem(canvas: Canvas){
        canvas.drawItem(dayItemBackgroundPaint)
    }

    private fun Canvas.drawItem(paint: Paint){
        drawCircle(
            width / 2f,
            height / 2f,
            width / 2f - 5.dp(),
            paint
        )
    }
}