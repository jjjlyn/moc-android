package app.moc.shared.domain.community.comment

import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Pair<String, String>, Unit>(ioDispatcher) {
    override fun execute(parameters: Pair<String, String>): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        emit(Result.Success(communityRepository.deleteComment(parameters.first, parameters.second)))
    }
}