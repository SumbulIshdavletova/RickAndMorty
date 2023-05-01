package ru.sumbul.rickandmorty.characters.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.sumbul.rickandmorty.characters.data.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKeyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
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
