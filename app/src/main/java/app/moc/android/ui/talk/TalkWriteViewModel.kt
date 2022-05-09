package app.moc.android.ui.talk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.Community
import app.moc.model.CommunityItemUpload
import app.moc.shared.domain.community.UploadCommunityItemUseCase
import app.moc.shared.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TalkWriteViewModel @Inject constructor(
    private val uploadCommunityItemUseCase: UploadCommunityItemUseCase
) : ViewModel() {

    private val _onCommunityItemUploaded = MutableSharedFlow<Result<Community>>()
    val onCommunityItemUploaded = _onCommunityItemUploaded.asSharedFlow()

    private val _onTagAdded = MutableSharedFlow<String>()
    val onTagAdded = _onTagAdded.asSharedFlow()

    fun addTag(tag: String){
        viewModelScope.launch {
            _onTagAdded.emit(tag)
        }
    }

    fun uploadCommunityItem(upload: CommunityItemUpload){
        viewModelScope.launch {
            uploadCommunityItemUseCase(upload).collectLatest {
                _onCommunityItemUploaded.emit(it)
            }
        }
    }
}