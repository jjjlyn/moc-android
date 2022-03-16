package app.moc.android.util

import app.moc.model.DateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

fun String.fmt(dateTime: DateTime): String {
    return SimpleDateFormat(this, Locale.getDefault()).format(dateTime)
}

fun String.fmt(localDate: LocalDate): String {
    return localDate.format(DateTimeFormatter.ofPattern(this))
}