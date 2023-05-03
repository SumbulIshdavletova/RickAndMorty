package ru.sumbul.rickandmorty.episodes.ui.details

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRepository
import ru.sumbul.rickandmorty.model.ListModelState
import javax.inject.Inject


@ExperimentalCoroutinesApi
class EpisodeDetailsViewModel @Inject constructor(
    private val repository: EpisodeRepository
) : ViewModel() {

    private val characters: LiveData<List<Character>> = repository.charactersData

    fun getCharacters(): LiveData<List<Character>> {
        return characters
    }

    var ids: MutableList<Int> = mutableListOf()

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    fun getCharacters(urls: List<String>) {
        _dataState.value = ListModelState(loading = true)
        viewModelScope.launch {
            ids.removeAll(ids)
            for (url in urls) {
                var result: String = url.substringAfterLast("/", "0")
                ids.add(result.toInt())
            }
            repository.getCharactersForEpisode(ids.toString())
        }
    }

}