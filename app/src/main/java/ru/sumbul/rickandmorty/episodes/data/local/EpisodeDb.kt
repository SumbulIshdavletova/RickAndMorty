package ru.sumbul.rickandmorty.episodes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeFilterEntity
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeRemoteKeyEntity
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeFilterDao
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeRemoteKeyDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationFilterDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationRemoteKeyDao

@Database(
    entities = [EpisodeEntity::class, EpisodeFilterEntity::class, EpisodeRemoteKeyEntity::class, CharacterEntity::class],
    version = 4
)

abstract class EpisodeDb : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
    abstract fun filterDao(): EpisodeFilterDao
    abstract fun remoteKeyDao(): EpisodeRemoteKeyDao
}