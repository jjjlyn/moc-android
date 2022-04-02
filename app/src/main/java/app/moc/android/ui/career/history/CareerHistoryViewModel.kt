package app.moc.android.ui.career.history

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
    private val setCareerDoneUseCase: SetCareerDoneUseCase
): ViewModel() {
    private val _careerChecksUseCaseResult = MutableStateFlow<Result<List<PlanCheck>>>(Result.Loading)
    val careerChecksUseCaseResult = _careerChecksUseCaseResult.asStateFlow()

    private val _modifyCareerUseCaseResult = MutableSharedFlow<Result<Plan>>()
    val modifyCareerUseCaseResult = _modifyCareerUseCaseResult.asSharedFlow()

    private val _deleteCareerUseCaseResult = MutableSharedFlow<Result<Unit>>()
    val deleteCareerUseCaseResult = _deleteCareerUseCaseResult.asSharedFlow()

    private val _setCareerDoneUseCaseResult = MutableSharedFlow<Result<Plan>>()
    val setCareerDoneUseCaseResult = _setCareerDoneUseCaseResult.asSharedFlow()

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

    // 삭제나 완료 시 뒤로 가면서 목록이 업데이트 되어야 하는데 이건 notifyItemChanged()로 구현하면 된다.
}