package app.moc.shared.data.api.request

import androidx.annotation.Keep
import app.moc.model.CommentModify
import app.moc.model.CommentUpload
import app.moc.model.CommunityItemModify
import app.moc.model.CommunityItemUpload
import com.squareup.moshi.Json
import java.io.File

@Keep
data class CommunityItemUploadRequest(
    val category: String,
    val title: String,
    val content: String,
    val images: File?
)

fun CommunityItemUpload.toRequest() = CommunityItemUploadRequest(category, title, content, images)

@Keep
data class CommunityItemModifyRequest(
    @Json(name = "board_id")
    val boardID: String,
    val category: String?,
    val title: String?,
    val content: String?,
    val imagesToAdd: File?,
    val imagesToDelete: String?
)

fun CommunityItemModify.toRequest() =
    CommunityItemModifyRequest(boardID, category, title, content, imagesToAdd, imagesToDelete)

@Keep
data class CommentUploadRequest(
    @Json(name = "board_id")
    val boardID: Long,
    val content: String,
    @Json(name = "parents_id")
    val parentsID: Int
)

fun CommentUpload.toRequest() = CommentUploadRequest(boardID, content, parentsID)

@Keep
data class CommentModifyRequest(
    @Json(name = "board_id")
    val boardID: Long,
    @Json(name = "comment_id")
    val commentID: Int,
    val content: String
)

fun CommentModify.toRequest() = CommentModifyRequest(boardID, commentID, content)