package ru.sumbul.rickandmorty.model

data class ListModelState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val error: Boolean = false,
)