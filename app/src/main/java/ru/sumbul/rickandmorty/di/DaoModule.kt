package ru.sumbul.rickandmorty.di

import dagger.Module
import dagger.Provides
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeFilterDao
import ru.sumbul.rickandmorty.episodes.data.local.dao.EpisodeRemoteKeyDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationDao
import ru.sumbul.rickandmorty.locations.data.local.LocationDb
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationFilterDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationRemoteKeyDao
import javax.inject.Singleton


@Module
object DaoModule {


    @Provides
    @Singleton
    fun provideCharacterDao(db: CharacterDb): CharacterDao = db.characterDao()

    @Provides
    @Singleton
    fun provideEpisodeDao(db: EpisodeDb): EpisodeDao = db.episodeDao()

    @Provides
    @Singleton
    fun provideLocationDao(db: LocationDb): LocationDao = db.locationDao()

    @Provides
    @Singleton
    fun provideFilterDao(db: CharacterDb): FilterDao = db.filterDao()

    @Provides
    @Singleton
    fun provideRemoteKeyDao(db: CharacterDb): RemoteKeyDao = db.remoteKeyDao()

    @Provides
    @Singleton
    fun provideLocationRemoteDao(db: LocationDb): LocationRemoteKeyDao = db.remoteKeyDao()

    @Provides
    @Singleton
    fun provideLocationFilterDao(db: LocationDb): LocationFilterDao = db.filterDao()


    @Provides
    @Singleton
    fun provideEpisodeRemoteDao(db: EpisodeDb): EpisodeRemoteKeyDao = db.remoteKeyDao()

    @Provides
    @Singleton
    fun provideEpisodeFilterDao(db: EpisodeDb): EpisodeFilterDao = db.filterDao()
}