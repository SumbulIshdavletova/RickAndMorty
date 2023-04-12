package ru.sumbul.rickandmorty.characters

import android.os.Parcelable

sealed interface InfoCharacter {}

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: NameUrl,
    val location: NameUrl,
    val image: String,
    // val episode: List<String> = listOf(),
    val url: String,
    val created: String,
) : InfoCharacter

data class NameUrl(
    val name1: String,
    val url2: String,
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String,
) : InfoCharacter