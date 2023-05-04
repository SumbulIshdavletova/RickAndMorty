package ru.sumbul.rickandmorty.locations.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 1
)

abstract class LocationDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}