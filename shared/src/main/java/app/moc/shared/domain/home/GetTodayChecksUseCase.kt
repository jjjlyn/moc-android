package app.moc.shared.domain.home

import app.moc.model.Plan
import app.moc.shared.data.plan.PlanRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class GetTodayChecksUseCase @Inject constructor(
        private val planRepository: PlanRepository,
        @IODispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Unit, List<Plan>>(ioDispatcher) {
    override suspend fun execute(parameters: Unit): List<Plan> {
        return planRepository.getInProgressPlans()
    }
}