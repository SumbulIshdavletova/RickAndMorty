package ru.sumbul.rickandmorty.locations.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.rickandmorty.util.StringListTypeConverter

@Entity
class LocationEntity (
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val type: String,
    val dimension: String,
    @field:TypeConverters(StringListTypeConverter::class)
    val residents: List<String> = emptyList(),
    val url: String,
    val created: String,
)