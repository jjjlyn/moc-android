package app.moc.shared.domain.prefs

import app.moc.shared.data.prefs.PreferencesStorage
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 온보딩 완료 시점에 리턴합니다.
 */
open class OnBoardingCompletedUseCase @Inject constructor(
    private val preferenceStorage: PreferencesStorage,
    @IODispatcher dispatcher: CoroutineDispatcher
): FlowUseCase<Unit, Boolean>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<Boolean>> {
        return preferenceStorage.onBoardingCompleted.map { Result.Success(it) }
    }
}