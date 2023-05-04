package ru.sumbul.rickandmorty.episodes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao

@Database(
    entities = [EpisodeEntity::class],
    version = 2
)

abstract class EpisodeDb : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
}