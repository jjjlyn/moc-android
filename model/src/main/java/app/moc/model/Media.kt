package app.moc.model

import android.net.Uri

data class Media(
    val albumName: String,
    val uri: Uri,
    val dateAddedSecond: Long
)