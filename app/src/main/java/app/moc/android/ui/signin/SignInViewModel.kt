package app.moc.android.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.onboarding.OnBoardingDelegate
import app.moc.model.SignIn
import app.moc.model.User
import app.moc.shared.domain.prefs.OnBoardingCompleteActionUseCase
import app.moc.shared.domain.signin.SignInUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val onBoardingDelegate: OnBoardingDelegate
) : ViewModel(), OnBoardingDelegate by onBoardingDelegate {

    private val _email = MutableStateFlow("")
    private val _pwd = MutableStateFlow("")

    val isSignInEnabled = combine(_email, _pwd){ email, pwd ->
        isSignInEnabled(email, pwd)
    }

    private val _signInUseCaseResult = MutableSharedFlow<Result<User>>()
    val signInUseCaseResult = _signInUseCaseResult.asSharedFlow()

    private fun isSignInEnabled(email: String, pwd: String): Boolean {
        return email.isNotBlank() && pwd.isNotBlank()
    }

    fun onEmailChanged(email: CharSequence){
        _email.value = email.toString()
    }

    fun onPwdChanged(pwd: CharSequence){
        _pwd.value = pwd.toString()
    }

    fun onSignIn(){
        viewModelScope.launch {
            signInUseCase(SignIn(_email.value, _pwd.value)).collectLatest {
                _signInUseCaseResult.emit(it)
            }
        }
    }
}