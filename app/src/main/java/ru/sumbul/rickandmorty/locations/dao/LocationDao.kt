package ru.sumbul.rickandmorty.locations.dao

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.locations.entity.LocationEntity

@Dao
interface LocationDao {

    @Upsert
    suspend fun upsertAll(location: List<LocationEntity>)

    @Query("SELECT * FROM LocationEntity")
    fun pagingSource(): PagingSource<Int, LocationEntity>

    @Query("DELETE FROM LocationEntity")
    suspend fun clearAll()

    @Query("SELECT * FROM LocationEntity")
    fun getAll(): LiveData<List<LocationEntity>>

    @Upsert
    suspend fun upsert(location: LocationEntity)
}