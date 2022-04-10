package app.moc.shared.data.api

import app.moc.shared.data.api.request.CommentModifyRequest
import app.moc.shared.data.api.request.CommentUploadRequest
import app.moc.shared.data.api.request.CommunityItemModifyRequest
import app.moc.shared.data.api.request.CommunityItemUploadRequest
import app.moc.shared.data.api.response.CommentResponse
import app.moc.shared.data.api.response.CommunityResponse
import app.moc.shared.data.api.response.PlanResponse
import retrofit2.http.*

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

    @GET("community/search")
    suspend fun fetchSearchResults(@Query("keyword") keywords: List<String>): List<CommunityResponse>

    @POST("community")
    suspend fun uploadCommunityItem(@Body communityItemUploadRequest: CommunityItemUploadRequest): CommunityResponse

    @DELETE("community")
    suspend fun deleteCommunityItem(@Query("board_id") boardID: String)

    @PUT("community")
    suspend fun modifyCommunityItem(@Body communityItemDeleteRequest: CommunityItemModifyRequest): CommunityResponse

    @GET("community/comment")
    suspend fun fetchComments(@Query("board_id") boardID: String): List<CommentResponse>

    @POST("community/comment")
    suspend fun uploadComment(@Body commentUploadRequest: CommentUploadRequest): CommentResponse

    @PUT("community/comment")
    suspend fun modifyComment(@Body commentModifyRequest: CommentModifyRequest): CommentResponse

    @DELETE("community/comment")
    suspend fun deleteComment(@Query("comment_id") commentID: String, @Query("board_id") boardID: String)
}