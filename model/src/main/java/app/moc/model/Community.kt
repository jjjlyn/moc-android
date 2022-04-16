package app.moc.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.File

@Keep
@Parcelize
data class Community(
    val boardID: Int,
    val userID: Int,
    val nickName: String,
    val category: Int,
    val business: Int,
    val title: String,
    val content: String,
    val likes: Int,
    val hits: Int,
    val tag: String?,
    val imageTag: String,
    val createDate: Long,
    val modifyDate: Long
): Parcelable

@Keep
data class CommunityItemUpload(
    val category: String,
    val title: String,
    val content: String,
    val images: File?
)

@Keep
data class CommunityItemModify(
    val boardID: String,
    val category: String?,
    val title: String?,
    val content: String?,
    val imagesToAdd: File?,
    val imagesToDelete: String?
)

@Keep
data class Comment(
    val commentID : Int,
    val parentsID: Int,
    val boardID: Long,
    val userID: Long,
    val nickname: String,
    val content: String,
    val likes: Int,
    val deleted: Char,
    val createDate: Long,
    val modifyDate: Long,
    val isMyComment: Boolean? = null
)

@Keep
data class CommentUpload(
    val boardID: Long,
    val content: String,
    val parentsID: Int
)

@Keep
data class CommentModify(
    val boardID: Long,
    val commentID: Int,
    val content: String
)