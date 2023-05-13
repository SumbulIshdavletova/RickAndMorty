package ru.sumbul.rickandmorty.characters.ui.list

import androidx.lifecycle.*
import androidx.paging.*

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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


@ExperimentalCoroutinesApi
class CharacterViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : ViewModel() {

    private val cached = repository
        .characterPagingFlow
        .cachedIn(viewModelScope)

    val characterPagingFlow: Flow<PagingData<Character>> = cached

    var cachedCharacterPagingFlow: Flow<PagingData<Character>> = cached

 //val cachedCharacters: Flow<PagingData<Character>> = filterCharactersOffline() // filter by name "rick" and status "alive"


//    private val cached2 = repository
//        .getFilteredCharacters()
//        .cachedIn(viewModelScope)

//  val filterPagingFlow : Flow<PagingData<Character>> = cached2

    private val _dataState = MutableLiveData<ListModelState>()
    val dataState: LiveData<ListModelState>
        get() = _dataState

    fun filterCharacters(
        name: String,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ) = viewModelScope.launch {
        try {
            _dataState.value = ListModelState(refreshing = true)
            repository.filterCharacters(name, status, species, type, gender)
            _dataState.value = ListModelState()
        } catch (e: Exception) {
            _dataState.value = ListModelState(error = true)
        }
    }


    fun filterCharactersOffline(
        name: String?, status: String?, species: String?, type: String?,
        gender: String?
    ): Flow<PagingData<Character>> {
        val characters: Flow<PagingData<Character>> = repository
            .characterPagingFlow
            .cachedIn(viewModelScope)
       // val characters: Flow<PagingData<Character>> = cached
        return characters.map { pd ->
            pd.filter { character ->
                (name == null || character.name.contains(name, ignoreCase = true)) &&
                        (status == null || character.status.contains(status, ignoreCase = true)) &&
                        (species == null || character.species.contains(species, ignoreCase = true)) &&
                        (type == null || character.type.contains(type, ignoreCase = true)) &&
                        (gender == null || character.gender.contains(gender, ignoreCase = true))
            }
        }
    }

}