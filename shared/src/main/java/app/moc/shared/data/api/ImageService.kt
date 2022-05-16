package app.moc.shared.data.api

import app.moc.shared.data.api.response.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageService {
    @GET("image")
    suspend fun fetchImages(@Query("tag") tag: String): List<ImageResponse>
}