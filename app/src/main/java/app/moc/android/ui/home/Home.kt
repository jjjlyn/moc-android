package app.moc.android.ui.home

import android.graphics.drawable.Drawable
import app.moc.android.util.toFmt
import app.moc.model.Community
import app.moc.model.DateTime
import app.moc.model.Plan

data class ComponentTitleUIModel(
    val image: Drawable?,
    val text: String,
    val button: String
)

data class HomeIntroUIModel(
    val nickName: String = "",
    val leftDays: Long = 0
) {
    fun getDdays() = "D-$leftDays"
    fun getWelcome() = "${nickName}님,\n오늘도 퇴사를\n고민하셨나요?"
}

data class TodayCheckItemUIModel(
    val id: Int,
    val categoryId: String,
    val title: String,
    val duration: String,
    val progress: Float
)

fun Plan.toUIModel() = TodayCheckItemUIModel(
    id, categoryId, title, "$startDate ~ $endDate", 2f
)

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
) {
    fun getCreateDateFmt() = "yyyy-MM-dd".toFmt(createDate)
}

fun Community.toUIModel() =
    MocTalkItemUIModel(boardID, category, "asdf", title, content, likes.toString(), hits.toString(), DateTime(createDate * 1000), imageTag)