package ru.sumbul.rickandmorty.Pagination

data class InfoDto
    (
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String
) {
}