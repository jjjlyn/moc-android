package app.moc.shared.domain.career

import app.moc.model.PlanCheck
import app.moc.model.PlanCheckQueryInfo
import app.moc.shared.data.plan.PlanRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class GetCareerChecksUseCase @Inject constructor(
    private val planRepository: PlanRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<PlanCheckQueryInfo, List<PlanCheck>>(ioDispatcher) {
    override fun execute(parameters: PlanCheckQueryInfo): Flow<Result<List<PlanCheck>>> = flow {
        emit(Result.Loading)
        emit(Result.Success(planRepository.getPlanChecks(parameters.planId, parameters.year, parameters.month)))
    }
}