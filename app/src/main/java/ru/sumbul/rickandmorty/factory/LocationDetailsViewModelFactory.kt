package ru.sumbul.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sumbul.rickandmorty.locations.data.LocationRepositoryImpl
import ru.sumbul.rickandmorty.locations.ui.details.LocationDetailsViewModel
import javax.inject.Inject

class LocationDetailsViewModelFactory @Inject constructor
    (private val repository: LocationRepositoryImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationDetailsViewModel(repository) as T
    }
}