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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val _modifyCareerUseCaseResult = MutableStateFlow<Result<Plan>>(Result.Loading)
    val modifyCareerUseCaseResult = _modifyCareerUseCaseResult.asStateFlow()

    private val _deleteCareerUseCaseResult = MutableStateFlow<Result<Unit>>(Result.Loading)
    val deleteCareerUseCaseResult = _deleteCareerUseCaseResult.asStateFlow()

    private val _setCareerDoneUseCaseResult = MutableStateFlow<Result<Plan>>(Result.Loading)
    val setCareerDoneUseCaseResult = _setCareerDoneUseCaseResult.asStateFlow()

    fun getCareerChecks(planCheckQueryInfo: PlanCheckQueryInfo) {
        viewModelScope.launch {
            getCareerChecksUseCase(planCheckQueryInfo).collectLatest {
                _careerChecksUseCaseResult.value = it
            }
        }
    }

    fun modifyCareer(uiModel: CareerItemUIModel){
//        viewModelScope.launch {
//            modifyCareerUseCase(plan).collectLatest {
//               _modifyCareerUseCaseResult.value = it
//            }
//        }
    }

    fun deleteCareer(careerId: Int){
        viewModelScope.launch {
            deleteCareerUseCase(careerId).collectLatest {
                _deleteCareerUseCaseResult.value = it
            }
        }
    }

    fun setCareerDone(careerId: Int){
        viewModelScope.launch {
            setCareerDoneUseCase(careerId).collectLatest {
                _setCareerDoneUseCaseResult.value = it
            }
        }
    }
}