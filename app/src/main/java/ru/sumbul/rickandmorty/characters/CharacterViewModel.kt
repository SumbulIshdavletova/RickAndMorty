package ru.sumbul.rickandmorty.characters

import androidx.lifecycle.*
import androidx.paging.*

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.characters.entity.*
import ru.sumbul.rickandmorty.model.ListModelState
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



@ExperimentalCoroutinesApi
class CharacterViewModel @Inject constructor(
    pager: Pager<Int, CharacterEntity>,
    private val repository: CharacterRepository,
    private val api: CharacterApi,
    private val dao: CharacterDao
) : ViewModel() {

    private val cached = repository
        .characterPagingFlow
        .cachedIn(viewModelScope)

    val characterPagingFlow: Flow<PagingData<ru.sumbul.rickandmorty.characters.entity.Character>> =
        cached

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    fun filterCharacters(name: String, status: String, gender: String) = viewModelScope.launch {
        try {
            _dataState.value = ListModelState(refreshing = true)
            repository.filterCharacters(name, status, gender)
            _dataState.value = ListModelState()
        } catch (e: Exception) {
            _dataState.value = ListModelState(error = true)
        }
    }


//    fun getById(id: Int): Character {
//        viewModelScope.launch {
//            try {
//                _dataState.value = ListModelState(loading = true)
//                val response = api.getCharacterById(id)
//                if (!response.isSuccessful) {
//                    throw ApiError(response.code(), response.message())
//                }
//                val body = response.body() ?: throw ApiError(response.code(), response.message())
//                dao.upsert(body)
//                _dataState.value = ListModelState()
//                character = body.toDto()
//            } catch (e: IOException) {
//                throw NetworkError
//            } catch (e: Exception) {
//                _dataState.value = ListModelState(error = true)
//            }
//
//        }
//        return character
//    }


}