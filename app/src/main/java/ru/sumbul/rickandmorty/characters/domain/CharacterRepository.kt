package ru.sumbul.rickandmorty.characters.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.entity.ResponseApi

interface CharacterRepository {
    val characterPagingFlow: Flow<PagingData<ru.sumbul.rickandmorty.characters.entity.Character>>
   
  // suspend fun filterCharacters(name: String, status: String, gender: String)
}