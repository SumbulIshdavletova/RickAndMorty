package ru.sumbul.rickandmorty.characters.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.rickandmorty.characters.data.entity.FilterEntity
import ru.sumbul.rickandmorty.characters.domain.FilteredRemoteMediator
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.entity.toDto
import ru.sumbul.rickandmorty.episodes.entity.toEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.domain.model.Location
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val dao: CharacterDao,
    private val api: CharacterApi,
    private val filterDao: FilterDao,
    private val appDb: CharacterDb,
    private val remoteKeyDao: RemoteKeyDao,
    private val locationDao: LocationDao,
    private val episodeDao: EpisodeDao,
    private val episodeApi: EpisodeApi,
    private val mapper: CharacterMapper
) : CharacterRepository {


    @OptIn(ExperimentalPagingApi::class)
    override val characterPagingFlow: Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = FilteredRemoteMediator(appDb, api, filterDao, remoteKeyDao, mapper),
            pagingSourceFactory = dao::getPagingSource,
        ).flow.map { pagingData ->
            pagingData.map {
                mapper.mapToDb(it)
            }
        }

    //change filter status etc
    override suspend fun filterCharacters(name: String, status: String, gender: String) {
        val body: FilterEntity = FilterEntity(name, status, gender)
        try {
            filterDao.upsert(body)
        } catch (e: Exception) {
            throw e
        }
    }


    override val data: LiveData<List<Episode>> = episodeDao.getAll().map(List<EpisodeEntity>::toDto)

    private var data1: MutableLiveData<List<Episode>?>? =
        MutableLiveData<List<Episode>?>()

    fun getData1(): MutableLiveData<List<Episode>?>? {
        return data1
    }
    //  var ids: MutableList<Int> = mutableListOf()

    //    suspend fun getEpisodes(urls: MutableList<String>): Any {
//
//    }
    override suspend fun getEpisodes(ids: String) {
//        ids.removeAll(ids)
//        for (url in urls) {
//            var result: String = url.substringAfterLast("/", "0")
//
//            ids.add(result.toInt())
//
//        }
        try {
            val response = api.getEpisodes(ids.toString())
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            data1?.value = null
            val body =
                response.body()?.toEntity() ?: throw ApiError(response.code(), response.message())
            episodeDao.upsertAll(body)
            data1?.value = body.toDto()

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }


    override suspend fun getEpisodeById(id: Int): Episode {
        var episode: Episode
        try {
            val response = episodeApi.getEpisodeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            episodeDao.upsert(body)
            episode = body.toDto()
        } catch (e: Exception) {
            episode = episodeDao.getEpisodeById(id).toDto()
        }
        return episode
    }


    override suspend fun getLocationById(url: String): Location {
        var location: Location
        var result: String = url.substringAfterLast("/", "0")
        val id = result.toInt()
        try {
            val response = api.getLocationById(id)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            locationDao.upsert(body)
            location = body.toDto()
        } catch (e: Exception) {
            location = locationDao.getLocationById(id).toDto()
        }
        return location
    }


}