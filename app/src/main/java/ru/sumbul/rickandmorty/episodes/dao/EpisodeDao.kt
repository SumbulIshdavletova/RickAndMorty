package ru.sumbul.rickandmorty.episodes.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity

@Dao
interface EpisodeDao {

    @Upsert
    suspend fun upsertAll(episode: List<EpisodeEntity>)

    @Query("SELECT * FROM EpisodeEntity")
    fun pagingSource(): PagingSource<Int, EpisodeEntity>

    @Query("DELETE FROM EpisodeEntity")
    suspend fun clearAll()

}