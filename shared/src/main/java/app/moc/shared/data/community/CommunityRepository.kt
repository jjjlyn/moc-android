package app.moc.shared.data.community

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.moc.model.*
import app.moc.shared.data.api.CommunityService
import app.moc.shared.data.api.request.toRequest
import app.moc.shared.data.api.response.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CommunityRepository {
    suspend fun getLatestCommunities(category: Int = -1): Flow<PagingData<Community>>
    suspend fun getPopularCommunities()
    suspend fun getSearchResults(keyword: List<String>): List<Community>
    suspend fun uploadCommunityItem(upload: CommunityItemUpload): Community
    suspend fun modifyCommunityItem(modify: CommunityItemModify) : Community
    suspend fun deleteCommunityItem(boardID: String)
    suspend fun getComments(boardID: String): List<Comment>
    suspend fun uploadComment(upload: CommentUpload): Comment
    suspend fun modifyComment(modify: CommentModify): Comment
    suspend fun deleteComment(boardID: String, commentID: String)
}

class DefaultCommunityRepository(
    private val service: CommunityService
): CommunityRepository {
    override suspend fun getLatestCommunities(category: Int): Flow<PagingData<Community>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LatestCommunityPagingSource(service, category) }
        ).flow
    }

    override suspend fun getPopularCommunities() {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(keyword: List<String>): List<Community> {
        return runCatching { service.fetchSearchResults(keyword) }
            .getOrThrow()
            .map { it.toModel() }
    }

    override suspend fun uploadCommunityItem(upload: CommunityItemUpload): Community {
        return runCatching { service.uploadCommunityItem(upload.toRequest()) }
            .getOrThrow()
            .toModel()
    }

    override suspend fun modifyCommunityItem(modify: CommunityItemModify): Community {
        return runCatching { service.modifyCommunityItem(modify.toRequest()) }
            .getOrThrow()
            .toModel()
    }

    override suspend fun deleteCommunityItem(boardID: String) {
        return kotlin.runCatching { service.deleteCommunityItem(boardID) }.getOrThrow()
    }

    override suspend fun getComments(boardID: String): List<Comment> {
        return runCatching { service.fetchComments(boardID) }
            .getOrThrow()
            .map { it.toModel() }
    }

    override suspend fun uploadComment(upload: CommentUpload): Comment {
        return runCatching { service.uploadComment(upload.toRequest()) }.getOrThrow().toModel()
    }

    override suspend fun modifyComment(modify: CommentModify): Comment {
        return runCatching { service.modifyComment(modify.toRequest()) }.getOrThrow().toModel()
    }

    override suspend fun deleteComment(boardID: String, commentID: String) {
        return runCatching { service.deleteComment(commentID, boardID) }.getOrThrow()
    }
}