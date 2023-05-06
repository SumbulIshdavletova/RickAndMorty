package ru.sumbul.rickandmorty.episodes.domain

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeFilterEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeRemoteKeyEntity
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeFilterDao
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeRemoteKeyDao
import ru.sumbul.rickandmorty.episodes.data.mapper.EpisodeMapper
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.domain.model.Location
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator @Inject constructor(
    private val episodeDb: EpisodeDb,
    private val episodeApi: EpisodeApi,
    private val episodeMapper: EpisodeMapper,
    private val remoteKeyDao: EpisodeRemoteKeyDao,
    private val filterDao: EpisodeFilterDao,
) : RemoteMediator<Int, EpisodeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeEntity>
    ): MediatorResult {

        return try {

            val name: String? = filterDao.getName()
            val episode: String? = filterDao.getEpisode()

            val result = when (loadType) {
                LoadType.REFRESH -> episodeApi.getEpisodes(1, name, episode)
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val page = remoteKeyDao.getNextPage() ?: return MediatorResult.Success(false)
                    episodeApi.getEpisodes(page, name, episode)
                }
            }

            if (!result.isSuccessful) {
                MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            val body = result.body()
            if (body == null) {
                MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            if (body != null) {
                if (body.results.isEmpty()) {
                    emptyList<Episode>()
                    episodeDb.episodeDao().clearAll()
                }
            }

            var nextPage: Any? = null
            if (body != null) {
                if (body.info.next == null) {
                    nextPage = body.info.pages
                } else {
                    nextPage = body.info.next
                }
            }
            val uri = Uri.parse(nextPage.toString())
            val nextPageQuery = uri?.getQueryParameter("page")
            val nextPageNumber = nextPageQuery?.toInt()

            val episodes = body?.results ?: emptyList()
            val responseData = mutableListOf<Episode>()
            responseData.addAll(episodes)

            episodeDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    episodeDb.episodeDao().clearAll()
                    episodeDb.filterDao().clear()
                    episodeDb.remoteKeyDao().clear()
                }
                episodeDb.remoteKeyDao().insert(EpisodeRemoteKeyEntity("query", nextPageNumber))
                episodeDb.filterDao().upsert(EpisodeFilterEntity(1, name, episode))
                episodeDb.episodeDao().upsertAll(episodeMapper.mapToEntity(responseData))
            }
            MediatorResult.Success(
                endOfPaginationReached = responseData.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }


}