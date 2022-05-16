package app.moc.shared.domain.image

import app.moc.model.Image
import app.moc.shared.data.image.ImageRepository
import app.moc.shared.data.prefs.PreferencesStorage
import app.moc.shared.di.IODispatcher
import app.moc.shared.domain.FlowUseCase
import app.moc.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class GetImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<String, List<Image>>(dispatcher) {
    override fun execute(parameters: String): Flow<Result<List<Image>>>  = flow {
        emit(Result.Loading)
        emit(Result.Success(imageRepository.getImages(parameters)))
    }
}