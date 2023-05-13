package ru.sumbul.rickandmorty.episodes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EpisodeRemoteKeyEntity(
    @PrimaryKey
    val label: String,
    val nextPage: Int?
) {
}