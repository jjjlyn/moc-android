package app.moc.shared.data.api.response

import androidx.annotation.Keep
import app.moc.model.Plan

@Keep
data class PlanResponse(
    val result: Boolean,
    val email: String?,
    val userId: Int,
    val planId: Int,
    val type: String,
    val title: String,
    val startDate: Long,
    val endDate: Long,
    val checkWeek: String,
    val goal: String
)

fun PlanResponse.toModel() = Plan(
    id = planId,
    categoryId = type,
    title, startDate, endDate
)
