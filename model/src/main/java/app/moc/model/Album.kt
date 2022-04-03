package app.moc.model

import android.net.Uri
import androidx.annotation.Keep

@Keep
data class Album(
    val name: String,
    val thumbnailUri: Uri,
    val mediaUris: List<Media>
) {
    val mediaCount: Int = mediaUris.size
    val displayMediaCount: String = "($mediaCount)"
}