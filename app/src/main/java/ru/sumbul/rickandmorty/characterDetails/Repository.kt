package ru.sumbul.rickandmorty.characterDetails

import androidx.lifecycle.LiveData
import ru.sumbul.rickandmorty.episodes.entity.Episode

interface Repository {
    val data: LiveData<List<Episode>>
    suspend fun getEpisodes(urls: List<String>)
}