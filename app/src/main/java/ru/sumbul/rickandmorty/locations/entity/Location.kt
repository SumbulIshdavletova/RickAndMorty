package ru.sumbul.rickandmorty.locations.entity

import androidx.room.TypeConverters
import ru.sumbul.rickandmorty.util.StringListTypeConverter

data class Location(
    val id: Int = 0,
    val name: String,
    val type: String,
    val dimension: String,
    @TypeConverters(StringListTypeConverter::class)
    val residents: List<String> = emptyList(),
    val url: String,
    val created: String,
)