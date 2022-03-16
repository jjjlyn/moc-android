package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.Plan
import app.moc.model.PlanCheck
import com.squareup.moshi.Json

@Keep
data class PlanResponse(
    val result: Boolean,
    val email: String?,
    val userId: Int,
    val planId: Int,
    val type: Int,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val checkWeek: String,
    val goal: String,
    val createDate: Long,
    val modifyDate: Long,
    val status: String?,
    val color: String?
)

@Keep
data class PlanCheckResponse(
    val result: Boolean,
    val planId: Int,
    val date: Long,
    val type: Int,
    val satisfact: Int,
    @Json(name = "image_tag")
    val imageTag: String?
)

fun PlanResponse.toModel() = Plan(
    id = planId,
    type = type.toString(),
    title, startDate * 1000, endDate * 1000, checkWeek, status, color, goal
)

fun PlanCheckResponse.toModel() = PlanCheck(
    id = planId, date = date, type = type.toString(), satisfact = satisfact, imageTag = imageTag
)