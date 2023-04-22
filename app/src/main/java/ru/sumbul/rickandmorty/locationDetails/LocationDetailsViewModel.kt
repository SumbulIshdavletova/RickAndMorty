package ru.sumbul.rickandmorty.locationDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.entity.Character
import ru.sumbul.rickandmorty.characters.entity.toDto
import ru.sumbul.rickandmorty.characters.entity.toEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.api.LocationApi
import ru.sumbul.rickandmorty.locations.dao.LocationDao
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LocationDetailsViewModel @Inject constructor(
    private val api: LocationApi,
    private val dao: LocationDao
) : ViewModel() {

    private var data: MutableLiveData<List<Character>?>? =
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