package ru.sumbul.rickandmorty.locations.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.episodes.entity.ResponseApiEpisode
import ru.sumbul.rickandmorty.locations.entity.Location
import ru.sumbul.rickandmorty.locations.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.entity.ResponseApiLocation

interface LocationApi {

    @GET("location/")
    suspend fun getLocations(
        @Query("page") page: Int
    ): Response<ResponseApiLocation>

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") id: Int
    ): Response<LocationEntity>


//    @GET("location/{id}")
//    suspend fun getLocationById2(
//        @Path("id") id: Int
//    ): Response<LocationEntity>

    @GET("character/{ids}")
    suspend fun getCharacters(@Path("ids") ids: String): Response<List<ru.sumbul.rickandmorty.characters.entity.Character>>
}