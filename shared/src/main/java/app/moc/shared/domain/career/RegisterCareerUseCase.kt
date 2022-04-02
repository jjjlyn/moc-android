package app.moc.shared.domain.career

import app.moc.model.Plan
import app.moc.shared.data.api.request.toRegisterRequest
import app.moc.shared.data.plan.PlanRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.domain.UseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class RegisterCareerUseCase @Inject constructor(
    private val planRepository: PlanRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Plan, Plan>(ioDispatcher) {
    override fun execute(parameters: Plan): Flow<Result<Plan>> = flow {
        emit(Result.Loading)
        emit(Result.Success(planRepository.registerPlan(parameters.toRegisterRequest())))
    }
}