package ru.sumbul.rickandmorty.locations.domain


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.locations.domain.model.Location

interface LocationRepository {
    val locationPagingFlow: Flow<PagingData<Location>>

    suspend fun filterLocation(name: String?, type: String?, dimension: String?)
    suspend fun getById(id: Int): Location
    fun getCharacters(ids: String) : Observable<List<Character>>
    fun getData(): MutableLiveData<List<Character>?>?
 //   fun getData1(): Observable<List<Character>>
}