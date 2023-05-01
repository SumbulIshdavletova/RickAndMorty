package ru.sumbul.rickandmorty.locations.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.data.remote.LocationApi
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.domain.model.Location
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject


@ExperimentalCoroutinesApi
class LocationDetailsViewModel @Inject constructor(
    private val api: LocationApi,
    private val dao: LocationDao,
    private val mapper: CharacterMapper,
) : ViewModel() {

    private var data: MutableLiveData<List<Character>?>? =
        MutableLiveData<List<Character>?>()

    fun getData(): MutableLiveData<List<Character>?>? {
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

            val body = response.body()?.let { mapper.mapCharactersToDb(it) }

            data?.value = body?.let { mapper.mapCharactersFromDb(it) }

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
          val location = body.toDto()
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            _dataState1.value = ListModelState(error = true)
        }

    }

}