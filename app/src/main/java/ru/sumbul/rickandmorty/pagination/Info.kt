package ru.sumbul.rickandmorty.pagination

data class Info
    (val count: Int,
    val pages: Int,
    val next: String,
    val prev: Any
) {
}