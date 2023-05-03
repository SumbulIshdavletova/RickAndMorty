package ru.sumbul.rickandmorty.episodes.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.episodes.data.mapper.EpisodeMapper
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import java.io.IOException
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator @Inject constructor(
    private val episodeDb: EpisodeDb,
    private val episodeApi: EpisodeApi,
    private val episodeMapper: EpisodeMapper
) : RemoteMediator<Int, EpisodeEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val resultBody = episodeApi.getEpisodes(
                page = loadKey
            )

            val info = resultBody.body()?.info
            val episodes = resultBody.body()?.results ?: emptyList()
            val responseData = mutableListOf<Episode>()
            responseData.addAll(episodes)

            episodeDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    episodeDb.episodeDao().clearAll()
                }
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