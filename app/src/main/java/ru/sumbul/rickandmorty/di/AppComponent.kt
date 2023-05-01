package ru.sumbul.rickandmorty.di

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.ui.MainActivity
import ru.sumbul.rickandmorty.characters.details.ui.CharacterDetailsFragment
import ru.sumbul.rickandmorty.characters.CharacterViewModel
import ru.sumbul.rickandmorty.characters.ui.CharactersListFragment
import ru.sumbul.rickandmorty.episodeDetails.EpisodeDetailsFragment
import ru.sumbul.rickandmorty.episodes.EpisodeViewModel
import ru.sumbul.rickandmorty.episodes.ui.EpisodesListFragment
import ru.sumbul.rickandmorty.locationDetails.LocationDetailsFragment
import ru.sumbul.rickandmorty.locations.LocationViewModel
import ru.sumbul.rickandmorty.locations.ui.LocationsListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, DaoModule::class, AppModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(charactersFragment: CharactersListFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(charactersDetailFragment: CharacterDetailsFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(charactersViewModel: CharacterViewModel)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(episodeDetailFragment: EpisodeDetailsFragment)
    fun inject(episodesFragment: EpisodesListFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(episodesViewModel: EpisodeViewModel)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(locationDetailsFragment: LocationDetailsFragment)
    fun inject(locationsFragment: LocationsListFragment)
    @OptIn(ExperimentalCoroutinesApi::class)
    fun inject(locationsViewModel: LocationViewModel)
    //TODO

//    @Component.Factory
//    interface AppComponentFactory{
//        fun create(@BindsInstance context: Context): AppComponent
//    }


    //fun getMainViewModelFactory(): MainActivityViewModelFactory
}
//
//class App: Application() {
//    // Reference to the application graph that is used across the whole app
//    val appComponent = DaggerAppComponent.create()
//}