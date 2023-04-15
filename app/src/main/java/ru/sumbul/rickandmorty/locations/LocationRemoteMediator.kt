package ru.sumbul.rickandmorty.locations

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.locations.api.LocationApi
import ru.sumbul.rickandmorty.locations.db.LocationDb
import ru.sumbul.rickandmorty.locations.entity.Location
import ru.sumbul.rickandmorty.locations.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.entity.toEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class LocationRemoteMediator(
    private val locationDb: LocationDb,
    private val locationApi: LocationApi
) : RemoteMediator<Int, LocationEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationEntity>
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
            val resultBody = locationApi.getLocations(
                page = loadKey
            )

            val info = resultBody.body()?.info
            val location = resultBody.body()?.results ?: emptyList()
            val responseData = mutableListOf<Location>()
            responseData.addAll(location)

            locationDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    locationDb.locationDao().clearAll()
                }
                locationDb.locationDao().upsertAll(responseData.toEntity())
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