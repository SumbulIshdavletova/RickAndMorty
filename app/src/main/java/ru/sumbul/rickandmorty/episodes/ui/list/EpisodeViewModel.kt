package ru.sumbul.rickandmorty.episodes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRepository
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.model.ListModelState
import javax.inject.Inject

//var episode: Episode = Episode(id = 0, name = "", air_date = "", "", emptyList(), "", "")

@ExperimentalCoroutinesApi
class EpisodeViewModel @Inject constructor(
    private val repository: EpisodeRepository,
) : ViewModel() {

    private val cached = repository.episodePagingFlow.cachedIn(viewModelScope)

    val episodePagingFlow: Flow<PagingData<Episode>> = cached

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    fun filterEpisodes(
        name: String?,
        episode: String?,
    ) = viewModelScope.launch {
        try {
            _dataState.value = ListModelState(refreshing = true)
            repository.filterEpisodes(name, episode)
            _dataState.value = ListModelState()
        } catch (e: Exception) {
            _dataState.value = ListModelState(error = true)
        }
    }

    fun filterEpisodesOffline(
        name: String?,
        episode: String?,
    ): Flow<PagingData<Episode>> {
        val episodesCached: Flow<PagingData<Episode>> =
            repository.episodePagingFlow.cachedIn(viewModelScope)
        return episodesCached.map { pd ->
            pd.filter { episodeCached ->
                (name == null || episodeCached.name.contains(name, ignoreCase = true)) &&
                        (episode == null || episodeCached.episode.contains(
                            episode,
                            ignoreCase = true
                        ))
            }
        }
    }

}
