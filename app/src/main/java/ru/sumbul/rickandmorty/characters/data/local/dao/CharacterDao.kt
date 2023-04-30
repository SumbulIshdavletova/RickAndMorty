package ru.sumbul.rickandmorty.characters.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    fun getPagingSource(): PagingSource<Int, CharacterEntity>

    @Upsert
    suspend fun upsert(character: CharacterEntity)

    @Upsert
    suspend fun upsertAll(character: List<CharacterEntity>)

    @Query("SELECT * FROM CharacterEntity")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM CharacterEntity")
    suspend fun clearAll()
}