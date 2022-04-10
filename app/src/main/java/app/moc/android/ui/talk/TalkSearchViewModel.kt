package app.moc.android.ui.talk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.util.WhileViewSubscribed
import app.moc.model.Community
import app.moc.shared.domain.community.GetSearchResultsUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TalkSearchViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase
) : ViewModel() {

    private val _keyword = MutableStateFlow<List<String>>(emptyList())
    val keyword = _keyword.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults : StateFlow<Result<List<Community>>> =
        _keyword.transformLatest { keyword ->
            if(keyword.isEmpty()) return@transformLatest
            getSearchResultsUseCase(keyword).collect {
                emit(it)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, Result.Loading)

    fun onKeywordChanged(keyword: List<String>){
        _keyword.value = keyword
    }
}