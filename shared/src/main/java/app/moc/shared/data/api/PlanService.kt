package app.moc.shared.data.api

import app.moc.shared.data.api.request.PlanCheckRegisterRequest
import app.moc.shared.data.api.request.PlanModifyRequest
import app.moc.shared.data.api.request.PlanRegisterRequest
import app.moc.shared.data.api.response.PlanCheckResponse
import app.moc.shared.data.api.response.PlanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PlanService {
    @GET("plan/getInprgPlan")
    suspend fun fetchInProgressPlans(): List<PlanResponse>

    @GET("plan/getCompPlan")
    suspend fun fetchCompletedPlans(): List<PlanResponse>

    @POST("plan/addPlan")
    suspend fun registerPlan(@Body planRegisterRequest: PlanRegisterRequest): PlanResponse

    @PUT("plan/updatePlan")
    suspend fun modifyPlan(@Body planModifyRequest: PlanModifyRequest): PlanResponse

    @DELETE("plan/deletePlan")
    suspend fun deletePlan(@Query("planId") planId: Int)

    @PUT("plan/updatePlanComp")
    suspend fun setPlanDone(@Query("planId") planId: Int): PlanResponse

    @Multipart
    @POST("plan/addPlanCheck")
    suspend fun registerPlanCheck(
        @Part("planId") planId: RequestBody,
        @Part("type") type: RequestBody,
        @Part("date") date: RequestBody,
        @Part("satisfact") satisfact: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): PlanCheckResponse

    @GET("plan/getPlanCheckList")
    suspend fun fetchPlanChecks(
        @Query("planId") planId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<PlanCheckResponse>
}