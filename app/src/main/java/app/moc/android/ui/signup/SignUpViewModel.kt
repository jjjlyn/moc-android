package app.moc.android.ui.signup

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.onboarding.OnBoardingDelegate
import app.moc.model.*
import app.moc.shared.domain.signup.CheckEmailDuplicateUseCase
import app.moc.shared.domain.signup.CheckNickNameDuplicateUseCase
import app.moc.shared.domain.signup.SignUpUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private val REGEX_PASSWORD = "^[A-Za-z0-9]{8,12}\$"
    .toRegex()
    .toPattern()

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val checkNickNameDuplicateUseCase: CheckNickNameDuplicateUseCase,
    private val onBoardingDelegate: OnBoardingDelegate
): ViewModel(), OnBoardingDelegate by onBoardingDelegate {
    private val _navigationAction = Channel<SignUpNavigationAction>(Channel.CONFLATED)
    val navigationAction = _navigationAction.receiveAsFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _pwd = MutableStateFlow("")
    val pwd = _pwd.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickName = _nickName.asStateFlow()

    private val _business = MutableStateFlow<Business?>(null)
    val business = _business.asStateFlow()

    private val _keyWords = MutableStateFlow(emptySet<KeyWord>())
    val keyWords = _keyWords.asStateFlow()

    private val _leaveDate = MutableStateFlow<DateTime?>(null)
    val leaveDate = _leaveDate.asStateFlow()

    private val _signUpUseCaseResult = MutableSharedFlow<Result<User>>()
    val signUpUseCaseResult = _signUpUseCaseResult.asSharedFlow()

    private val _emailUseCaseResult = MutableSharedFlow<Result<User>>()
    val emailUseCaseResult = _emailUseCaseResult.asSharedFlow()

    private val _nickNameUseCaseResult = MutableSharedFlow<Result<User>>()
    val nickNameUseCaseResult = _nickNameUseCaseResult.asSharedFlow()

    val emailValidCheck = MutableStateFlow(EmailValidCheck("", false)) // check duplicate
    val isNickNameValid = MutableStateFlow(false) // check duplicate

    val isNextEnabled = combine(emailValidCheck, _pwd){ emailValidCheck, pwd ->
        isNextEnabled(emailValidCheck.checkedEmail, emailValidCheck.isValid, pwd)
    }
    val isSignUpEnabled = combine(isNickNameValid, _business, _keyWords, _leaveDate){ isNickNameValid, business, keyWords, leaveDate ->
        isSignUpEnabled(isNickNameValid, business, keyWords, leaveDate)
    }

    private fun isNextEnabled(email: String, isEmailValid: Boolean, pwd: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches() &&
                isEmailValid &&
                REGEX_PASSWORD.matcher(pwd).matches()
    }

    private fun isSignUpEnabled(isNickNameValid: Boolean, business: Business?, keyWords: Set<KeyWord>, leaveDate: DateTime?): Boolean {
        return isNickNameValid &&
                business != null &&
                keyWords.isNotEmpty() &&
                leaveDate != null
    }

    fun getBusinessByMain(businessMain: Business) : List<Business> {
        return businessSub.filter { it.categoryM == businessMain.categoryM }
    }

    fun onEmailChanged(email: CharSequence){
        _email.value = email.toString()
    }

    fun onPwdChanged(pwd: CharSequence){
        _pwd.value = pwd.toString()
    }

    fun onNickNameChanged(nickName: CharSequence){
        _nickName.value = nickName.toString()
    }

    fun onNextClick(){
        viewModelScope.launch {
            _navigationAction.send(SignUpNavigationAction.NavigateToDetail)
        }
    }

    fun checkEmailDuplicate(){
        if(_email.value.isEmpty()) return
        viewModelScope.launch {
            checkEmailDuplicateUseCase(_email.value).collectLatest {
                _emailUseCaseResult.emit(it)
            }
        }
    }

    fun checkNickNameDuplicate(){
        if(_nickName.value.isEmpty()) return
        viewModelScope.launch {
            checkNickNameDuplicateUseCase(_nickName.value).collectLatest {
                _nickNameUseCaseResult.emit(it)
            }
        }
    }

    fun onBusinessChanged(business: Business){
        _business.value = business
    }

    fun onKeyWordsChanged(keyWord: Set<KeyWord>){
        _keyWords.value = keyWord
    }

    fun onLeaveDateChanged(leaveDate: DateTime){
        _leaveDate.value = leaveDate
    }

    fun onSignUp(){
        val signUp = SignUp(
            business = _business.value?.categoryS ?: return,
            email = _email.value,
            keyWords = _keyWords.value.joinToString { it.content },
            leaveDate = _leaveDate.value?.time?.let { it / 1000 } ?: return,
            nickName = _nickName.value,
            pwd = _pwd.value
        )
        viewModelScope.launch {
            signUpUseCase(signUp).collectLatest {
                _signUpUseCaseResult.emit(it)
            }
        }
    }
}

sealed class SignUpNavigationAction {
    object NavigateToDetail : SignUpNavigationAction()
}