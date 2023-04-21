package ru.sumbul.rickandmorty.episodes

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
import ru.sumbul.rickandmorty.characters.episode
import ru.sumbul.rickandmorty.episodes.api.EpisodeApi
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class EpisodeViewModel @Inject constructor(
    pager: Pager<Int, EpisodeEntity>,
    private val api: EpisodeApi,
    val dao: EpisodeDao
) : ViewModel() {

    val episodePagingFlow = pager
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

    fun getById(id: Int): Episode {
        viewModelScope.launch {
            try {
                _dataState.value = ListModelState(loading = true)
                val response = api.getEpisodeById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.upsert(body)
                _dataState.value = ListModelState()
                episode = body.toDto()
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                _dataState.value = ListModelState(error = true)
            }

        }
        return episode
    }


}