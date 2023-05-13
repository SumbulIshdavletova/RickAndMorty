package ru.sumbul.rickandmorty.episodes.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import ru.sumbul.rickandmorty.util.StringListTypeConverter

@Entity
data class EpisodeEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val air_date: String,
    val episode: String,
    @field:TypeConverters(StringListTypeConverter::class)
    val characters: List<String> = emptyList(),
    val url: String,
    val created: String,
)
