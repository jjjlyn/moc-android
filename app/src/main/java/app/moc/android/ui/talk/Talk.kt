package app.moc.android.ui.talk

import android.graphics.drawable.Drawable
import app.moc.android.util.fmt
import app.moc.model.Comment
import app.moc.model.DateTime

data class TalkFilterUIModel(
    val category: Int,
    val filterType: String // latest, popular
)

data class TalkDetailFooterItemUIModel(
    val text: String,
    val image: Drawable?
)

data class TalkCommentUIModel(
    val commentID : Int,
    val parentsID: Int,
    val boardID: Long,
    val userID: Long,
    val nickName: String,
    val content: String,
    val likes: Int,
    val deleted: Char,
    val createDate: Long,
    val modifyDate: Long,
    val isMyComment: Boolean = false,
    val isLastItem: Boolean = false
) {
    fun hasParent() = parentsID != 0
    private fun getFormattedModifiedDate() = "${"yy.MM.dd hh:mm".fmt(DateTime(modifyDate))}"
    fun getModifyDateDisplayText() =
        if (createDate == modifyDate) {
            getFormattedModifiedDate()
        } else {
            "${getFormattedModifiedDate()} (수정됨)"
        }
    }

fun Comment.toUIModel() =
    TalkCommentUIModel(commentID, parentsID, boardID, userID, nickname, content, likes, deleted, createDate * 1000, modifyDate * 1000, isMyComment = isMyComment ?: false)

sealed class TagAction {
    object Add : TagAction()
    data class Modify(val position: Int, val oldTag: String) : TagAction()
}