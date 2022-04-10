package app.moc.shared.domain.community

import app.moc.model.Community
import app.moc.model.CommunityItemUpload
import app.moc.shared.data.community.CommunityRepository
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UploadCommunityItemUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
    @IODispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<CommunityItemUpload, Community>(ioDispatcher) {
    override fun execute(parameters: CommunityItemUpload): Flow<Result<Community>> = flow {
        emit(Result.Loading)
        emit(Result.Success(communityRepository.uploadCommunityItem(parameters)))
    }
}