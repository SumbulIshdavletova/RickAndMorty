package ru.sumbul.rickandmorty.episodes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity

@Database(
    entities = [EpisodeEntity::class],
    version = 1
)

abstract class EpisodeDb : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
}