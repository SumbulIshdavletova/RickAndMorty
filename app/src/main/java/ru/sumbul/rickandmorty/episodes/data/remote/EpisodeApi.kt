package ru.sumbul.rickandmorty.episodes.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.domain.model.ResponseApiEpisode

interface EpisodeApi {

    @GET("episode/")
    suspend fun getEpisodes(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?,
    ): Response<ResponseApiEpisode>


    @GET("episode/")
    suspend fun getEpisodeById(
        @Query("id") id: Int
    ): Response<EpisodeEntity>

    @GET("character/{ids}")
    suspend fun getCharacters(@Path("ids") ids: String): Response<List<Character>>
}