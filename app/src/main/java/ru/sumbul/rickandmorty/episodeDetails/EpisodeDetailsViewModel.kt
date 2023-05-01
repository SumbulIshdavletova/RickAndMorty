package ru.sumbul.rickandmorty.episodeDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.entity.toDto
import ru.sumbul.rickandmorty.characters.entity.toEntity
import ru.sumbul.rickandmorty.episodes.api.EpisodeApi
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import java.io.IOException
import javax.inject.Inject


@ExperimentalCoroutinesApi
class EpisodeDetailsViewModel @Inject constructor(
    private val api: EpisodeApi,
    private val dao: EpisodeDao
) : ViewModel() {

    private var data: MutableLiveData<List<ru.sumbul.rickandmorty.characters.entity.Character>?>? =
        MutableLiveData<List<ru.sumbul.rickandmorty.characters.entity.Character>?>()

    fun getData(): MutableLiveData<List<ru.sumbul.rickandmorty.characters.entity.Character>?>? {
        return data
    }

    var ids: MutableList<Int> = mutableListOf()

    fun getCharacters(urls: List<String>): Any = viewModelScope.launch {
        ids.removeAll(ids)
        for (url in urls) {
            var result: String = url.substringAfterLast("/", "0")

            ids.add(result.toInt())

        }
        val check = ids.toString()
        try {

            val response = api.getCharacters(ids.toString())
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            data?.value = null
            val body =
                response.body()?.toEntity() ?: throw ApiError(response.code(), response.message())

            data?.value = body.toDto()

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }

}