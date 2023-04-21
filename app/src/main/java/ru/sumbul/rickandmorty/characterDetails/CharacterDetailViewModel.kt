package ru.sumbul.rickandmorty.characterDetails

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.api.CharacterApi
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.entity.toDto
import ru.sumbul.rickandmorty.episodes.entity.toEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import java.io.IOException
import javax.inject.Inject


var episode: Episode = Episode(id = 0, name = "", air_date = "", "", emptyList(), "", "")


@HiltViewModel
@ExperimentalCoroutinesApi
class CharacterDetailViewModel @Inject constructor(
    private val api: CharacterApi,
    private val dao: EpisodeDao,
    private val repository: CharacterDetailRepository,
) : ViewModel() {

//    val data: LiveData<List<Episode>> = repository.data
//
//    fun loadPosts(urls: List<String>) = viewModelScope.launch {
//        try {
//            repository.getEpisodes(urls)
//        } catch (e: Exception) {
//            NetworkError
//        }
//    }

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

            data1?.value = body.toDto()

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }

}