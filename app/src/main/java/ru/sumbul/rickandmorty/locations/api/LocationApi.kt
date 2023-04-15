package ru.sumbul.rickandmorty.locations.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.sumbul.rickandmorty.episodes.entity.ResponseApiEpisode
import ru.sumbul.rickandmorty.locations.entity.ResponseApiLocation

interface LocationApi {

    @GET("location/")
    suspend fun getLocations(
        @Query("page") page: Int
    ): Response<ResponseApiLocation>

}