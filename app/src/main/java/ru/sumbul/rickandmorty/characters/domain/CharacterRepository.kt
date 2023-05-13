package ru.sumbul.rickandmorty.characters.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.domain.model.Location

interface CharacterRepository {

    val characterPagingFlow: Flow<PagingData<Character>>
    suspend fun filterCharacters(
        name: String, status: String?, species: String?,
        type: String?, gender: String?
    )

    fun getEpisodes(ids: List<Int> ): Observable<List<Episode>>
    fun getLocationById(id: Int): Single<Location>
    fun getEpisodeById(id: Int): Single<Episode>
}