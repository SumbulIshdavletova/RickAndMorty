package ru.sumbul.rickandmorty.characters.details.domain

import androidx.lifecycle.LiveData
import ru.sumbul.rickandmorty.episodes.entity.Episode

interface CharacterDetailsRepository {
    val data: LiveData<List<Episode>>
    suspend fun getEpisodes(urls: List<String>)
}