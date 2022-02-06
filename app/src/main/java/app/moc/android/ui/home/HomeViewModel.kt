package app.moc.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import app.moc.android.util.WhileViewSubscribed
import app.moc.model.Community
import app.moc.model.Plan
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.home.GetTodayChecksUseCase
import app.moc.shared.domain.prefs.GetLeftDaysUseCase
import app.moc.shared.domain.prefs.GetNickNameUseCase
import app.moc.shared.result.Result
import app.moc.shared.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTodayChecksUseCase: GetTodayChecksUseCase,
    getNickNameUseCase: GetNickNameUseCase,
    getLeftDaysUseCase: GetLeftDaysUseCase,
    private val communityRepository: CommunityRepository
): ViewModel() {
    private val _todayCheckUseCaseResult = MutableStateFlow<Result<List<Plan>>>(Result.Loading)
    val todayCheckUseCaseResult = _todayCheckUseCaseResult.asStateFlow()

    private val _latestCommunities = MutableStateFlow<PagingData<Community>>(PagingData.empty())
    val latestCommunities = _latestCommunities.asStateFlow()

    val homeIntroUIModel = combine(getNickNameUseCase(Unit), getLeftDaysUseCase(Unit)) { nickNameResult, leftDaysResult ->
        HomeIntroUIModel(nickNameResult.data ?: "", leftDaysResult.data ?: 0)
    }.stateIn(viewModelScope, WhileViewSubscribed, HomeIntroUIModel())

    init {
        viewModelScope.launch {
            getTodayChecks()
            getLatestCommunities()
        }
    }

    private suspend fun getTodayChecks() {
        _todayCheckUseCaseResult.value = getTodayChecksUseCase(Unit)
    }

    private suspend fun getLatestCommunities(){
        communityRepository.getLatestCommunities(-1).collectLatest {
            _latestCommunities.value = it
        }
    }
}