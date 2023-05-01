package ru.sumbul.rickandmorty.di

import dagger.Module
import dagger.Provides
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.data.local.LocationDb


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