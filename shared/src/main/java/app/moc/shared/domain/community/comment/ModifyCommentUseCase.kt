package app.moc.shared.domain.community.comment

import app.moc.model.Comment
import app.moc.model.CommentModify
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ModifyCommentUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<CommentModify, Comment>(ioDispatcher) {
    override fun execute(parameters: CommentModify): Flow<Result<Comment>> = flow {
        emit(Result.Loading)
        emit(Result.Success(communityRepository.modifyComment(parameters)))
    }
}