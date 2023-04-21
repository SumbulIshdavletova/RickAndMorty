package ru.sumbul.rickandmorty.characterDetails.api

import retrofit2.http.GET
import retrofit2.http.Path
import ru.sumbul.rickandmorty.episodes.entity.Episode

interface CharacterDetailsService {

    @GET("episode/{ids}")
    fun getEpisodes(@Path("ids") ids: String): io.reactivex.rxjava3.core.Observable<List<Episode>>

}