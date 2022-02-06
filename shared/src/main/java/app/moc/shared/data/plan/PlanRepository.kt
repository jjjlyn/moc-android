package app.moc.shared.data.plan

import app.moc.model.Plan
import app.moc.shared.data.api.PlanService
import app.moc.shared.data.api.response.toModel

interface PlanRepository {
    suspend fun getInProgressPlans(): List<Plan>
}

class DefaultPlanRepository(
    private val service: PlanService
) : PlanRepository {
    override suspend fun getInProgressPlans(): List<Plan> {
        return runCatching {
            service.fetchInProgressPlans()
        }.getOrThrow().map { it.toModel() }
    }
}