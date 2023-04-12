package ru.sumbul.rickandmorty.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class CharacterViewModel @Inject constructor(
    pager: Pager<Int, CharacterEntity>
): ViewModel() {

    val characterPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.toDto() }
        }
       .cachedIn(viewModelScope)
    //    .asLiveData(Dispatchers.Default)
}