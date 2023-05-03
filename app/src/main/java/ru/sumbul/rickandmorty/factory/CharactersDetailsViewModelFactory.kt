package ru.sumbul.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.sumbul.rickandmorty.characters.data.CharacterRepositoryImpl
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailViewModel
import ru.sumbul.rickandmorty.characters.ui.list.CharacterViewModel
import javax.inject.Inject

class CharactersDetailsViewModelFactory @Inject constructor(
    private val repository: CharacterRepositoryImpl
) : ViewModelProvider.Factory {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailViewModel(repository) as T
    }
}