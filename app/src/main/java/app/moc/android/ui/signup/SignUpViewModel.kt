package app.moc.android.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.*
import app.moc.shared.domain.signup.BusinessUseCase
import app.moc.shared.domain.signup.EmailUseCase
import app.moc.shared.domain.signup.NickNameUseCase
import app.moc.shared.domain.signup.SignUpUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
        private val signUpUseCase: SignUpUseCase,
        private val businessUseCase: BusinessUseCase,
        private val emailUseCase: EmailUseCase,
        private val nickNameUseCase: NickNameUseCase
): ViewModel() {
    private val _navigationAction = Channel<SignUpNavigationAction>(Channel.CONFLATED)
    val navigationAction = _navigationAction.receiveAsFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _pwd = MutableStateFlow("")
    val pwd = _pwd.asStateFlow()

    private val _nickName = MutableStateFlow("")
    val nickName = _nickName.asStateFlow()

    private val _business = MutableStateFlow(Business(1, 101, "QA(Quality Assurance)", Business.CATEGORY_SUB))
    val business = _business.asStateFlow()

    private val _keyWords = MutableStateFlow(emptySet<KeyWord>())
    val keyWords = _keyWords.asStateFlow()

    private val _leaveDate = MutableStateFlow<DateTime?>(null)
    val leaveDate = _leaveDate.asStateFlow()

    private val _signUpUseCaseResult = MutableSharedFlow<Result<User>>()
    val signUpUseCaseResult = _signUpUseCaseResult.asSharedFlow()

    private val _emailUseCaseResult = MutableSharedFlow<Result<Unit>>()
    val emailUseCaseResult = _emailUseCaseResult.asSharedFlow()

    private val _nickNameUseCaseResult = MutableSharedFlow<Result<Unit>>()
    val nickNameUseCaseResult = _nickNameUseCaseResult.asSharedFlow()

    val isNextEnabled = combine(_email, _pwd){ email, pwd ->
        isNextEnabled(email, pwd)
    }

    private val _businessUseCaseResult = MutableStateFlow<Result<List<Business>>>(Result.Loading)

    private fun isNextEnabled(email: String, pwd: String): Boolean {
        return email.isNotEmpty() && pwd.isNotEmpty()
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
        viewModelScope.launch {
            emailUseCase(_email.value).collectLatest {
                _emailUseCaseResult.emit(it)
            }
        }
    }

    fun checkNickNameDuplicate(){
        viewModelScope.launch {
            nickNameUseCase(_nickName.value).collectLatest {
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
            business = _business.value.categoryS ?: return,
            email = _email.value,
            keyWords = _keyWords.value.joinToString { it.content },
            leaveDate = _leaveDate.value?.time ?: return,
            nickName = _nickName.value,
            pwd = _pwd.value
        )
        viewModelScope.launch {
            signUpUseCase(signUp).collectLatest {
                _signUpUseCaseResult.emit(it)
            }
        }
    }

    private fun getBusiness(){
        viewModelScope.launch {
            _businessUseCaseResult.value = businessUseCase(Unit)
        }
    }
}

sealed class SignUpNavigationAction {
    object NavigateToDetail : SignUpNavigationAction()
}