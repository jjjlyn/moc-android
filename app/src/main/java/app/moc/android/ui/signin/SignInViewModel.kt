package app.moc.android.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.SignIn
import app.moc.model.User
import app.moc.shared.domain.signin.SignInUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel(){

    private val _email = MutableStateFlow("")
    private val _pwd = MutableStateFlow("")
    val email = _email.asStateFlow()
    val pwd = _pwd.asStateFlow()

    private val _signInUseCaseResult = MutableStateFlow<Result<User>>(Result.Loading)
    val signInUseCaseResult = _signInUseCaseResult.asStateFlow()

    fun onEmailChanged(email: CharSequence){
        _email.value = email.toString()
    }

    fun onPwdChanged(pwd: CharSequence){
        _pwd.value = pwd.toString()
    }

    fun onSignIn(signIn: SignIn){
        viewModelScope.launch {
            _signInUseCaseResult.value = signInUseCase(signIn)
        }
    }
}