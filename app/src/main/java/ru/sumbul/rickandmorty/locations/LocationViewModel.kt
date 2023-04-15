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
import ru.sumbul.rickandmorty.locations.entity.LocationEntity
import ru.sumbul.rickandmorty.model.ListModelState
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class LocationViewModel @Inject constructor(
    pager: Pager<Int, LocationEntity>
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
}