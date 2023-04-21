package ru.sumbul.rickandmorty.characters

import androidx.lifecycle.*
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.room.TypeConverters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.api.CharacterApi
import ru.sumbul.rickandmorty.characters.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.entity.Character
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.entity.Location
import ru.sumbul.rickandmorty.characters.entity.Origin
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.model.ListModelState
import ru.sumbul.rickandmorty.util.StringListTypeConverter
import java.io.IOException
import javax.inject.Inject

var character: Character = Character(
    id = 0,
    name = "",
    status = "",
    species = "",
    type = "",
    gender = "",
    origin = Origin(
        name = "",
        url = ""
    ),
    location = Location(
        name = "",
        url = ""
    ),
    image = "",
    episode = emptyList(),
    url = "",
    created = "",
)

var episode: Episode = Episode(id = 0, name = "", air_date = "", "", emptyList(), "", "")

@HiltViewModel
@ExperimentalCoroutinesApi
class CharacterViewModel @Inject constructor(
    pager: Pager<Int, CharacterEntity>,
    private val api: CharacterApi,
    private val dao: CharacterDao
) : ViewModel() {

    val characterPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.toDto() }
        }
        .cachedIn(viewModelScope)
    //    .asLiveData(Dispatchers.Default)


    val characterPagingLiveData = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.toDto() }
        }
        .cachedIn(viewModelScope)
        .asLiveData(Dispatchers.Default)


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

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = ListModelState(refreshing = true)
            //       repository.getAll()
            _dataState.value = ListModelState()
        } catch (e: Exception) {
            _dataState.value = ListModelState(error = true)
        }
    }

    fun getById(id: Int): Character {
        viewModelScope.launch {
            try {
                _dataState.value = ListModelState(loading = true)
                val response = api.getCharacterById(id)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.upsert(body)
                _dataState.value = ListModelState()
                character = body.toDto()
            } catch (e: IOException) {
                throw NetworkError
            } catch (e: Exception) {
                _dataState.value = ListModelState(error = true)
            }

        }
        return character
    }



}