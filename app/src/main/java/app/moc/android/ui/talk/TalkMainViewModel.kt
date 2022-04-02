package app.moc.android.ui.talk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.moc.model.Community
import app.moc.shared.data.community.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TalkMainViewModel @Inject constructor(
    private val communityRepository: CommunityRepository
) : ViewModel() {

    private val _latestCommunities = MutableStateFlow<PagingData<Community>>(PagingData.empty())
    val latestCommunities = _latestCommunities.asStateFlow()

    init {
        getLatestCommunities()
    }

    private fun getLatestCommunities(){
        viewModelScope.launch {
            communityRepository.getLatestCommunities().cachedIn(viewModelScope).collectLatest {
                _latestCommunities.value = it
            }
        }
    }
}