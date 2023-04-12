package ru.sumbul.rickandmorty.characters

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE
import ru.sumbul.rickandmorty.characters.Character

@Dao
interface CharacterDao {
    @Query("SELECT * FROM CharacterEntity ORDER BY id DESC")
    fun getAll(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity ORDER BY id DESC")
    fun getPagingSource(): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: List<CharacterEntity>)

    @Upsert
    suspend fun upsertAll(character: List<CharacterEntity>)

    @Query("SELECT * FROM CharacterEntity")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM CharacterEntity")
    suspend fun clearAll()
}