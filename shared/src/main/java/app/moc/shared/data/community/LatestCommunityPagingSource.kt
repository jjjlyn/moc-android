package app.moc.shared.data.community

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.moc.model.Community
import app.moc.shared.data.api.CommunityService
import app.moc.shared.data.api.response.toModel
import retrofit2.HttpException
import java.io.IOException

class LatestCommunityPagingSource(
    private val service: CommunityService,
    private val category: Int
) : PagingSource<Int, Community>() {
    private var isAlreadyExecutedOnce = false

    companion object {
        private const val PAGING_LIMIT = 10
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Community> {
        val from = params.key
        return if (from == null) {
            if(isAlreadyExecutedOnce){
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = params.key,
                    nextKey = null
                )
            } else {
                isAlreadyExecutedOnce = true

                val response = service.fetchLatestCommunities(-1, PAGING_LIMIT, category)
                val nextKey =
                    if (response.isEmpty()) {
                        null
                    } else {
                        val totalCount = response[0].boardID
                        if (totalCount - PAGING_LIMIT < -1) {
                            null
                        } else {
                            totalCount - PAGING_LIMIT
                        }
                    }
                LoadResult.Page(
                    data = response.map { it.toModel() },
                    prevKey = params.key,
                    nextKey = nextKey
                )
            }
        } else {
            return try {
                val response = service.fetchLatestCommunities(from, PAGING_LIMIT, category)
                val nextKey =
                    if (response.isEmpty()) {
                        null
                    } else {
                        // initial load size = 3 * NETWORK_PAGE_SIZE
                        // ensure we're not requesting duplicating items, at the 2nd request
                        val totalCount = response[0].boardID
                        if (totalCount - PAGING_LIMIT < 0) {
                            null
                        } else {
                            totalCount - PAGING_LIMIT
                        }
                    }
                LoadResult.Page(
                    data = response.map { it.toModel() },
                    prevKey = from,
                    nextKey = nextKey
                )
            } catch (exception: IOException) {
                return LoadResult.Error(exception)
            } catch (exception: HttpException) {
                return LoadResult.Error(exception)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Community>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(10)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(10)
        }
    }
}