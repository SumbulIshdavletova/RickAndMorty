package ru.sumbul.rickandmorty.characters.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.entity.ResponseApi
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import rx.Observable

interface CharacterApi {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<ResponseApi>

    @GET("character/")
    suspend fun getCharacterById(
        @Query("id") id: Int
    ): Response<CharacterEntity>

    @GET("character/{id}")
    suspend fun getById(@Path("id") id: Int): Response<ru.sumbul.rickandmorty.characters.entity.Character>

    @GET("episode/{ids}")
    suspend fun getEpisodes(@Path("ids") ids: String): Response<List<Episode>>

    suspend fun filterCharacters(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    )
}