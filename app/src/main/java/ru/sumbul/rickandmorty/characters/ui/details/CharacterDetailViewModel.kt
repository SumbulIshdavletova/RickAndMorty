package ru.sumbul.rickandmorty.characters.ui.details

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.data.CharacterRepositoryImpl
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.domain.model.Location
import ru.sumbul.rickandmorty.model.ListModelState
import javax.inject.Inject


var episode: Episode = Episode(id = 0, name = "", air_date = "", "", emptyList(), "", "")

var location: Location = Location(
    id = 0,
    name = "",
    type = "",
    dimension = "",
    residents = emptyList(),
    url = "",
    created = "",
)

@ExperimentalCoroutinesApi
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val episodes: MutableLiveData<List<Episode>?>? = repository.getData1()

    fun getEpisodes(): MutableLiveData<List<Episode>?>? {
        return episodes
    }

    var ids: MutableList<Int> = mutableListOf()

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    fun getEpisodes(urls: MutableList<String>) {
        _dataState.value = ListModelState(loading = true)
        viewModelScope.launch {
            ids.removeAll(ids)
            for (url in urls) {
                var result: String = url.substringAfterLast("/", "0")

                ids.add(result.toInt())
            }
            val check = ids.toString()
            repository.getEpisodes(ids.toString())
        }
    }

    fun getEpisodeById(id: Int): Episode {
        _dataState.value = ListModelState(loading = true)
        viewModelScope.launch {
            try {
                episode = repository.getEpisodeById(id)
                //TODO ПРОВЕРИТЬ БУДЕТ ЛИ ОНО СОХРАНЯТЬСЯ
            } catch (e: Exception) {
                _dataState.value = ListModelState(error = true)
            }
        }
        return episode
    }

    fun getLocationById(url: String) : Location {
        _dataState.value = ListModelState(loading = true)
//        var result: String = url.substringAfterLast("/", "0")
//        val id = result.toInt()
        viewModelScope.launch {
            try {
                location = repository.getLocationById(url)
            } catch (e: Exception) {
                _dataState.value = ListModelState(error = true)
            }

        }
        return location
    }

}