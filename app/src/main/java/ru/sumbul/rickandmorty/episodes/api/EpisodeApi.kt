package ru.sumbul.rickandmorty.episodes.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.sumbul.rickandmorty.episodes.entity.ResponseApiEpisode

interface EpisodeApi {

    @GET("episode/")
    suspend fun getEpisodes(
        @Query("page") page: Int
    ): Response<ResponseApiEpisode>
}