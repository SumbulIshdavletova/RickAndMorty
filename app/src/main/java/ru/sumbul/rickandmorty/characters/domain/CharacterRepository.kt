package ru.sumbul.rickandmorty.characters.domain


import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.domain.model.Location

interface CharacterRepository {
    val characterPagingFlow: Flow<PagingData<Character>>
    suspend fun filterCharacters(
        name: String, status: String, species: String,
        type: String, gender: String
    )

    //val data: LiveData<List<Episode>>
    suspend fun getEpisodes(ids: String)
    suspend fun getLocationById(url: String): Location
    suspend fun getEpisodeById(id: Int): Episode
    fun getData1(): MutableLiveData<List<Episode>?>?
}