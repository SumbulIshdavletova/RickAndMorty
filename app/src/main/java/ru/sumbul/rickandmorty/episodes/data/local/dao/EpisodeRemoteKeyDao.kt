package ru.sumbul.rickandmorty.episodes.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeRemoteKeyEntity
import ru.sumbul.rickandmorty.locations.data.entity.LocationRemoteKeyEntity

@Dao
interface EpisodeRemoteKeyDao {

    @Upsert
    suspend fun insert(remoteKey: EpisodeRemoteKeyEntity)

    @Upsert
    suspend fun insert(RemoteKeyEntity: List<EpisodeRemoteKeyEntity>)

    @Query("SELECT * FROM EpisodeRemoteKeyEntity WHERE label = :query")
    suspend fun remoteKeyByQuery(query: String): LocationRemoteKeyEntity

    @Query("SELECT nextPage FROM EpisodeRemoteKeyEntity")
    suspend fun getNextPage(): Int?

    @Query("DELETE FROM EpisodeRemoteKeyEntity WHERE label = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM EpisodeRemoteKeyEntity")
    suspend fun clear()

}