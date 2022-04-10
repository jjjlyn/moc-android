package app.moc.shared.domain.community

import app.moc.model.Community
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
): FlowUseCase<List<String>, List<Community>>(ioDispatcher) {
    override fun execute(parameters: List<String>): Flow<Result<List<Community>>> = flow {
        emit(Result.Loading)
        emit(Result.Success(communityRepository.getSearchResults(parameters)))
    }
}