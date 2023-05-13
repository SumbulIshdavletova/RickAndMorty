package ru.sumbul.rickandmorty.characters.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class RemoteKeyEntity(
    @PrimaryKey
    val label: String, val nextPage: Int?
)
