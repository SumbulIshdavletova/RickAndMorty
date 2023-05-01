package ru.sumbul.rickandmorty.locations.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
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
    suspend fun getCharacters(@Path("ids") ids: String): Response<List<Character>>
}