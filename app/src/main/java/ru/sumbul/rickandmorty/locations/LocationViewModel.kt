package ru.sumbul.rickandmorty.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.entity.Origin
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.locations.api.LocationApi
import ru.sumbul.rickandmorty.locations.dao.LocationDao
import ru.sumbul.rickandmorty.locations.entity.Location
import ru.sumbul.rickandmorty.locations.entity.LocationEntity
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject

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
class LocationViewModel @Inject constructor(
    pager: Pager<Int, LocationEntity>,
    private val api: LocationApi,
    val dao: LocationDao
) : ViewModel() {

    val locationPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.toDto() }
        }
        .cachedIn(viewModelScope)

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = ListModelState(loading = true)
            //     repository.getAll()
            _dataState.value = ListModelState()
        } catch (e: Exception) {
            _dataState.value = ListModelState(error = true)
        }
    }

    fun getById(id: Int): Location {
        viewModelScope.launch {
            try {
                _dataState.value = ListModelState(loading = true)
                val response = api.getLocationById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.upsert(body)
                _dataState.value = ListModelState()
                location = body.toDto()
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                _dataState.value = ListModelState(error = true)
            }
        }
        return location
    }
}