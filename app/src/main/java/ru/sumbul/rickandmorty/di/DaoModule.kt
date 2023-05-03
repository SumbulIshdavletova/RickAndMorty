package ru.sumbul.rickandmorty.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.domain.FilteredRemoteMediator
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDao
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.locations.data.local.LocationDao
import ru.sumbul.rickandmorty.locations.data.local.LocationDb
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

}