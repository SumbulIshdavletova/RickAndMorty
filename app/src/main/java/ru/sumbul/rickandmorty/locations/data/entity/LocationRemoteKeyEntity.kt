package ru.sumbul.rickandmorty.locations.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationRemoteKeyEntity(
    @PrimaryKey
    val label: String,
    val nextPage: Int?
)