package app.moc.shared.data.image

import app.moc.model.Image
import app.moc.shared.data.api.ImageService
import app.moc.shared.data.api.response.toModel
import javax.inject.Inject

interface ImageRepository {
    suspend fun getImages(tag: String): List<Image>
}

class DefaultImageRepository @Inject constructor(
    private val service: ImageService
): ImageRepository {
    override suspend fun getImages(tag: String): List<Image> {
        return runCatching {
            service.fetchImages(tag)
        }.getOrThrow().map { it.toModel() }
    }
}