package ru.sumbul.rickandmorty.characters.domain

import ru.sumbul.rickandmorty.characters.domain.model.CharacterDomain

interface CharacterRepository {

   suspend fun getCharacterById(id:Int) : CharacterDomain

}