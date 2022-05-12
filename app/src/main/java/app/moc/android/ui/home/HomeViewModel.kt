package app.moc.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.moc.android.util.WhileViewSubscribed
import app.moc.model.Community
import app.moc.model.Plan
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.domain.career.GetCareerChecksUseCase
import app.moc.shared.domain.career.GetInProgressCareersUseCase
import app.moc.shared.domain.prefs.GetLeftDaysUseCase
import app.moc.shared.domain.prefs.GetNickNameUseCase
import app.moc.shared.result.Result
import app.moc.shared.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getInProgressCareersUseCase: GetInProgressCareersUseCase,
    getNickNameUseCase: GetNickNameUseCase,
    getLeftDaysUseCase: GetLeftDaysUseCase,
    private val communityRepository: CommunityRepository,
): ViewModel() {
    private val _todayCheckUseCaseResult = MutableStateFlow<Result<List<Plan>>>(Result.Loading)
    val todayCheckUseCaseResult = _todayCheckUseCaseResult.asStateFlow()

    private val _latestCommunities = MutableStateFlow<PagingData<Community>>(PagingData.empty())
    val latestCommunities = _latestCommunities.asStateFlow()

    private val _inProgressCareerResult = MutableStateFlow<Result<List<Plan>>>(Result.Loading)
    val inProgressCareerResult = _inProgressCareerResult.asStateFlow()

    val homeIntroUIModel = combine(getNickNameUseCase(Unit), getLeftDaysUseCase(Unit)) { nickNameResult, leftDaysResult ->
        HomeIntroUIModel(nickNameResult.data ?: "", leftDaysResult.data ?: 0)
    }.stateIn(viewModelScope, WhileViewSubscribed, HomeIntroUIModel())

    init {
        getTodayChecks()
        getLatestCommunities()
    }

    fun getTodayChecks() {
        viewModelScope.launch {
            getInProgressCareersUseCase(Unit).collectLatest {
                _todayCheckUseCaseResult.value = it
            }
        }
    }

    private fun getLatestCommunities(){
        viewModelScope.launch {
            communityRepository.getLatestCommunities(-1)
                .cachedIn(viewModelScope)
                .collectLatest {
                    _latestCommunities.value = it
                }
        }
    }
}