package ru.sumbul.rickandmorty.episodes.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRemoteMediator
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.remote.EpisodeApi
import ru.sumbul.rickandmorty.episodes.data.local.EpisodeDb
import ru.sumbul.rickandmorty.episodes.data.mapper.EpisodeMapper
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
class DbEpisodeModule {

    @Singleton
    @Provides
    fun provideDb(
        context: Context
    ): EpisodeDb = Room.databaseBuilder(context, EpisodeDb::class.java, "episodes.db")
        .fallbackToDestructiveMigration()
        .build()


    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideEpisodePager(
        episodeDb: EpisodeDb,
        episodeApi: EpisodeApi,
        episodeMapper: EpisodeMapper
    ): Pager<Int, EpisodeEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = EpisodeRemoteMediator(
                episodeDb, episodeApi, episodeMapper
            ),
            pagingSourceFactory = {
                episodeDb.episodeDao().pagingSource()
            }
        )
    }
}