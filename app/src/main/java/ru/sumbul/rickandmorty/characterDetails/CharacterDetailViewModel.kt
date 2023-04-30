package ru.sumbul.rickandmorty.characterDetails

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.episodes.api.EpisodeApi
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.entity.toDto
import ru.sumbul.rickandmorty.episodes.entity.toEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.dao.LocationDao
import ru.sumbul.rickandmorty.locations.entity.Location
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject


var episode: Episode = Episode(id = 0, name = "", air_date = "", "", emptyList(), "", "")

var location: Location = Location(
    id = 0,
    name = "",
    type = "",
    dimension = "",
    residents = emptyList(),
    url = "",
    created = "",
)


@HiltViewModel
@ExperimentalCoroutinesApi
class CharacterDetailViewModel @Inject constructor(
    private val api: CharacterApi,
    private val locationDao: LocationDao,
    private val dao: EpisodeDao,
    private val characterDao: CharacterDao,
    private val repository: CharacterDetailRepository,
    private val episodeApi: EpisodeApi,
) : ViewModel() {


    private var data1: MutableLiveData<List<Episode>?>? =
        MutableLiveData<List<Episode>?>()

    fun getData1(): MutableLiveData<List<Episode>?>? {
        return data1
    }

    var ids: MutableList<Int> = mutableListOf()

    override fun onCleared() {
        data1?.value = null
    }

    fun getEpisodes(urls: MutableList<String>): Any = viewModelScope.launch {
        ids.removeAll(ids)
        for (url in urls) {
            var result: String = url.substringAfterLast("/", "0")

            ids.add(result.toInt())

        }
        val check = ids.toString()
        try {

            val response = api.getEpisodes(ids.toString())
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            data1?.value = null
            val body =
                response.body()?.toEntity() ?: throw ApiError(response.code(), response.message())
            dao.upsertAll(body)
            data1?.value = body.toDto()

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getEpisodesFromDb(urls: MutableList<String>): Any = viewModelScope.launch {
        ids.removeAll(ids)
        for (url in urls) {
            var result: String = url.substringAfterLast("/", "0")

            ids.add(result.toInt())

        }

        val check = ids.toString()
        try {
            var filteredEp: MutableList<EpisodeEntity>?
//            for (i in ids) {
//                val ep = dao.getAll().value?.filter { episode ->
//                    episode.id == i
//                }
//                filteredEp.replaceAll(ep.toList())
//            }

            //    data1?.value = filteredEp.toDto()

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }


    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    fun getEpisodeById(id: Int): Episode {
        viewModelScope.launch {
            try {
                _dataState.value = ListModelState(loading = true)
                val response = episodeApi.getEpisodeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.upsert(body)
                _dataState.value = ListModelState()
                ru.sumbul.rickandmorty.episodes.episode = body.toDto()
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                _dataState.value = ListModelState(error = true)
            }

        }
        return episode
    }

    private val _loc = MutableLiveData<Location>()
    val loc: LiveData<Location>
        get() = _loc

    private val _dataState1 = MutableLiveData<ListModelState>()
    val dataState1: LiveData<ListModelState>
        get() = _dataState1

    fun getLocationById(url: String) {
        var result: String = url.substringAfterLast("/", "0")
        val id = result.toInt()
        viewModelScope.launch {
            try {
                _dataState1.value = ListModelState(loading = true)
                val response = api.getLocationById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                locationDao.upsert(body)
                _loc.value = Location(
                    body.id,
                    body.name,
                    body.type,
                    body.dimension,
                    body.residents,
                    body.url,
                    body.created
                )
                _dataState1.value = ListModelState()
                ru.sumbul.rickandmorty.characterDetails.location = body.toDto()
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                _dataState1.value = ListModelState(error = true)
            }

        }
    }

}