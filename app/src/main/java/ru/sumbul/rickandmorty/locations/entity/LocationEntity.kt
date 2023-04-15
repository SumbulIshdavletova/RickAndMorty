package ru.sumbul.rickandmorty.locations.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
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
) {
    fun toDto() = Location(
        id,
        name,
        type,
        dimension,
        residents,
        url,
        created
    )

    companion object {
        fun fromDto(dto: Location) =
            LocationEntity(
                dto.id,
                dto.name,
                dto.type,
                dto.dimension,
                dto.residents,
                dto.url,
                dto.created,
            )
    }
}

fun List<LocationEntity>.toDto(): List<Location> = map(LocationEntity::toDto)
fun List<Location>.toEntity(): List<LocationEntity> =
    map(LocationEntity::fromDto)