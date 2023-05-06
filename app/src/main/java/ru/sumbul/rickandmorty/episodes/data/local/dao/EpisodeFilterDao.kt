package ru.sumbul.rickandmorty.episodes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeFilterEntity
import ru.sumbul.rickandmorty.locations.data.entity.LocationFilterEntity

@Dao
interface EpisodeFilterDao {
    @Upsert
    suspend fun upsert(filter: EpisodeFilterEntity)

    @Query("SELECT name FROM EpisodeFilterEntity")
    suspend fun getName(): String

    @Query("SELECT episode FROM EpisodeFilterEntity")
    suspend fun getEpisode(): String

    @Query("DELETE FROM EpisodeFilterEntity")
    suspend fun clear()
}