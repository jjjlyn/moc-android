package app.moc.android.ui.talk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.moc.model.Comment
import app.moc.model.CommentUpload
import app.moc.shared.domain.community.DeleteCommunityItemUseCase
import app.moc.shared.domain.community.ModifyCommunityItemUseCase
import app.moc.shared.domain.community.comment.DeleteCommentUseCase
import app.moc.shared.domain.community.comment.GetCommentsUseCase
import app.moc.shared.domain.community.comment.ModifyCommentUseCase
import app.moc.shared.domain.community.comment.UploadCommentUseCase
import app.moc.shared.result.Result
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
    private val deleteCommentUseCase: DeleteCommentUseCase
) : ViewModel() {

    private val _comments = MutableStateFlow<Result<List<Comment>>>(Result.Loading)
    val comments = _comments.asStateFlow()

    private val _onCommentUploaded = MutableSharedFlow<Result<Comment>>()
    val onCommentUpload = _onCommentUploaded.asSharedFlow()

    fun getComments(boardID: String){
        viewModelScope.launch {
            getCommentsUseCase(boardID).collectLatest {
                _comments.value = it
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