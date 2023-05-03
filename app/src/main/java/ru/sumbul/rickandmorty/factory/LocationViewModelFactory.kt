package ru.sumbul.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.episodes.data.EpisodeRepositoryImpl
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodeViewModel
import ru.sumbul.rickandmorty.locations.data.LocationRepositoryImpl
import ru.sumbul.rickandmorty.locations.ui.list.LocationViewModel
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(
    private val repository: LocationRepositoryImpl
) : ViewModelProvider.Factory {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(repository) as T
    }
}