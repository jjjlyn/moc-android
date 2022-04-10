package app.moc.shared.domain.community

import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCommunityItemUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<String, Unit>(ioDispatcher) {
    override fun execute(parameters: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        emit(Result.Success(communityRepository.deleteCommunityItem(parameters)))
    }
}