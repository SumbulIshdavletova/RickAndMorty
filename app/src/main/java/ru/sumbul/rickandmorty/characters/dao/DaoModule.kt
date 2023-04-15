package ru.sumbul.rickandmorty.characters.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sumbul.rickandmorty.characters.db.CharacterDb

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    fun provideCharacterDao(db: CharacterDb): CharacterDao = db.characterDao()
}