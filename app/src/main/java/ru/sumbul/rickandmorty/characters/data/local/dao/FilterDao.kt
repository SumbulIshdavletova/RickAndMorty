package ru.sumbul.rickandmorty.characters.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.characters.data.entity.FilterEntity

@Dao
interface FilterDao {

    @Upsert
    suspend fun upsert(filter: FilterEntity)

    @Query("SELECT name FROM FilterEntity")
    suspend fun getName(): String

    @Query("SELECT status FROM FilterEntity")
    suspend fun getStatus(): String

    @Query("SELECT gender FROM FilterEntity")
    suspend fun getGender(): String

    @Query("SELECT species FROM FilterEntity")
    suspend fun getSpecies(): String

    @Query("SELECT type FROM FilterEntity")
    suspend fun getType(): String

    @Query("DELETE FROM FilterEntity")
    suspend fun clear()
}