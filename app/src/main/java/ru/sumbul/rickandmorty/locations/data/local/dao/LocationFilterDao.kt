package ru.sumbul.rickandmorty.locations.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.locations.data.entity.LocationFilterEntity

@Dao
interface LocationFilterDao {
    @Upsert
    suspend fun upsert(filter: LocationFilterEntity)

    @Query("SELECT name FROM LocationFilterEntity")
    suspend fun getName(): String

    @Query("SELECT episode FROM LocationFilterEntity")
    suspend fun getEpisode(): String

    @Query("DELETE FROM LocationFilterEntity")
    suspend fun clear()
}