package app.moc.shared.data.api

import app.moc.shared.data.api.response.PlanResponse
import retrofit2.http.GET

interface PlanService {

    @GET("plan/getInprgPlan")
    suspend fun fetchInProgressPlans(): List<PlanResponse>
}