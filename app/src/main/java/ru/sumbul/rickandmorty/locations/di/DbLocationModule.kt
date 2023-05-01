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
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
class DbLocationModule {

    @Singleton
    @Provides
    fun provideDb(
        context: Context
    ): LocationDb = Room.databaseBuilder(context, LocationDb::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideLocationPager(
        locationDb: LocationDb,
        locationApi: LocationApi
    ): Pager<Int, LocationEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = LocationRemoteMediator(
                locationDb, locationApi
            ),
            pagingSourceFactory = {
                locationDb.locationDao().pagingSource()
            }
        )
    }

}