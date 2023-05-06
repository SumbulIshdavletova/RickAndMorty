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

    @Query("SELECT type FROM LocationFilterEntity")
    suspend fun getType(): String

    @Query("SELECT dimension FROM LocationFilterEntity")
    suspend fun getDimension(): String

    @Query("DELETE FROM LocationFilterEntity")
    suspend fun clear()
}