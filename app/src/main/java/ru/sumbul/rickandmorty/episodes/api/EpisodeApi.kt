package ru.sumbul.rickandmorty.episodes.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.entity.ResponseApiEpisode

interface EpisodeApi {

    @GET("episode/")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): Response<ResponseApiEpisode>


    @GET("episode/")
    suspend fun getEpisodeById(
        @Query("id") id: Int
    ): Response<EpisodeEntity>

    @GET("character/{ids}")
    suspend fun getCharacters(@Path("ids") ids: String): Response<List<ru.sumbul.rickandmorty.characters.entity.Character>>
}