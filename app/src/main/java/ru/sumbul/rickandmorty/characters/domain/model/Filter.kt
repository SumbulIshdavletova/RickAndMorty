package ru.sumbul.rickandmorty.characters.domain.model


data class Filter(
    var id: Int = 1,
    var name: String? = "",
    var status: String? = "",
    var species: String? = "",
    var type: String? = "",
    var gender: String? = "",
)
