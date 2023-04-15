package ru.sumbul.rickandmorty.episodes.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.entity.LocationEmbedded
import ru.sumbul.rickandmorty.characters.entity.OriginEmbedded
import ru.sumbul.rickandmorty.util.StringListTypeConverter

@Entity
class EpisodeEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val air_date: String,
    val episode: String,
    @TypeConverters(StringListTypeConverter::class)
    val characters: List<String> = emptyList(),
    val url: String,
    val created: String,
) {
    fun toDto() = Episode(
        id,
        name,
        air_date,
        episode,
        characters,
        url,
        created
    )

    companion object {
        fun fromDto(dto: Episode) =
            EpisodeEntity(
                dto.id,
                dto.name,
                dto.air_date,
                dto.episode,
                dto.characters,
                dto.url,
                dto.created,
            )
    }
}

fun List<EpisodeEntity>.toDto(): List<Episode> = map(EpisodeEntity::toDto)
fun List<Episode>.toEntity(): List<EpisodeEntity> =
    map(EpisodeEntity::fromDto)