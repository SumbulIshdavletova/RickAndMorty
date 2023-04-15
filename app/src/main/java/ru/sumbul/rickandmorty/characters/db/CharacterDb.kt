package ru.sumbul.rickandmorty.characters.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.sumbul.rickandmorty.characters.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 2
)

abstract class CharacterDb : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}