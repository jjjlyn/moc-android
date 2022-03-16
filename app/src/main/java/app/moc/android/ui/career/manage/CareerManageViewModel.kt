package app.moc.android.ui.career.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.career.CareerManageListItemUIModel
import app.moc.android.ui.career.toUIModel
import app.moc.android.util.WhileViewSubscribed
import app.moc.android.util.fmt
import app.moc.model.DateTime
import app.moc.model.Plan
import app.moc.shared.domain.career.GetCompletedCareersUseCase
import app.moc.shared.domain.career.GetInProgressCareersUseCase
import app.moc.shared.result.Result
import app.moc.shared.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CareerManageViewModel @Inject constructor(
    private val getInProgressCareersUseCase: GetInProgressCareersUseCase,
    private val getCompletedCareersUseCase: GetCompletedCareersUseCase
): ViewModel() {

    private val _today = MutableStateFlow("yyyy MM월 dd일".fmt(DateTime()))
    val today = _today.asStateFlow()

    private val _inProgressCareersResult = MutableStateFlow<Result<List<Plan>>>(Result.Loading)
    private val _completedCareersResult = MutableStateFlow<Result<List<Plan>>>(Result.Loading)

    val totalResult = combine(_inProgressCareersResult, _completedCareersResult){ a, b ->
        buildTotalContent(a, b)
    }.stateIn(viewModelScope, WhileViewSubscribed, Result.Loading)

    private fun buildTotalContent(a: Result<List<Plan>>, b: Result<List<Plan>>) : Result<List<CareerManageListItemUIModel>> {
        return if(a is Result.Success && b is Result.Success){
            Result.Success(listOf(
                CareerManageListItemUIModel(
                    title = "진행중",
                    list = a.data.map { plan -> plan.toUIModel() }
                ),
                CareerManageListItemUIModel(
                    title = "완료",
                    list = b.data.map { plan -> plan.toUIModel() }
                )
            ))
        } else if(a.isLoading || b.isLoading){
            Result.Loading
        } else {
            // TODO
            Result.Error(Throwable("error"))
        }
    }

    val isTotalCareerEmpty = _inProgressCareersResult.combine(_completedCareersResult) { a, b ->
        val aData = a.data
        val bData = b.data
        if(aData != null && bData != null){
            aData.isEmpty() && bData.isEmpty()
        } else {
            false
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, false)

    init {
        getCareers()
    }

    fun getCareers(){
        viewModelScope.launch {
            getInProgressCareers()
            getCompletedCareers()
        }
    }

    private suspend fun getInProgressCareers(){
        getInProgressCareersUseCase(Unit).collectLatest {
            _inProgressCareersResult.value = it
        }
    }

    private suspend fun getCompletedCareers(){
        getCompletedCareersUseCase(Unit).collectLatest {
            _completedCareersResult.value = it
        }
    }
}