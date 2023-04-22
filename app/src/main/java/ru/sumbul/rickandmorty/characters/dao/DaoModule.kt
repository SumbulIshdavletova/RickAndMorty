package ru.sumbul.rickandmorty.characters.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sumbul.rickandmorty.characters.db.CharacterDb
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.db.EpisodeDb
import ru.sumbul.rickandmorty.locations.dao.LocationDao
import ru.sumbul.rickandmorty.locations.db.LocationDb

@InstallIn(SingletonComponent::class)
@Module
object DaoModule {

    @Provides
    fun provideCharacterDao(db: CharacterDb): CharacterDao = db.characterDao()

    @Provides
    fun provideEpisodeDao(db: EpisodeDb): EpisodeDao = db.episodeDao()

    @Provides
    fun provideLocationDao(db: LocationDb): LocationDao = db.locationDao()

}