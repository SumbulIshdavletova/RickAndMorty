package ru.sumbul.rickandmorty.characters

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
import ru.sumbul.rickandmorty.Pagination.CharacterRemoteMediator
import ru.sumbul.rickandmorty.api.CharacterApi
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(
        @ApplicationContext
        context: Context
    ): CharacterDb = Room.databaseBuilder(context, CharacterDb::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideBeerPager(
        characterDb: CharacterDb,
        characterApi: CharacterApi
    ): Pager<Int, CharacterEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharacterRemoteMediator(
                characterDb, characterApi
            ),
            pagingSourceFactory = {
                characterDb.characterDao().getPagingSource()
            }
        )
    }
}