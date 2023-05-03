package ru.sumbul.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.episodes.data.EpisodeRepositoryImpl
import ru.sumbul.rickandmorty.episodes.ui.details.EpisodeDetailsViewModel
import ru.sumbul.rickandmorty.episodes.ui.list.EpisodeViewModel
import javax.inject.Inject

class EpisodeDetailsViewModelFactory @Inject constructor(
    private val repository: EpisodeRepositoryImpl
) : ViewModelProvider.Factory {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EpisodeDetailsViewModel(repository) as T
    }
}