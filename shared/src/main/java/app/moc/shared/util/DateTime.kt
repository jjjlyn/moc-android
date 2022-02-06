package app.moc.shared.util

import java.util.concurrent.TimeUnit

fun millisDiffToDays(date1: Long, date2: Long): Long {
    val diffInMillis = date2 - date1
    return TimeUnit.MILLISECONDS.toDays(diffInMillis)
}