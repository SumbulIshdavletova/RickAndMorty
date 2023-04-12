package ru.sumbul.rickandmorty.characters

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CharacterEntity::class],
    version = 1
)

abstract class CharacterDb : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}