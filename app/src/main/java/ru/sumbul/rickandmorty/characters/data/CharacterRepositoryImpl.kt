package ru.sumbul.rickandmorty.characters.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.rickandmorty.characters.data.entity.FilterEntity
import ru.sumbul.rickandmorty.characters.domain.FilteredRemoteMediator
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilteredCharactersDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.data.mapper.FilterChMapper
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.characters.domain.SecondRemoteMediator
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.data.mapper.EpisodeMapper
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationDao
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper
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
    private val mapper: CharacterMapper,
    private val episodeMapper: EpisodeMapper,
    private val locationMapper: LocationMapper,
    private val filteredCharactersDao: FilteredCharactersDao,
    private val filterChMapper: FilterChMapper,
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

    @OptIn(ExperimentalPagingApi::class)
    override fun getFilteredCharacters(): Flow<PagingData<ru.sumbul.rickandmorty.characters.domain.model.Character>> {
        val characters: Flow<PagingData<ru.sumbul.rickandmorty.characters.domain.model.Character>> =
            Pager(
                config = PagingConfig(pageSize = 90),
                remoteMediator = SecondRemoteMediator(
                    appDb,
                    api,
                    filterDao,
                    remoteKeyDao,
                    mapper,
                    filteredCharactersDao,
                    filterChMapper
                ),
                pagingSourceFactory = filteredCharactersDao::getPagingSource,
            ).flow.map { pagingData ->
                pagingData.map {
                    filterChMapper.mapToDb(it)
                }
            }
        return characters
    }

    //change filter status etc
    override suspend fun filterCharacters(
        name: String, status: String?, species: String?,
        type: String?, gender: String?
    ) {
        val body: FilterEntity = FilterEntity(1, name, status, species, type, gender)
        try {
            filterDao.upsert(body)
        } catch (e: Exception) {
            throw e
        }
    }


    override fun getEpisodes(ids: List<Int>): Observable<List<Episode>> {
        return api.getEpisodes(ids.toString())
            .map { response ->
                return@map response
            }.subscribeOn(Schedulers.io())
            .onErrorReturn {
                val list1: List<EpisodeEntity> = episodeDao.getEpisodesByIds(ids)
                val list: List<Episode> = episodeMapper.mapFromEntity(list1)
                return@onErrorReturn list
            }
    }

    override fun getLocationById(id: Int): Single<Location> {
        return api.getLocationById(id)
            .map { response ->
                return@map response
            }.subscribeOn(Schedulers.io())
            .onErrorReturn {
                val result: Location = locationMapper.mapFromEntity(locationDao.getLocationById(id))
                return@onErrorReturn result
            }
    }

    override fun getEpisodeById(id: Int): Single<Episode> {
        return api.getEpisodeById(id)
            .map { response ->
                return@map response
            }.subscribeOn(Schedulers.io())
            .doOnError {
                episodeDao.getEpisodeById(id)
            }
    }


}