package app.moc.android.util.imagepicker.type

import android.net.Uri
import android.os.Environment
import android.provider.MediaStore

enum class MediaType(
    val savedDirectoryName: String,
    val fileSuffix: String,
    val mimeType: String,
    val externalContentUri: Uri
) {
    IMAGE(
        Environment.DIRECTORY_PICTURES,
        ".jpg",
        "image/*",
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    )
}