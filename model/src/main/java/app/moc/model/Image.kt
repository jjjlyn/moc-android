package app.moc.model

import androidx.annotation.Keep

@Keep
data class Image(
    val id: Int,
    val fileName: String,
    val imageUrl: String,
    val tag: String
)