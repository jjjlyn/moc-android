package app.moc.android.ui.career.check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.PlanCheck
import app.moc.shared.domain.career.RegisterCareerCheckUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CareerCheckDialogViewModel @Inject constructor(
    private val registerCareerCheckUseCase: RegisterCareerCheckUseCase
): ViewModel() {

    private val _satisfact = MutableStateFlow(0)
    val satisfact = _satisfact.asStateFlow()

    private val _registerCheckResult = MutableSharedFlow<Result<PlanCheck>>()
    val registerCheckResult = _registerCheckResult.asSharedFlow()

    fun setSatisfact(satisfact: Int){
        _satisfact.value = satisfact
    }

    fun registerCareerCheck(planCheck: PlanCheck){
        viewModelScope.launch {
            _registerCheckResult.emit(
                registerCareerCheckUseCase(planCheck)
            )
        }
    }
}