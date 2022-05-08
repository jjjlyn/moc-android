package app.moc.android.ui.career.history.calendar

import androidx.annotation.Keep
import org.threeten.bp.LocalDate

@Keep
data class CalendarHistoryListUIModel(
    val type: CalendarHistoryItemUIModel,
    val start: CalendarHistoryItemUIModel,
    val end: CalendarHistoryItemUIModel,
    val memo: CalendarHistoryItemUIModel
)

@Keep
data class CalendarHistoryItemUIModel(
    val label: String,
    val content: String?
)

@Keep
data class CalendarItemUIModel(
    val localDate: LocalDate,
    val hasSchedule: Boolean
)

