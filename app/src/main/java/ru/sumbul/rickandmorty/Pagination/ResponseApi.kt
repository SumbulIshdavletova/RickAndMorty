package ru.sumbul.rickandmorty.Pagination

data class ResponseApi (
    val info: Info,
    val results: List<ru.sumbul.rickandmorty.characters.Character>
        )