package app.moc.android.ui.talk

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.android.ui.home.MocTalkItemUIModel
import app.moc.model.Comment
import app.moc.model.CommentModify
import app.moc.model.CommentUpload
import app.moc.shared.domain.community.DeleteCommunityItemUseCase
import app.moc.shared.domain.community.ModifyCommunityItemUseCase
import app.moc.shared.domain.community.comment.DeleteCommentUseCase
import app.moc.shared.domain.community.comment.GetCommentsUseCase
import app.moc.shared.domain.community.comment.ModifyCommentUseCase
import app.moc.shared.domain.community.comment.UploadCommentUseCase
import app.moc.shared.result.Result
import app.moc.shared.result.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TalkDetailViewModel @Inject constructor(
    private val modifyCommunityItemUseCase: ModifyCommunityItemUseCase,
    private val deleteCommunityItemUseCase: DeleteCommunityItemUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val uploadCommentUseCase: UploadCommentUseCase,
    private val modifyCommentUseCase: ModifyCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _comments = MutableStateFlow<Result<List<TalkCommentUIModel>>>(Result.Loading)
    val comments = _comments.asStateFlow()

    private val _onCommentUpload = MutableSharedFlow<TalkCommentUIModel>()
    val onCommentUpload = _onCommentUpload.asSharedFlow()

    private val _onCommentUploaded = MutableSharedFlow<Result<Comment>>()
    val onCommentUploaded = _onCommentUploaded.asSharedFlow()

    private val _onCommentDelete = MutableSharedFlow<Pair<Long, Int>>()
    val onCommentDelete = _onCommentDelete.asSharedFlow()

    private val _onCommentDeleted = MutableSharedFlow<Result<Unit>>()
    val onCommentDeleted = _onCommentDeleted.asSharedFlow()

    private val _onCommentModified = MutableSharedFlow<Result<Comment>>()
    val onCommentModified = _onCommentModified.asSharedFlow()

    private val _onCommentModify = MutableSharedFlow<TalkCommentUIModel>() // boardID, commentID
    val onCommentModify = _onCommentModify.asSharedFlow()
    
    init {
        getComments()
    }

    fun requestUploadComment(uiModel: TalkCommentUIModel){
        viewModelScope.launch {
            _onCommentUpload.emit(uiModel)
        }
    }

    fun requestDeleteComment(boardID: Long, commentID: Int){
        viewModelScope.launch {
            _onCommentDelete.emit(Pair(boardID, commentID))
        }
    }

    fun requestModifyCommentFocus(uiModel: TalkCommentUIModel){
        viewModelScope.launch {
            _onCommentModify.emit(uiModel)
        }
    }

    fun modifyComment(modify: CommentModify){
        viewModelScope.launch {
            modifyCommentUseCase(modify).collectLatest { result ->
                _onCommentModified.emit(result)
            }
        }
    }

    fun deleteComment(boardID: String, commentID: String){
        viewModelScope.launch {
            deleteCommentUseCase(Pair(boardID, commentID)).collectLatest { result ->
                _onCommentDeleted.emit(result)
            }
        }
    }

    fun getComments(){
        val talkItemUIModel = savedStateHandle.get<MocTalkItemUIModel>("uiModel")
        viewModelScope.launch {
            talkItemUIModel?.let {
                getCommentsUseCase(it.id.toString()).collectLatest { result ->
                    _comments.value = result.map { list ->
                        list.map { item -> item.toUIModel() }
                    }
                }
            }
        }
    }

    fun uploadComment(upload: CommentUpload){
        viewModelScope.launch {
            uploadCommentUseCase(upload).collectLatest {
                _onCommentUploaded.emit(it)
            }
        }
    }
}