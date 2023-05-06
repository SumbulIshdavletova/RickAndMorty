package ru.sumbul.rickandmorty.locations.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.rxjava3.annotations.NonNull

@Entity
class LocationFilterEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int,
    var name: String? = null,
    var episode: String? = null,
)