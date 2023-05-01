package ru.sumbul.rickandmorty.di

import dagger.Binds
import dagger.Module
import ru.sumbul.rickandmorty.characters.data.CharacterRepositoryImpl
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import javax.inject.Singleton


@Module
interface RepositoryModule {
//TODO
//    @Singleton
//    @Binds
//    abstract fun provideRepository(repository: RepositoryImpl): CharacterDetailsRepositoryImpl

    @Binds
    @Singleton
    fun bindsPostRepository(impl: CharacterRepositoryImpl): CharacterRepository


}