package ru.sumbul.rickandmorty.characters.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import io.reactivex.rxjava3.core.Observable
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity


@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    fun getPagingSource(): PagingSource<Int, CharacterEntity>

    @Upsert
    suspend fun upsert(character: CharacterEntity)

    @Upsert
    suspend fun upsertAll(character: List<CharacterEntity>)

    @Query("DELETE FROM CharacterEntity")
    suspend fun clearAll()

    @Query("SELECT * FROM CharacterEntity WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity

    @Query("SELECT * FROM CharacterEntity WHERE (:name IS NULL OR name LIKE '%' || :name || '%')" +
            "AND (:status IS NULL OR status LIKE :status)" +
            "AND (:species IS NULL OR species LIKE '%' || :species || '%')" +
            "AND (:type IS NULL OR type LIKE '%' || :type || '%')" +
            "AND (:gender IS NULL OR gender LIKE :gender)")
    suspend fun getFilteredCharacters(name: String?, status: String?, species: String?,
                              type: String?, gender: String?): List<CharacterEntity>

    @Query("SELECT * FROM CharacterEntity WHERE id IN (:ids)")
    suspend fun getCharactersByIds(ids: List<Int>): List<CharacterEntity>

    @Query("SELECT * FROM CharacterEntity WHERE id IN (:ids)")
    fun getCharactersByIdsRx(ids: List<Int>): List<CharacterEntity>

}