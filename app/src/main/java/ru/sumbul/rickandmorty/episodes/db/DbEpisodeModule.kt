package ru.sumbul.rickandmorty.episodes.db

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
import ru.sumbul.rickandmorty.episodes.api.EpisodeApi
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@InstallIn(SingletonComponent::class)
@Module
class DbEpisodeModule {

    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext
        context: Context
    ): EpisodeDb = Room.databaseBuilder(context, EpisodeDb::class.java, "episodes.db")
        .fallbackToDestructiveMigration()
        .build()


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideEpisodePager(
        episodeDb: EpisodeDb,
        episodeApi: EpisodeApi
    ): Pager<Int, EpisodeEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = EpisodeRemoteMediator(
                episodeDb, episodeApi
            ),
            pagingSourceFactory = {
                episodeDb.episodeDao().pagingSource()
            }
        )
    }
}