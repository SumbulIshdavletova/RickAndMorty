package ru.sumbul.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.characters.data.CharacterRepositoryImpl
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.characters.ui.list.CharacterViewModel
import ru.sumbul.rickandmorty.locations.domain.LocationRepository
import javax.inject.Inject

class CharactersViewModelFactory @Inject constructor(
    private val repository: CharacterRepositoryImpl
) : ViewModelProvider.Factory {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterViewModel(repository) as T
    }
}