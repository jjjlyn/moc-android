package app.moc.shared.util

import app.moc.model.DateTime
import org.threeten.bp.*
import java.util.concurrent.TimeUnit

fun millisDiffToDays(date1: Long, date2: Long): Long {
    val diffInMillis = date2 - date1
    return TimeUnit.MILLISECONDS.toDays(diffInMillis) + 1
}

fun DateTime.toLocalDate()
        = Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDateTime.toMillis() = this.toInstant(ZoneOffset.UTC).toEpochMilli()