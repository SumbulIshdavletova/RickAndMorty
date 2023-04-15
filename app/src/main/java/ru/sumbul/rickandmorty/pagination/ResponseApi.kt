package ru.sumbul.rickandmorty.pagination

data class ResponseApi(
    val info: Info,
    val results: List<ru.sumbul.rickandmorty.characters.entity.Character>
)