package ru.sumbul.rickandmorty.characters.domain.model


data class ResponseApi(
    val info: Info,
    val results: List<Character>
)

data class Info
    (
    val count: Int,
    val pages: Int,
    val next: Any,
    val prev: Any
)