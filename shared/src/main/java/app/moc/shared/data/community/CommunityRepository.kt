package app.moc.shared.data.community

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import app.moc.model.Community
import app.moc.shared.data.api.CommunityService
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    suspend fun getLatestCommunities(category: Int = -1): Flow<PagingData<Community>>
    suspend fun getPopularCommunities()
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

}