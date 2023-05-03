package ru.sumbul.rickandmorty.episodes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.episodes.data.mapper.EpisodeMapper
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRemoteMediator
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRepository
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import java.io.IOException
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    pager: Pager<Int, EpisodeEntity>,
    private val api: EpisodeApi,
    private val dao: EpisodeDao,
    private val mapper: EpisodeMapper,
    private val db: EpisodeDb,
    private val characterDao: CharacterDao,
    private val characterMapper: CharacterMapper,
) : EpisodeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val episodePagingFlow: Flow<PagingData<Episode>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = EpisodeRemoteMediator(db, api, mapper),
            pagingSourceFactory = dao::pagingSource,
        ).flow.map { pg ->
            pg.map {
                mapper.mapToDb(it)
            }
        }

    override suspend fun getById(id: Int): Episode {
        var episode: Episode
        try {
            val response = api.getEpisodeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.upsert(body)
            episode = mapper.mapToDb(body)
        } catch (e: Exception) {
            episode = mapper.mapToDb(dao.getEpisodeById(id))
        }
        return episode
    }


   // override val charactersData: LiveData<List<Character>> = liveData { data }

    private var data: MutableLiveData<List<Character>?>? =
        MutableLiveData<List<Character>?>()

    override fun getData(): MutableLiveData<List<Character>?>? {
        return data
    }

    override suspend fun getCharactersForEpisode(ids: String) {
        try {
            val response = api.getCharacters(ids)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            data?.value = null
            val body =
                response.body() ?: throw ApiError(response.code(), response.message())
            characterDao.upsertAll(characterMapper.mapToEntity(body))
            data?.value = body
        } catch (e: IOException) {
            throw NetworkError
            //сделать вызов из БД по id список персонажей
        } catch (e: Exception) {
            throw NetworkError
        }
    }


}