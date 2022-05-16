package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.Image
import com.squareup.moshi.Json

@Keep
data class ImageResponse(
    @Json(name = "image_id")
    val id: Int,
    val fileName: String,
    @Json(name = "image_url")
    val imageUrl: String,
    val tag: String
)

fun ImageResponse.toModel() = Image(id, fileName, imageUrl, tag)