package app.moc.android.ui.career.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.android.ui.career.DayOfWeek
import app.moc.android.ui.career.dayOfWeeks
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
    private val registerCareerUseCase: RegisterCareerUseCase,
    private val modifyCareerUseCase: ModifyCareerUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _careerName = MutableStateFlow("")
    val careerName = _careerName.asStateFlow()

    private val _careerType = MutableStateFlow(1)
    val careerType = _careerType.asStateFlow()

    private val _startDate = MutableStateFlow(DateTime())
    val startDate = _startDate.map {
        "yy.MM.dd".fmt(it)
    }.stateIn(viewModelScope, WhileViewSubscribed, "시작날짜")

    private val _endDate = MutableStateFlow(DateTime())
    val endDate = _endDate.map {
        "yy.MM.dd".fmt(it)
    }.stateIn(viewModelScope, WhileViewSubscribed, "종료날짜")

    private val _dayOfWeeks = MutableStateFlow("")
    val currentDayOfWeeksSelection = _dayOfWeeks.map {
        it.replace(" ", "").split(",").mapNotNull {
            val item = dayOfWeeks.find { dayOfWeek -> it == dayOfWeek.dayOfWeekEng }
            item
        }.toSet()
    }.stateIn(viewModelScope, WhileViewSubscribed, emptySet())

    private val _color = MutableStateFlow("#2562FF")
    val color = _color.asStateFlow()

    private val _memo = MutableStateFlow("")
    val memo = _memo.asStateFlow()

    val isConfirmEnabled = combine(
        _careerName,
        _careerType,
        _startDate,
        _endDate,
        _dayOfWeeks
    ) { careerName, careerType, startDate, endDate, dayOfWeeks ->
        careerName.isNotEmpty() && dayOfWeeks.isNotEmpty() && endDate.after(startDate)
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    private val _careerItemUIModel = MutableStateFlow<CareerItemUIModel?>(null)
    val careerItemUIModel = _careerItemUIModel.asStateFlow()

    private val _registerResult = MutableSharedFlow<Result<Plan>>()
    val registerResult = _registerResult.asSharedFlow()

    private val _modifyResult = MutableSharedFlow<Result<Plan>>()
    val modifyResult = _modifyResult.asSharedFlow()

    init {
        setCareerItemUIModel()
    }

    private fun setCareerItemUIModel(){
        val uiModel = savedStateHandle.get("uiModel") as? CareerItemUIModel ?: return
        _careerItemUIModel.value = uiModel
    }

    private fun modifyCareerItemUIModel(uiModel: CareerItemUIModel){
        _careerItemUIModel.value = uiModel
    }

    fun onColorChanged(color: String){
        _color.value = color
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(color = color))
    }

    fun onCareerNameChanged(careerName: CharSequence) {
        _careerName.value = careerName.toString()
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(title = careerName.toString()))
    }

    fun setCareerType(careerType: Int) {
        _careerType.value = careerType
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(type = careerType.toString()))
    }

    fun setStartDate(startDate: DateTime) {
        _startDate.value = startDate
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(startDate = startDate.time))
    }

    fun setEndDate(endDate: DateTime) {
        _endDate.value = endDate
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(endDate = endDate.time))
    }

    fun setDayOfWeeks(dayOfWeeks: String) {
        _dayOfWeeks.value = dayOfWeeks
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(dayOfWeeks = dayOfWeeks))
    }

    fun onMemoChanged(memo: CharSequence) {
        _memo.value = memo.toString()
        val uiModel = _careerItemUIModel.value ?: return
        modifyCareerItemUIModel(uiModel.copy(memo = memo.toString()))
    }

    fun registerCareer() {
        viewModelScope.launch {
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
            ).collectLatest {
                _registerResult.emit(it)
            }
        }
    }

    fun modifyCareer(id: Int) {
        viewModelScope.launch {
            modifyCareerUseCase(
                Plan(
                    id = id,
                    type = _careerType.value.toString(),
                    title = _careerName.value,
                    startDate = _startDate.value.time,
                    endDate = _endDate.value.time,
                    dayOfWeeks = _dayOfWeeks.value,
                    color = _color.value,
                    memo = _memo.value,
                )
            ).collectLatest {
                _modifyResult.emit(it)
            }
        }
    }
}