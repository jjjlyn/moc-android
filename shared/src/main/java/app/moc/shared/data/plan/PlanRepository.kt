package app.moc.shared.data.plan

import app.moc.model.Plan
import app.moc.model.PlanCheck
import app.moc.shared.data.api.PlanService
import app.moc.shared.data.api.request.PlanCheckRegisterRequest
import app.moc.shared.data.api.request.PlanModifyRequest
import app.moc.shared.data.api.request.PlanRegisterRequest
import app.moc.shared.data.api.response.toModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

interface PlanRepository {
    suspend fun getInProgressPlans(): List<Plan>
    suspend fun getCompletedPlans(): List<Plan>
    suspend fun registerPlan(planRegisterRequest: PlanRegisterRequest): Plan
    suspend fun modifyPlan(planModifyRequest: PlanModifyRequest): Plan
    suspend fun deletePlan(planId: Int)
    suspend fun setPlanDone(planId: Int): Plan
    suspend fun registerPlanCheck(planCheckRegisterRequest: PlanCheckRegisterRequest): PlanCheck
    suspend fun getPlanChecks(planId: Int, year: Int, month: Int): List<PlanCheck>
}

class DefaultPlanRepository(
    private val service: PlanService
) : PlanRepository {

    override suspend fun getInProgressPlans(): List<Plan> {
        return runCatching {
            service.fetchInProgressPlans()
        }.getOrThrow().map { it.toModel() }
    }

    override suspend fun getCompletedPlans(): List<Plan> {
        return runCatching {
            service.fetchCompletedPlans()
        }.getOrThrow().map { it.toModel() }
    }

    override suspend fun registerPlan(planRegisterRequest: PlanRegisterRequest): Plan {
        return runCatching {
            service.registerPlan(planRegisterRequest)
        }.getOrThrow().toModel()
    }

    override suspend fun modifyPlan(planModifyRequest: PlanModifyRequest): Plan {
        return runCatching {
            service.modifyPlan(planModifyRequest)
        }.getOrThrow().toModel()
    }

    override suspend fun deletePlan(planId: Int) {
        return runCatching {
            service.deletePlan(planId)
        }.getOrThrow()
    }

    override suspend fun setPlanDone(planId: Int): Plan {
        return runCatching {
            service.setPlanDone(planId)
        }.getOrThrow().toModel()
    }

    override suspend fun registerPlanCheck(planCheckRegisterRequest: PlanCheckRegisterRequest): PlanCheck {
        return runCatching {
            service.registerPlanCheck(
                planId = planCheckRegisterRequest.planId.toString().toRequestBody(MultipartBody.FORM),
                type = planCheckRegisterRequest.type.toString().toRequestBody(MultipartBody.FORM),
                date = planCheckRegisterRequest.date.toString().toRequestBody(MultipartBody.FORM),
                satisfact = planCheckRegisterRequest.satisfact.toString().toRequestBody(MultipartBody.FORM),
                images = planCheckRegisterRequest.images.map {
                    MultipartBody.Part.createFormData("multipartFile", it.name, it.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
                }
            )
        }.getOrThrow().toModel()
    }

    override suspend fun getPlanChecks(planId: Int, year: Int, month: Int): List<PlanCheck> {
        return runCatching {
            service.fetchPlanChecks(planId, year, month)
        }.getOrThrow().map { it.toModel() }
    }
}