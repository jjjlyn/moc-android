package app.moc.android.ui.home

import android.os.Parcelable
import androidx.annotation.Keep
import app.moc.android.util.fmt
import app.moc.model.Community
import app.moc.model.DateTime
import kotlinx.parcelize.Parcelize

@Keep
data class HomeIntroUIModel(
    val nickName: String = "",
    val leftDays: Long = 0
) {
    fun getDdays() = "D-$leftDays"
    fun getWelcome() = "${nickName}님,\n오늘도 퇴사를\n고민하셨나요?"
}

@Keep
@Parcelize
data class MocTalkItemUIModel(
    val id: Int,
    val categoryID: Int,
    val categoryName: String,
    val title: String,
    val content: String,
    val likes: String,
    val hits: String,
    val createDate: DateTime,
    val thumb: String?
) : Parcelable {
    fun getCreateDateFmt() = "yyyy-MM-dd".fmt(createDate)
}

fun Community.toUIModel() =
    MocTalkItemUIModel(boardID, category, "asdf", title, content, likes.toString(), hits.toString(), DateTime(createDate * 1000), imageTag)