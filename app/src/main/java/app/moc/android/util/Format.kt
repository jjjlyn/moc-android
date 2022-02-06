package app.moc.android.util

import app.moc.model.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun String.toFmt(dateTime: DateTime): String {
    return SimpleDateFormat(this, Locale.getDefault()).format(dateTime)
}