package ru.sumbul.rickandmorty.locations.domain

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.data.entity.LocationFilterEntity
import ru.sumbul.rickandmorty.locations.data.entity.LocationRemoteKeyEntity
import ru.sumbul.rickandmorty.locations.data.remote.LocationApi
import ru.sumbul.rickandmorty.locations.data.local.LocationDb
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationFilterDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationRemoteKeyDao
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper
import ru.sumbul.rickandmorty.locations.domain.model.Location
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class LocationRemoteMediator @Inject constructor(
    private val locationDb: LocationDb,
    private val locationApi: LocationApi,
    private val filterDao: LocationFilterDao,
    private val remoteKeyDao: LocationRemoteKeyDao,
    private val locationMapper: LocationMapper,
) : RemoteMediator<Int, LocationEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationEntity>
    ): MediatorResult {

        return try {

            val name: String? = filterDao.getName()
            val episode: String? = filterDao.getEpisode()

            val result = when (loadType) {
                LoadType.REFRESH -> {
                    locationApi.getLocations(1, name, episode)
                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val page = remoteKeyDao.getNextPage() ?: return MediatorResult.Success(false)
                    locationApi.getLocations(page, name, episode)
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
                    emptyList<Location>()
                    locationDb.locationDao().clearAll()
                    // все стираем чтобы получить надпись что нет данных
                    //TODO MAKE IT ЧТОБЫ ОНО НЕ УДАЛЯЛОСЬ ПРИ ОТСУТСВИЕ ОТВЕТА
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


            val location = body?.results ?: emptyList()
            val responseData = mutableListOf<Location>()
            responseData.addAll(location)

            //locationDb.filterDao().clear()

            locationDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    locationDb.locationDao().clearAll()
                    locationDb.filterDao().clear()
                    locationDb.remoteKeyDao().clear()
                }

                locationDb.remoteKeyDao()
                    .insert(LocationRemoteKeyEntity("query", nextPageNumber))
                //   locationDb.filterDao().clear()
                locationDb.filterDao().upsert(LocationFilterEntity(1, name, episode))
                locationDb.locationDao().upsertAll(locationMapper.mapToListEntity(responseData))
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