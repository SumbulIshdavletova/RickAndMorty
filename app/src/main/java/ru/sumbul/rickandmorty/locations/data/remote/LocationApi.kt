package ru.sumbul.rickandmorty.locations.data.remote

import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.domain.model.Location
import ru.sumbul.rickandmorty.locations.domain.model.ResponseApiLocation


interface LocationApi {

    @GET("location/")
    suspend fun getLocations(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?,
    ): Response<ResponseApiLocation>

    @GET("location/{id}")
    fun getLocationById(
        @Path("id") id: Int
    ): Single<Location>

    @GET("character/{ids}")
    fun getCharacters(@Path("ids") ids: String): Observable<List<Character>>
}