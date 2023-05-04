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
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRepository
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.data.remote.LocationApi
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper
import ru.sumbul.rickandmorty.locations.domain.LocationRepository
import ru.sumbul.rickandmorty.locations.domain.model.Location
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject


@ExperimentalCoroutinesApi
class LocationDetailsViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private var data: LiveData<List<Character>?>? = repository.getData()

    fun getData(): LiveData<List<Character>?>? {
        return data
    }

    var ids: MutableList<Int> = mutableListOf()

    fun getCharacters(urls: List<String>) {
        ids.removeAll(ids)
        for (url in urls) {
            var result: String = url.substringAfterLast("/", "0")
            ids.add(result.toInt())
        }
        viewModelScope.launch {
            val check = ids.toString()
            repository.getCharacters(ids.toString())
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

    fun getLocationById(url: String) {
        var result: String = url.substringAfterLast("/", "0")
        var id: Int = result.toInt()
        viewModelScope.launch {
            loc.value = repository.getById(id)
        }
    }

}