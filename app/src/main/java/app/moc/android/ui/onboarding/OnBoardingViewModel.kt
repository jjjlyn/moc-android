package app.moc.android.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.shared.domain.prefs.OnBoardingCompleteActionUseCase
import app.moc.shared.domain.prefs.OnBoardingCompletedUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val onBoardingDelegate: OnBoardingDelegate
): ViewModel(), OnBoardingDelegate by onBoardingDelegate {

    private val _navigationAction = Channel<OnBoardingNavigationAction>(Channel.CONFLATED)
    val navigationAction = _navigationAction.receiveAsFlow()

    fun onSignUpClick() {
        viewModelScope.launch {
            _navigationAction.send(OnBoardingNavigationAction.NavigateToSignUp)
        }
    }

    fun onSignInClick() {
        viewModelScope.launch {
            _navigationAction.send(OnBoardingNavigationAction.NavigateToSignIn)
        }
    }
}

sealed class OnBoardingNavigationAction {
    object NavigateToSignIn : OnBoardingNavigationAction()
    object NavigateToSignUp : OnBoardingNavigationAction()
}