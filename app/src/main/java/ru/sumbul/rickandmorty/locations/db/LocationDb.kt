package ru.sumbul.rickandmorty.locations.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.locations.dao.LocationDao
import ru.sumbul.rickandmorty.locations.entity.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 2
)

abstract class LocationDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}