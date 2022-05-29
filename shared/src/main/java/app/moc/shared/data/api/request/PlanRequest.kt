package app.moc.shared.data.api.request

import androidx.annotation.Keep
import app.moc.model.Plan
import app.moc.model.PlanCheck
import com.squareup.moshi.Json
import java.io.File

@Keep
data class PlanRegisterRequest(
    val type: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val goal: String?,
    val checkWeek: String, // TODO: 향후 enum으로 변경
    val color: String?
)

@Keep
data class PlanModifyRequest(
    val planId: Int,
    val type: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val goal: String?,
    val checkWeek: String,
    val userId: String,
    val color: String?
)

fun Plan.toRegisterRequest() = PlanRegisterRequest(
    type, title, startDate / 1000, endDate / 1000, memo, dayOfWeeks, color
)

fun Plan.toModifyRequest(userId: String) = PlanModifyRequest(
    id!!, type, title, startDate / 1000, endDate / 1000, memo, dayOfWeeks, userId, color
)

@Keep
data class PlanCheckRegisterRequest(
    val planId: Int,
    val type: Int,
    val date: Long,
    val satisfact: Int,
    val images: List<File>
)
