package ru.sumbul.rickandmorty.characters.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.rxjava3.annotations.NonNull

@Entity
data class FilterEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int,
    var name: String? = null,
    var status: String? = null,
    var species: String? = null,
    var type: String? = null,
    var gender: String? = null,
)