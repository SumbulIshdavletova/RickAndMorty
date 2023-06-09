package ru.sumbul.rickandmorty.locations.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.domain.model.Character
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

    fun filterLocations(
        name: String?, type: String?, dimension: String?
    ) = viewModelScope.launch {
        try {
            _dataState.value = ListModelState(refreshing = true)
            repository.filterLocation(name, type, dimension)
            _dataState.value = ListModelState()
        } catch (e: Exception) {
            _dataState.value = ListModelState(error = true)
        }
    }

    fun filterLocationsOffline(
        name: String?, type: String?, dimension: String?
    ): Flow<PagingData<Location>> {
        val locations: Flow<PagingData<Location>> =
            repository.locationPagingFlow.cachedIn(viewModelScope)
        return locations.map { pd ->
            pd.filter { location ->
                (name == null || location.name.contains(
                    name,
                    ignoreCase = true
                )) && (type == null || location.type.contains(
                    type,
                    ignoreCase = true
                )) && (dimension == null || location.dimension.contains(
                    dimension, ignoreCase = true
                ))
            }
        }
    }


}