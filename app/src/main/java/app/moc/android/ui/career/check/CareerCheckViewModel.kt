package app.moc.android.ui.career.check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.DateTime
import app.moc.model.PlanCheck
import app.moc.model.PlanCheckQueryInfo
import app.moc.shared.data.api.request.PlanCheckRegisterRequest
import app.moc.shared.domain.career.GetCareerChecksUseCase
import app.moc.shared.domain.career.RegisterCareerCheckUseCase
import app.moc.shared.result.Result
import app.moc.shared.result.data
import app.moc.shared.util.toLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import javax.inject.Inject

@HiltViewModel
class CareerCheckDialogViewModel @Inject constructor(
    private val registerCareerCheckUseCase: RegisterCareerCheckUseCase,
    private val getCareerChecksUseCase: GetCareerChecksUseCase
): ViewModel() {

    private val _satisfact = MutableStateFlow(0)
    val satisfact = _satisfact.asStateFlow()

    private val _registerCheckResult = MutableSharedFlow<Result<PlanCheck>>()
    val registerCheckResult = _registerCheckResult.asSharedFlow()

    private val _alreadyCheckedItem = MutableStateFlow<PlanCheck?>(null)
    val alreadyCheckedItem = _alreadyCheckedItem.asStateFlow()

    fun setSatisfact(satisfact: Int){
        _satisfact.value = satisfact
    }

    fun registerCareerCheck(request: PlanCheckRegisterRequest){
        viewModelScope.launch {
            _registerCheckResult.emit(
                registerCareerCheckUseCase(request)
            )
        }
    }

    fun getCurrentCareerChecksUseCase(planID: Int){
        val now = LocalDate.now()
        viewModelScope.launch {
            getCareerChecksUseCase(PlanCheckQueryInfo(planID, now.year, now.monthValue)).collectLatest { result ->
                val data = result.data ?: return@collectLatest
                val alreadyCheckedItem = data.find {
                    val checkedDate = DateTime(it.date).toLocalDate()
                    checkedDate.year == now.year
                            && checkedDate.monthValue == now.monthValue
                            && checkedDate.dayOfMonth == now.dayOfMonth
                }
                _alreadyCheckedItem.value = alreadyCheckedItem
            }
        }
    }
}