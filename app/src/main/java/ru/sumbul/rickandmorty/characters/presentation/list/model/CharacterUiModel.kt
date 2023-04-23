package ru.sumbul.rickandmorty.characters.presentation.list.model

data class CharacterUiModel(
    val id: Int = 0,
    val name: String,
    val status: String,
    val gender: String,
    val image: String,
)