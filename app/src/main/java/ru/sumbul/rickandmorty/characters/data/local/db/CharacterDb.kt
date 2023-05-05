package ru.sumbul.rickandmorty.characters.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.entity.FilterEntity
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.entity.RemoteKeyEntity

@Database(
    entities = [CharacterEntity::class, FilterEntity::class, RemoteKeyEntity::class],
    version = 1
)

abstract class CharacterDb : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun filterDao(): FilterDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}