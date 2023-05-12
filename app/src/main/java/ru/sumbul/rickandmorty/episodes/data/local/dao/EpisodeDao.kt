package ru.sumbul.rickandmorty.episodes.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity

@Dao
interface EpisodeDao {


    @Upsert
    suspend fun upsertAll(episode: List<EpisodeEntity>)

    @Query("SELECT * FROM EpisodeEntity")
    fun pagingSource(): PagingSource<Int, EpisodeEntity>

    @Query("DELETE FROM EpisodeEntity")
    suspend fun clearAll()

    @Query("SELECT * FROM EpisodeEntity")
    fun getAll(): LiveData<List<EpisodeEntity>>

    @Upsert
    suspend fun upsert(episode: EpisodeEntity)

    @Query("SELECT * FROM EpisodeEntity WHERE id = :id")
    fun getEpisodeById(id: Int): EpisodeEntity

    @Query("SELECT * FROM EpisodeEntity WHERE id = :id")
    suspend fun getEpisodeByIdSuspend(id: Int): EpisodeEntity

    @Query("SELECT * FROM EpisodeEntity WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:episode IS NULL OR episode LIKE '%' || :episode || '%')")
    fun getFilteredEpisodes(name: String?, episode: String?): List<EpisodeEntity>

    @Query("SELECT * FROM EpisodeEntity WHERE id IN (:ids)")
    fun getEpisodesByIds(ids: List<Int?>?): List<EpisodeEntity>

}