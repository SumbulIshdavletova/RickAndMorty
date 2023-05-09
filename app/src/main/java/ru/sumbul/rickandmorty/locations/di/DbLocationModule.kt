package ru.sumbul.rickandmorty.locations.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.sumbul.rickandmorty.locations.domain.LocationRemoteMediator
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.data.remote.LocationApi
import ru.sumbul.rickandmorty.locations.data.local.LocationDb
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationFilterDao
import ru.sumbul.rickandmorty.locations.data.local.dao.LocationRemoteKeyDao
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper
import javax.inject.Singleton


@Module
class DbLocationModule {

    @Singleton
    @Provides
    fun provideDb(
        context: Context
    ): LocationDb = Room.databaseBuilder(context, LocationDb::class.java, "location.db")
        .fallbackToDestructiveMigration()
        .build()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideLocationPager(
        locationDb: LocationDb,
        locationApi: LocationApi,
        mapper: LocationMapper,
        remoteKeyDao: LocationRemoteKeyDao,
        filterDao: LocationFilterDao
    ): Pager<Int, LocationEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationRemoteMediator(
                locationDb, locationApi, filterDao, remoteKeyDao, mapper
            ),
            pagingSourceFactory = {
                locationDb.locationDao().pagingSource()
            }
        )
    }

}