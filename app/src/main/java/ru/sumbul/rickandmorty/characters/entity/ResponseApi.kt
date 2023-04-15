package ru.sumbul.rickandmorty.characters.entity


data class ResponseApi(
    val info: Info,
    val results: List<ru.sumbul.rickandmorty.characters.entity.Character>
)

data class Info
    (
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: Any
)