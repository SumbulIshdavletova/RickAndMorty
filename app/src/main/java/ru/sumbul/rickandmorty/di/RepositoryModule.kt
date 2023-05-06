package ru.sumbul.rickandmorty.di

import dagger.Binds
import dagger.Module
import ru.sumbul.rickandmorty.characters.data.CharacterRepositoryImpl
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.episodes.data.EpisodeRepositoryImpl
import ru.sumbul.rickandmorty.episodes.domain.EpisodeRepository
import ru.sumbul.rickandmorty.locations.data.LocationRepositoryImpl
import ru.sumbul.rickandmorty.locations.domain.LocationRepository
import javax.inject.Singleton


@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsCharacterRepository(impl: CharacterRepositoryImpl): CharacterRepository

    @Binds
    @Singleton
    fun bindsEpisodeRepository(impl: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    @Singleton
    fun bindsLocationRepository(impl: LocationRepositoryImpl): LocationRepository

}