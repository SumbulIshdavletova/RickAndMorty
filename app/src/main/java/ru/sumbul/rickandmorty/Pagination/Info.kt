package ru.sumbul.rickandmorty.Pagination

data class Info
    (
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: Any
) {
}