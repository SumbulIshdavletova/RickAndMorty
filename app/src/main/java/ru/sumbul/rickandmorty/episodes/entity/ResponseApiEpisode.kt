package ru.sumbul.rickandmorty.episodes.entity

data class ResponseApiEpisode(
    val info: Info,
    val results: List<Episode>
)

data class Info
    (
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: Any
)