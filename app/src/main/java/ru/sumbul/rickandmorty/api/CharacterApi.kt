package ru.sumbul.rickandmorty.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.Pagination.ResponseApi

interface CharacterApi {

    @GET("character/{id}")
    suspend fun getById(@Path("id") id: Long): Response<ru.sumbul.rickandmorty.characters.Character>

    @GET("character/{ids}")
    suspend fun getMultipleCharacters(ids: List<Int>)

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): Response<ResponseApi>
            //List<ru.sumbul.rickandmorty.characters.Character>

    suspend fun filterCharacters(
        name: String,
        status: String,
        species: String,
        type: String,
        gender: String
    )
}