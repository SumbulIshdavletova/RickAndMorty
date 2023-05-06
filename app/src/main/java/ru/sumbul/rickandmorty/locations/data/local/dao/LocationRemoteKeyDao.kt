package ru.sumbul.rickandmorty.locations.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.locations.data.entity.LocationRemoteKeyEntity

@Dao
interface LocationRemoteKeyDao {

    @Upsert
    suspend fun insert(remoteKey: LocationRemoteKeyEntity)

    @Upsert
    suspend fun insert(RemoteKeyEntity: List<LocationRemoteKeyEntity>)

    @Query("SELECT * FROM LocationRemoteKeyEntity WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): LocationRemoteKeyEntity

    @Query("SELECT nextPage FROM LocationRemoteKeyEntity")
    suspend fun getNextPage(): Int?

    @Query("DELETE FROM LocationRemoteKeyEntity WHERE label = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM LocationRemoteKeyEntity")
    suspend fun clear()

}