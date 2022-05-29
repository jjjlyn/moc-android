package app.moc.shared.domain.career

import app.moc.model.PlanCheck
import app.moc.shared.data.api.request.PlanCheckRegisterRequest
import app.moc.shared.data.api.request.toRegisterRequest
import app.moc.shared.data.plan.PlanRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class RegisterCareerCheckUseCase @Inject constructor(
    private val planRepository: PlanRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<PlanCheckRegisterRequest, PlanCheck>(ioDispatcher) {
    override suspend fun execute(parameters: PlanCheckRegisterRequest) : PlanCheck {
        return planRepository.registerPlanCheck(parameters)
    }
}