package app.moc.shared.domain.community.comment

import app.moc.model.Comment
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.data.prefs.PreferencesStorage
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val preferencesStorage: PreferencesStorage,
    @IODispatcher ioDispatcher: CoroutineDispatcher
): FlowUseCase<String, List<Comment>>(ioDispatcher) {
    val userID = runBlocking {
        preferencesStorage.userID.first()
    }

    override fun execute(parameters: String): Flow<Result<List<Comment>>> = flow {
        emit(Result.Loading)
        emit(Result.Success(communityRepository.getComments(boardID = parameters).map {
            it.copy(isMyComment = it.userID.toString() == userID)
        }))
    }
}