package app.moc.shared.domain.prefs

import app.moc.shared.data.prefs.PreferencesStorage
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class GetLeftDaysUseCase @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Long>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Long>> =
        preferencesStorage.leftDays.map { Result.Success(it) }
}