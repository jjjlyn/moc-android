package app.moc.shared.data.api

import app.moc.shared.data.api.response.CommunityResponse
import app.moc.shared.data.api.response.PlanResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityService {
    @GET("community/latest")
    suspend fun fetchLatestCommunities(
        @Query("from") from: Int,
        @Query("count") count: Int,
        @Query("category") category: Int
    ): List<CommunityResponse>

    @GET("community/popular")
    suspend fun fetchPopularCommunities(
        @Query("from") from: Int,
        @Query("count") count: Int,
        @Query("category") category: Int
    ): List<CommunityResponse>
}