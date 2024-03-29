package app.moc.shared.domain.prefs

import app.moc.model.User
import app.moc.shared.data.prefs.PreferencesStorage
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class OnBoardingCompleteActionUseCase @Inject constructor(
    private val preferencesStorage: PreferencesStorage,
    @IODispatcher dispatcher: CoroutineDispatcher
): UseCase<User, Unit>(dispatcher) {
    override suspend fun execute(parameters: User) {
        preferencesStorage.completeOnBoarding(parameters)
    }
}