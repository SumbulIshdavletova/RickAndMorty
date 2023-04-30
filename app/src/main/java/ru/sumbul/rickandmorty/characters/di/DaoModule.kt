package ru.sumbul.rickandmorty.characters.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
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

    @Provides
    fun provideFilterDao(db: CharacterDb): FilterDao = db.filterDao()

    @Provides
    fun provideRemoteKeyDao(db: CharacterDb): RemoteKeyDao = db.remoteKeyDao()

}