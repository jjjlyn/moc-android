package app.moc.shared.domain.career

import app.moc.model.Plan
import app.moc.shared.data.api.request.toRegisterRequest
import app.moc.shared.data.plan.PlanRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class RegisterCareerUseCase @Inject constructor(
    private val planRepository: PlanRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Plan, Plan>(ioDispatcher) {
    override suspend fun execute(parameters: Plan) : Plan {
        return planRepository.registerPlan(parameters.toRegisterRequest())
    }
}