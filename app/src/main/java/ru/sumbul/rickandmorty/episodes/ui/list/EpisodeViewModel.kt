package ru.sumbul.rickandmorty.episodes.ui.list

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRepository
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject

//var episode: Episode = Episode(id = 0, name = "", air_date = "", "", emptyList(), "", "")

@ExperimentalCoroutinesApi
class EpisodeViewModel @Inject constructor(
    private val repository: EpisodeRepository,
) : ViewModel() {

    private val cached = repository.episodePagingFlow.cachedIn(viewModelScope)

    val episodePagingFlow : Flow<PagingData<Episode>> = cached


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
//    fun getById(id: Int): Episode {
//        viewModelScope.launch {
//            try {
//                _dataState.value = ListModelState(loading = true)
//                val response = api.getEpisodeById(id)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//                val body = response.body() ?: throw ApiError(response.code(), response.message())
//                dao.upsert(body)
//                _dataState.value = ListModelState()
//                episode = body.toDto()
//            } catch (e: IOException) {
//                throw NetworkError
//            } catch (e: Exception) {
//                _dataState.value = ListModelState(error = true)
//            }
//
//        }
//        return episode
//    }


}