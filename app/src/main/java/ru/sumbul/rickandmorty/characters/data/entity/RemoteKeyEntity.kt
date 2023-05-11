package ru.sumbul.rickandmorty.characters.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//data class RemoteKey(val label: String, val nextPage: Int?)

@Entity
data class RemoteKeyEntity(
    @PrimaryKey
    val label: String, val nextPage: Int?
)
