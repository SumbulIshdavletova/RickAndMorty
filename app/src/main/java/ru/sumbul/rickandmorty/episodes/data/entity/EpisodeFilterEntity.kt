package ru.sumbul.rickandmorty.episodes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.rxjava3.annotations.NonNull

@Entity
class EpisodeFilterEntity
    (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int,
    var name: String? = null,
    var episode: String? = null,
)
