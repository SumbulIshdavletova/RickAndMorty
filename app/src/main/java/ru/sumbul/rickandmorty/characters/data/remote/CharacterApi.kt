package ru.sumbul.rickandmorty.characters.data.remote

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.domain.model.ResponseApi
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.domain.model.Location

interface CharacterApi {

    @GET("character/")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?,
    ): Response<ResponseApi>

    @GET("character/")
    suspend fun getCharacterById(
        @Query("id") id: Int
    ): Response<CharacterEntity>

    @GET("character/{id}")
    suspend fun getById(@Path("id") id: Int): Response<Character>

    @GET("episode/{ids}")
    fun getEpisodes(@Path("ids") ids: String): Observable<List<Episode>>

    @GET("location/")
    fun getLocationById(
        @Query("id") id: Int
    ): Single<Location>

    @GET("episode/")
    fun getEpisodeById(
        @Query("id") id: Int
    ): Single<Episode>

}