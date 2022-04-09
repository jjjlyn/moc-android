package app.moc.android.ui.career

import android.os.Parcelable
import androidx.annotation.Keep
import app.moc.android.util.fmt
import app.moc.model.DateTime
import app.moc.model.Plan
import kotlinx.parcelize.Parcelize

@Keep
data class CareerManageListItemUIModel(
    val title: String,
    val list: List<CareerItemUIModel>
)

@Keep
@Parcelize
data class CareerItemUIModel(
    val id: Int,
    val type: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val color: String = "#2562FF",
    val progress: Float,
    val dayOfWeeks: String,
    val memo: String?,
    val status: String?,
    val adapterPosition: Int = -1
): Parcelable {
    fun getDuration() = "${"yy년 MM월 dd일".fmt(DateTime(startDate))} ~ ${"yy년 MM월 dd일".fmt(DateTime(endDate))}"
    fun getTypeDisplayText() = when(type){
        "1" -> "자기계발"
        "2" -> "이직준비"
        "3" -> "사업준비"
        else -> "기타"
    }
    fun getIsCompleted() = "COMP" == status
}

@Keep
data class DayOfWeek(
    val dayOfWeek: String, // TODO enum으로 교체
    val dayOfWeekEng: String
)

val dayOfWeeks = listOf(
    DayOfWeek("일", "SUN"),
    DayOfWeek("월", "MON"),
    DayOfWeek("화", "TUE"),
    DayOfWeek("수", "WED"),
    DayOfWeek("목", "THU"),
    DayOfWeek("금", "FRI"),
    DayOfWeek("토", "SAT")
)

val colors = listOf(
    "#2562FF",
    "#6557FF",
    "#A536FD",
    "#FF8686",
    "#FF5454",
    "#FFA14D",
    "#FFD80A",
    "#CEC2FF",
    "#68EC9F",
    "#FF5AE5",
    "#00D1B8",
    "#ADEC37",
    "#FF7313",
    "#FF1010",
    "#FFB8E3",
    "#0017E2",
    "#686868",
    "#009F83",
    "#73DDFF",
    "#C76CFF",
    "#556EAD",
    "#212121",
    "#A6AA00",
    "#A76633"
)

fun Plan.toUIModel() = CareerItemUIModel(
    id!!, type, title, startDate, endDate, color ?: "#2562FF", 0.2f, dayOfWeeks, memo, status
)
