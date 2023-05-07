package ru.sumbul.rickandmorty.di

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.ui.MainActivity
import ru.sumbul.rickandmorty.characters.di.DbCharacterModule
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailViewModel
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailsFragment
import ru.sumbul.rickandmorty.characters.ui.list.CharacterFilterFragment
import ru.sumbul.rickandmorty.characters.ui.list.CharacterViewModel
import ru.sumbul.rickandmorty.characters.ui.list.CharactersListFragment
import ru.sumbul.rickandmorty.episodes.di.DbEpisodeModule
import ru.sumbul.rickandmorty.episodes.ui.details.EpisodeDetailsFragment
import ru.sumbul.rickandmorty.episodes.ui.details.EpisodeDetailsViewModel
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodeFilterFragment
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodeViewModel
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodesListFragment
import ru.sumbul.rickandmorty.locations.di.DbLocationModule
import ru.sumbul.rickandmorty.locations.ui.details.LocationDetailsFragment
import ru.sumbul.rickandmorty.locations.ui.details.LocationDetailsViewModel
import ru.sumbul.rickandmorty.locations.ui.details.LocationDetailsViewModelJava
import ru.sumbul.rickandmorty.locations.ui.list.LocationFilterFragment
import ru.sumbul.rickandmorty.locations.ui.list.LocationViewModel
import ru.sumbul.rickandmorty.locations.ui.list.LocationsListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, DaoModule::class, AppModule::class, DbCharacterModule::class, DbEpisodeModule::class, DbLocationModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(charactersFragment: CharactersListFragment)
    fun inject(characterFilterFragment: CharacterFilterFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(charactersDetailFragment: CharacterDetailsFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(charactersViewModel: CharacterViewModel)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(characterDetailViewModel: CharacterDetailViewModel)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(episodeDetailFragment: EpisodeDetailsFragment)
    fun inject(episodesFragment: EpisodesListFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(episodesViewModel: EpisodeViewModel)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(episodeDetailsViewModel: EpisodeDetailsViewModel)
    fun inject(episodeFilterFragment: EpisodeFilterFragment)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(locationDetailsFragment: LocationDetailsFragment)
    fun inject(locationsFragment: LocationsListFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(locationsViewModel: LocationViewModel)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(locationDetailViewModel: LocationDetailsViewModel)
    fun inject(locationDetailsViewModelJava: LocationDetailsViewModelJava)
    fun inject(locationFilterFragment: LocationFilterFragment)

}