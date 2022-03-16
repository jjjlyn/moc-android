package app.moc.shared.domain.career

import app.moc.shared.data.plan.PlanRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class DeleteCareerUseCase @Inject constructor(
    private val planRepository: PlanRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Int, Unit>(ioDispatcher) {
    override fun execute(parameters: Int): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        emit(Result.Success(planRepository.deletePlan(parameters)))
    }
}