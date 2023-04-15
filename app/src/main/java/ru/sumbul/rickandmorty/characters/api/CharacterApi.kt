package ru.sumbul.rickandmorty.characters.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.characters.entity.ResponseApi

interface CharacterApi {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<ResponseApi>

    suspend fun filterCharacters(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    )
}