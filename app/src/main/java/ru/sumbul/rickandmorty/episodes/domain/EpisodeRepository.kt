package ru.sumbul.rickandmorty.episodes.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.locations.data.entity.LocationFilterEntity

interface EpisodeRepository {

    val episodePagingFlow: Flow<PagingData<Episode>>
    suspend fun getById(id: Int): Episode
   // val charactersData: LiveData<List<ru.sumbul.rickandmorty.characters.domain.model.Character>>
    suspend fun getCharactersForEpisode(ids: List<Int>)
    fun getData(): MutableLiveData<List<Character>?>?

    suspend fun filterEpisodes(name: String?, episode: String?)


}