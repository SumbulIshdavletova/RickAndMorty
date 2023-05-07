package ru.sumbul.rickandmorty.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.sumbul.rickandmorty.characters.data.CharacterRepositoryImpl
import ru.sumbul.rickandmorty.characters.ui.details.CharacterDetailsViewModelJava
import javax.inject.Inject

class CharacterDetailsViewModelJavaFactory @Inject constructor
    (private val repository: CharacterRepositoryImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharacterDetailsViewModelJava(repository) as T
    }
}