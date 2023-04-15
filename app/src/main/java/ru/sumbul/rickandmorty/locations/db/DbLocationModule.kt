package ru.sumbul.rickandmorty.locations.db

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.sumbul.rickandmorty.episodes.EpisodeRemoteMediator
import ru.sumbul.rickandmorty.locations.LocationRemoteMediator
import ru.sumbul.rickandmorty.locations.api.LocationApi
import ru.sumbul.rickandmorty.locations.entity.LocationEntity
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@InstallIn(SingletonComponent::class)
@Module
class DbLocationModule {

    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext
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