package ru.sumbul.rickandmorty.locations.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.domain.model.Location

interface LocationRepository {
    val locationPagingFlow: Flow<PagingData<Location>>
    suspend fun getById(id: Int): Location
    suspend fun  getCharacters(ids: String)
    val charactersForLocation: LiveData<List<Character>?>?
}