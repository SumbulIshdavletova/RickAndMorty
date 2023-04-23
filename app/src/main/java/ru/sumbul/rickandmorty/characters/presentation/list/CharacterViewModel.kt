package ru.sumbul.rickandmorty.characters.presentation.list

import androidx.lifecycle.*
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.rickandmorty.characters.api.CharacterApi
import ru.sumbul.rickandmorty.characters.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.domain.model.*
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import ru.sumbul.rickandmorty.model.ListModelState
import java.io.IOException
import javax.inject.Inject

var character: CharacterDomain = CharacterDomain(
    id = 0,
    name = "",
    status = "",
    species = "",
    type = "",
    gender = "",
    origin = CharacterOrigin(
        name = "",
        url = ""
    ),
    location = CharacterLocation(
        name = "",
        url = ""
    ),
    image = "",
    episode = emptyList(),
    url = "",
    created = "",
)



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
//    fun getById(id: Int): ru.sumbul.rickandmorty.characters.domain.model.Character {
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