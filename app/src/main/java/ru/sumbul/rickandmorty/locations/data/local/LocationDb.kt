package ru.sumbul.rickandmorty.locations.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.data.entity.LocationFilterEntity
import ru.sumbul.rickandmorty.locations.data.entity.LocationRemoteKeyEntity
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationFilterDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationRemoteKeyDao

@Database(
    entities = [LocationEntity::class, LocationRemoteKeyEntity::class, LocationFilterEntity::class],
    version = 2
)

abstract class LocationDb : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun filterDao(): LocationFilterDao
    abstract fun remoteKeyDao(): LocationRemoteKeyDao
}