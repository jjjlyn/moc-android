package app.moc.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.MainNavigationAction.*
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
    onBoardingCompletedUseCase: OnBoardingCompletedUseCase
) : ViewModel(){
    val mainDestination = onBoardingCompletedUseCase(Unit).map { result ->
        if (result.data == false) {
            NavigateToOnBoarding
        } else {
            NavigateToHome
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, Result.Loading)
}

sealed class MainNavigationAction {
    object NavigateToOnBoarding : MainNavigationAction()
    object NavigateToHome : MainNavigationAction()
}