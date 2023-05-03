package ru.sumbul.rickandmorty.locations.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.domain.LocationRepository
import ru.sumbul.rickandmorty.locations.domain.model.Location
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository,
) : ViewModel() {

    private val cached = repository.locationPagingFlow.cachedIn(viewModelScope)

    val locationPagingFlow: Flow<PagingData<Location>> = cached

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

//    init {
//        loadPosts()
//    }
//
//    fun loadPosts() = viewModelScope.launch {
//        try {
//            _dataState.value = ListModelState(loading = true)
//            //     repository.getAll()
//            _dataState.value = ListModelState()
//        } catch (e: Exception) {
//            _dataState.value = ListModelState(error = true)
//        }
//    }
//
//    fun getById(id: Int): Location {
//        viewModelScope.launch {
//            try {
//                _dataState.value = ListModelState(loading = true)
//                val response = api.getLocationById(id)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//                val body = response.body() ?: throw ApiError(response.code(), response.message())
//                dao.upsert(body)
//                _dataState.value = ListModelState()
//                location = body.toDto()
//            } catch (e: IOException) {
//                throw NetworkError
//            } catch (e: Exception) {
//                _dataState.value = ListModelState(error = true)
//            }
//        }
//        return location
//    }
}