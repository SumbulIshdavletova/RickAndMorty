package ru.sumbul.rickandmorty.locationDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.entity.toDto
import ru.sumbul.rickandmorty.characters.entity.toEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.api.LocationApi
import ru.sumbul.rickandmorty.locations.dao.LocationDao
import ru.sumbul.rickandmorty.locations.entity.Location
import ru.sumbul.rickandmorty.locations.location
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LocationDetailsViewModel @Inject constructor(
    private val api: LocationApi,
    private val dao: LocationDao
) : ViewModel() {

    private var data: MutableLiveData<List<ru.sumbul.rickandmorty.characters.domain.model.Character>?>? =
        MutableLiveData<List<ru.sumbul.rickandmorty.characters.domain.model.Character>?>()

    fun getData(): MutableLiveData<List<ru.sumbul.rickandmorty.characters.domain.model.Character>?>? {
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
            delay(5_000L)
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

//    private var _loc = MutableLiveData<Location>()
//    val loc: LiveData<Location>
//        get() = _loc

    private var loc: MutableLiveData<Location> =
        MutableLiveData<Location>()

    fun getLoc(): MutableLiveData<Location> {
        return loc
    }

    private var _dataState1 = MutableLiveData<ListModelState>()
    val dataState1: LiveData<ListModelState>
        get() = _dataState1

    fun getLocationById(url: String): Any = viewModelScope.launch {
        var result: String = url.substringAfterLast("/", "0")
        var id:Int = result.toInt()
        try {
            _dataState1.value = ListModelState(loading = true)
            val response = api.getLocationById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            loc.value = body.toDto()
            dao.upsert(body)
           _dataState1.value = ListModelState()
           ru.sumbul.rickandmorty.characterDetails.location = body.toDto()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            _dataState1.value = ListModelState(error = true)
        }

    }

}