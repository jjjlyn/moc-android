package app.moc.android.ui.onboarding

import app.moc.android.util.WhileViewSubscribed
import app.moc.model.User
import app.moc.shared.di.ApplicationScope
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.prefs.OnBoardingCompleteActionUseCase
import app.moc.shared.domain.prefs.OnBoardingCompletedUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OnBoardingDelegate {
    val onBoardingCompleted: Flow<Result<Boolean>>
    fun onBoardingComplete(user: User)
}

class DefaultOnBoardingDelegate @Inject constructor(
    private val onBoardingCompletedUseCase: OnBoardingCompletedUseCase,
    private val onBoardingCompleteActionUseCase: OnBoardingCompleteActionUseCase,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : OnBoardingDelegate {

    private val _onBoardingCompleted = onBoardingCompletedUseCase(Unit)
    override val onBoardingCompleted : StateFlow<Result<Boolean>> = _onBoardingCompleted
        .stateIn(externalScope, WhileViewSubscribed, Result.Loading)

    override fun onBoardingComplete(user: User) {
        externalScope.launch(ioDispatcher) {
            onBoardingCompleteActionUseCase(user)
        }
    }
}