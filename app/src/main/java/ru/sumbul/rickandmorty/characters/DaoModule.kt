package ru.sumbul.rickandmorty.characters

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    fun provideCharacterDao(db: CharacterDb): CharacterDao = db.characterDao()
}