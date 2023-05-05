package ru.sumbul.rickandmorty.characters.data.local.dao

import androidx.room.*
import ru.sumbul.rickandmorty.characters.data.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    //@Insert(onConflict = OnConflictStrategy.REPLACE)

    @Upsert
    suspend fun insert(remoteKey: RemoteKeyEntity)

    @Upsert
    suspend fun insert(RemoteKeyEntity: List<RemoteKeyEntity>)

    @Query("SELECT * FROM RemoteKeyEntity WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): RemoteKeyEntity

    @Query("SELECT nextPage FROM RemoteKeyEntity")
    suspend fun getNextPage(): Int?

    @Query("DELETE FROM RemoteKeyEntity WHERE label = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM RemoteKeyEntity")
    suspend fun clear()

}
