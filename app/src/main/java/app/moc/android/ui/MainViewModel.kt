package app.moc.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.MainNavigationAction.*
import app.moc.android.ui.onboarding.OnBoardingDelegate
import app.moc.shared.domain.prefs.OnBoardingCompletedUseCase
import app.moc.shared.result.Result
import app.moc.shared.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    onBoardingDelegate: OnBoardingDelegate
) : ViewModel(), OnBoardingDelegate by onBoardingDelegate{
    val mainDestination = onBoardingCompleted.map { result ->
        when {
            result.isLoading -> {
                Result.Loading
            }
            result.data == false -> {
                NavigateToOnBoarding
            }
            else -> {
                NavigateToHome
            }
        }
    }
}

sealed class MainNavigationAction {
    object NavigateToOnBoarding : MainNavigationAction()
    object NavigateToHome : MainNavigationAction()
}