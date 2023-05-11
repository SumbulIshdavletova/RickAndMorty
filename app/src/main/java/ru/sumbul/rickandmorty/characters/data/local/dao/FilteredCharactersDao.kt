package ru.sumbul.rickandmorty.characters.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.entity.FilteredCharactersEntity

@Dao
interface FilteredCharactersDao {

    @Query("SELECT * FROM FilteredCharactersEntity")
    fun getPagingSource(): PagingSource<Int, FilteredCharactersEntity>

    @Upsert
    suspend fun upsert(character: FilteredCharactersEntity)

    @Upsert
    suspend fun upsertAll(character: List<FilteredCharactersEntity>)

    @Query("DELETE FROM CharacterEntity")
    suspend fun clearAll()

    @Query("SELECT * FROM FilteredCharactersEntity WHERE id = :id")
    suspend fun getCharacterById(id: Int): FilteredCharactersEntity

    @Query(
        "SELECT * FROM FilteredCharactersEntity WHERE (:name IS NULL OR name LIKE '%' || :name || '%')" +
                "AND (:status IS NULL OR status LIKE :status)" +
                "AND (:species IS NULL OR species LIKE '%' || :species || '%')" +
                "AND (:type IS NULL OR type LIKE '%' || :type || '%')" +
                "AND (:gender IS NULL OR gender LIKE :gender)"
    )
    suspend fun getFilteredCharacters(
        name: String?, status: String?, species: String?,
        type: String?, gender: String?
    ): List<FilteredCharactersEntity>

    @Query("SELECT * FROM FilteredCharactersEntity WHERE id IN (:ids)")
    suspend fun getCharactersByIds(ids: List<Int>): List<FilteredCharactersEntity>

    @Query("SELECT * FROM FilteredCharactersEntity WHERE id IN (:ids)")
    fun getCharactersByIdsRx(ids: List<Int>): List<FilteredCharactersEntity>

}