package ru.sumbul.rickandmorty.episodes.domain.model

import androidx.room.TypeConverters
import ru.sumbul.rickandmorty.util.StringListTypeConverter

@kotlinx.serialization.Serializable
data class Episode(
    val id: Int = 0,
    val name: String,
    val air_date: String,
    val episode: String,
    @TypeConverters(StringListTypeConverter::class)
    val characters: List<String> = emptyList(),
    val url: String,
    val created: String,
): java.io.Serializable