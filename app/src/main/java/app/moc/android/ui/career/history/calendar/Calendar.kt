package app.moc.android.ui.career.history.calendar


data class CalendarHistoryListUIModel(
    val type: CalendarHistoryItemUIModel,
    val start: CalendarHistoryItemUIModel,
    val end: CalendarHistoryItemUIModel,
    val memo: CalendarHistoryItemUIModel
)

data class CalendarHistoryItemUIModel(
    val label: String,
    val content: String?
)
