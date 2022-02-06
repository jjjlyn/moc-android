package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.Community
import com.squareup.moshi.Json

@Keep
data class CommunityResponse(
    @Json(name = "board_id")
    val boardID: Int,
    @Json(name = "user_id")
    val userID: Int,
    val nickName: String,
    val category: Int,
    val business: Int,
    val title: String,
    val content: String,
    val likes: Int,
    val hits: Int,
    val tag: String?,
    @Json(name = "image_tag")
    val imageTag: String,
    val createDate: Long,
    val modifyDate: Long
)

fun CommunityResponse.toModel() =
    Community(boardID, userID, nickName, category, business, title, content, likes, hits, tag, imageTag, createDate, modifyDate)