package ru.sumbul.rickandmorty.characters.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.entity.FilteredCharactersEntity
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilteredCharactersDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.data.mapper.FilterChMapper
import ru.sumbul.rickandmorty.characters.domain.FilteredRemoteMediator
import ru.sumbul.rickandmorty.characters.domain.SecondRemoteMediator
import javax.inject.Singleton


@Module
class DbCharacterModule {

    @Singleton
    @Provides
    fun provideDb(
        context: Context
    ): CharacterDb = Room.databaseBuilder(context, CharacterDb::class.java, "character.db")
        .fallbackToDestructiveMigration()
        .build()


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideCharacterPager(
        characterDb: CharacterDb,
        characterApi: CharacterApi,
        filterDao: FilterDao,
        remoteKeyDao: RemoteKeyDao,
        mapper: CharacterMapper,
    ): Pager<Int, CharacterEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = FilteredRemoteMediator(
                characterDb, characterApi, filterDao, remoteKeyDao, mapper
            ),
            pagingSourceFactory = {
                characterDb.characterDao().getPagingSource()
            }
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideFilteredCharacterPager(
        characterDb: CharacterDb,
        characterApi: CharacterApi,
        filterDao: FilterDao,
        remoteKeyDao: RemoteKeyDao,
        mapper: CharacterMapper,
        filteredCharactersDao: FilteredCharactersDao,
        filterChMapper: FilterChMapper,
    ): Pager<Int, FilteredCharactersEntity> {
        return Pager(
            config = PagingConfig(pageSize = 90),
            remoteMediator = SecondRemoteMediator(
                characterDb,
                characterApi,
                filterDao,
                remoteKeyDao,
                mapper,
                filteredCharactersDao,
                filterChMapper
            ),
            pagingSourceFactory = {
                characterDb.filterCharacterDao().getPagingSource()
            }
        )
    }

}