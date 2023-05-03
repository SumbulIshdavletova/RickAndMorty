package ru.sumbul.rickandmorty.locations.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.data.local.LocationDb
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper
import ru.sumbul.rickandmorty.locations.data.remote.LocationApi
import ru.sumbul.rickandmorty.locations.domain.LocationRemoteMediator
import ru.sumbul.rickandmorty.locations.domain.LocationRepository
import ru.sumbul.rickandmorty.locations.domain.model.Location
import java.io.IOException
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    pager: Pager<Int, LocationEntity>,
    private val api: LocationApi,
    private val dao: LocationDao,
    private val db: LocationDb,
    private val mapper: LocationMapper,
    private val characterMapper: CharacterMapper,

    ) : LocationRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val locationPagingFlow: Flow<PagingData<Location>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationRemoteMediator(db, api, mapper),
            pagingSourceFactory = dao::pagingSource,
        ).flow.map { pg ->
            pg.map {
                mapper.mapFromEntity(it)
            }
        }

    override suspend fun getById(id: Int): Location {
        var location: Location
        try {
            val response = api.getLocationById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.upsert(body)
            location = mapper.mapFromEntity(body)
        } catch (e: IOException) {
            throw NetworkError
        }
        return location
    }

    private var data: MutableLiveData<List<Character>?>? =
        MutableLiveData<List<Character>?>()

    override fun getData(): MutableLiveData<List<Character>?>? {
        return data
    }

   // override val charactersForLocation: LiveData<List<Character>?>? = liveData { data }

    override suspend fun getCharacters(ids: String) {
        try {
            val response = api.getCharacters(ids.toString())
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            data?.value = null

            val body = response.body()?.let { characterMapper.mapToEntity(it) }

            data?.value = body?.let { characterMapper.mapCharactersFromDb(it) }

        } catch (e: IOException) {
            throw NetworkError
        }
    }


}