package ru.sumbul.rickandmorty.locations.entity

import ru.sumbul.rickandmorty.episodes.entity.Episode


data class ResponseApiLocation(
    val info: Info,
    val results: List<Location>
)

data class Info
    (
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: Any
)