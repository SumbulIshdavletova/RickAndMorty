package ru.sumbul.rickandmorty.characters.ui.list

import androidx.lifecycle.*
import androidx.paging.*

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.characters.domain.model.Location
import ru.sumbul.rickandmorty.characters.domain.model.Origin
import ru.sumbul.rickandmorty.model.ListModelState
import javax.inject.Inject

//var character: Character = Character(
//    id = 0,
//    name = "",
//    status = "",
//    species = "",
//    type = "",
//    gender = "",
//    origin = Origin(
//        name = "",
//        url = ""
//    ),
//    location = Location(
//        name = "",
//        url = ""
//    ),
//    image = "",
//    episode = emptyList(),
//    url = "",
//    created = "",
//)


@ExperimentalCoroutinesApi
class CharacterViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val cached = repository
        .characterPagingFlow
        .cachedIn(viewModelScope)

    val characterPagingFlow: Flow<PagingData<Character>> =
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


}