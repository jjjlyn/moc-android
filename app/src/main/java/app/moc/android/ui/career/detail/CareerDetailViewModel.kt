package app.moc.android.ui.career.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.util.WhileViewSubscribed
import app.moc.android.util.fmt
import app.moc.model.DateTime
import app.moc.model.Plan
import app.moc.model.PlanCheck
import app.moc.shared.domain.career.ModifyCareerUseCase
import app.moc.shared.domain.career.RegisterCareerCheckUseCase
import app.moc.shared.domain.career.RegisterCareerUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CareerDetailViewModel @Inject constructor(
    private val registerCareerUseCase: RegisterCareerUseCase
): ViewModel() {

    private val _careerName = MutableStateFlow("")

    private val _careerType = MutableStateFlow(1)

    private val _startDate = MutableStateFlow(DateTime())
    val startDate = _startDate.map {
        "yy.MM.dd".fmt(it)
    }.stateIn(viewModelScope, WhileViewSubscribed, "시작날짜")

    private val _endDate = MutableStateFlow(DateTime())
    val endDate = _endDate.map {
        "yy.MM.dd".fmt(it)
    }.stateIn(viewModelScope, WhileViewSubscribed, "종료날짜")

    private val _dayOfWeeks = MutableStateFlow("")

    private val _color = MutableStateFlow("#2562FF")
    val color = _color.asStateFlow()

    private val _memo = MutableStateFlow("")

    val isConfirmEnabled = combine(
        _careerName,
        _careerType,
        _startDate,
        _endDate,
        _dayOfWeeks
    ) { careerName, careerType, startDate, endDate, dayOfWeeks ->
        careerName.isNotEmpty() && dayOfWeeks.isNotEmpty() && endDate.after(startDate)
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    private val _registerResult = MutableSharedFlow<Result<Plan>>()
    val registerResult = _registerResult.asSharedFlow()

    fun onColorChanged(color: String){
        _color.value = color
    }

    fun onCareerNameChanged(careerName: CharSequence) {
        _careerName.value = careerName.toString()
    }

    fun setCareerType(careerType: Int) {
        _careerType.value = careerType
    }

    fun setStartDate(startDate: DateTime) {
        _startDate.value = startDate
    }

    fun setEndDate(endDate: DateTime) {
        _endDate.value = endDate
    }

    fun setDayOfWeeks(dayOfWeeks: String) {
        _dayOfWeeks.value = dayOfWeeks
    }

    fun onMemoChanged(memo: CharSequence) {
        _memo.value = memo.toString()
    }

    fun registerCareer() {
        viewModelScope.launch {
            _registerResult.emit(
                registerCareerUseCase(
                    Plan(
                        type = _careerType.value.toString(),
                        title = _careerName.value,
                        startDate = _startDate.value.time,
                        endDate = _endDate.value.time,
                        dayOfWeeks = _dayOfWeeks.value,
                        color = _color.value,
                        memo = _memo.value,
                    )
                )
            )
        }
    }
}