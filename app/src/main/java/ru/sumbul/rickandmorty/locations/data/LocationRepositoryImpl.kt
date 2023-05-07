package ru.sumbul.rickandmorty.locations.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.data.entity.LocationFilterEntity
import ru.sumbul.rickandmorty.locations.data.local.LocationDb
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationFilterDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationRemoteKeyDao
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper
import ru.sumbul.rickandmorty.locations.data.remote.LocationApi
import ru.sumbul.rickandmorty.locations.domain.LocationRemoteMediator
import ru.sumbul.rickandmorty.locations.domain.LocationRepository
import ru.sumbul.rickandmorty.locations.domain.model.Location
import java.io.IOException
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val api: LocationApi,
    private val dao: LocationDao,
    private val db: LocationDb,
    private val mapper: LocationMapper,
    private val characterMapper: CharacterMapper,
    private val remoteKeyDao: LocationRemoteKeyDao,
    private val filterDao: LocationFilterDao

) : LocationRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val locationPagingFlow: Flow<PagingData<Location>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationRemoteMediator(db, api, filterDao, remoteKeyDao, mapper),
            pagingSourceFactory = dao::pagingSource,
        ).flow.map { pg ->
            pg.map {
                mapper.mapFromEntity(it)
            }
        }

    override suspend fun filterLocation(name: String?, type: String?, dimension: String?) {
        val body = LocationFilterEntity(1, name, type, dimension)
        try {
            filterDao.upsert(body)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getById(id: Int): Single<Location> {
        return api.getLocationById(id)
            .map { response ->
                return@map response
            }.subscribeOn(Schedulers.io())
    }

    override fun getCharacters(ids: String) : Observable<List<Character>> {
        return api.getCharacters(ids)
            .map { response ->
                return@map response
            }.subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
    }


}