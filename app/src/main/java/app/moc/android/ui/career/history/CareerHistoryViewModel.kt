package app.moc.android.ui.career.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.career.CareerItemUIModel
import app.moc.model.Plan
import app.moc.model.PlanCheck
import app.moc.model.PlanCheckQueryInfo
import app.moc.shared.domain.career.DeleteCareerUseCase
import app.moc.shared.domain.career.GetCareerChecksUseCase
import app.moc.shared.domain.career.ModifyCareerUseCase
import app.moc.shared.domain.career.SetCareerDoneUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CareerHistoryViewModel @Inject constructor(
    private val getCareerChecksUseCase: GetCareerChecksUseCase,
    private val modifyCareerUseCase: ModifyCareerUseCase,
    private val deleteCareerUseCase: DeleteCareerUseCase,
    private val setCareerDoneUseCase: SetCareerDoneUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _careerChecksUseCaseResult = MutableStateFlow<Result<List<PlanCheck>>>(Result.Loading)
    val careerChecksUseCaseResult = _careerChecksUseCaseResult.asStateFlow()

    private val _modifyCareerUseCaseResult = MutableSharedFlow<Result<Plan>>()
    val modifyCareerUseCaseResult = _modifyCareerUseCaseResult.asSharedFlow()

    private val _deleteCareerUseCaseResult = MutableSharedFlow<Result<Unit>>()
    val deleteCareerUseCaseResult = _deleteCareerUseCaseResult.asSharedFlow()

    private val _setCareerDoneUseCaseResult = MutableSharedFlow<Result<Plan>>()
    val setCareerDoneUseCaseResult = _setCareerDoneUseCaseResult.asSharedFlow()

    private val _careerItemUIModel = MutableStateFlow(CareerItemUIModel.EMPTY)
    val careerItemUIModel = _careerItemUIModel.asStateFlow()

    init {
        setCareerItemUIModel()
    }

    private fun setCareerItemUIModel(){
        val uiModel = savedStateHandle.get<CareerItemUIModel>("uiModel")
        if(uiModel != null){
            _careerItemUIModel.value = uiModel
        }
    }

    fun getCareerChecks(planCheckQueryInfo: PlanCheckQueryInfo) {
        viewModelScope.launch {
            getCareerChecksUseCase(planCheckQueryInfo).collectLatest {
                _careerChecksUseCaseResult.value = it
            }
        }
    }

    fun modifyCareer(uiModel: CareerItemUIModel){
        viewModelScope.launch {
            modifyCareerUseCase(
                Plan(
                    id = uiModel.id,
                    type = uiModel.type,
                    title = uiModel.title,
                    startDate = uiModel.startDate,
                    endDate = uiModel.endDate,
                    dayOfWeeks = uiModel.dayOfWeeks,
                    status = uiModel.status,
                    color = uiModel.color,
                    memo = uiModel.memo
                )
            ).collectLatest {
               _modifyCareerUseCaseResult.emit(it)
            }
        }
    }

    fun deleteCareer(careerId: Int){
        viewModelScope.launch {
            deleteCareerUseCase(careerId).collectLatest {
                _deleteCareerUseCaseResult.emit(it)
            }
        }
    }

    fun setCareerDone(careerId: Int){
        viewModelScope.launch {
            setCareerDoneUseCase(careerId).collectLatest {
                _setCareerDoneUseCaseResult.emit(it)
            }
        }
    }

}