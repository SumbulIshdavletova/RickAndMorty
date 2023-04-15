package ru.sumbul.rickandmorty.characters.entity

import androidx.room.*
import ru.sumbul.rickandmorty.util.StringListTypeConverter

@Entity
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded
    val origin: OriginEmbedded,
    @Embedded
    val location: LocationEmbedded,
    val image: String,
    @field:TypeConverters(StringListTypeConverter::class)
    val episode: List<String>,
    val url: String,
    val created: String,
) {
    fun toDto() = ru.sumbul.rickandmorty.characters.entity.Character(
        id,
        name,
        status,
        species,
        type,
        gender,
        origin.toDto(),
        location.toDto(),
        image,
        episode,
        url,
        created
    )

    companion object {
        fun fromDto(dto: ru.sumbul.rickandmorty.characters.entity.Character) =
            CharacterEntity(
                dto.id,
                dto.name,
                dto.status,
                dto.species,
                dto.type,
                dto.gender,
                OriginEmbedded.fromDto(dto.origin),
                LocationEmbedded.fromDto(dto.location),
                dto.image,
                dto.episode,
                dto.url,
                dto.created,
            )
    }
}

data class OriginEmbedded(
    @ColumnInfo(name = "origin_name")
    val name: String,

    @ColumnInfo(name = "origin_url")
    val url: String,
) {
    fun toDto() = Origin(name, url)

    companion object {
        fun fromDto(dto: Origin) = dto.let {
            OriginEmbedded(it.name, it.url)
        }
    }
}

data class LocationEmbedded(
    @ColumnInfo(name = "location_name")
    val name: String,

    @ColumnInfo(name = "location_url")
    val url: String,
) {
    fun toDto() = Location(name, url)

    companion object {
        fun fromDto(dto: Location) = dto.let {
            LocationEmbedded(it.name, it.url)
        }
    }
}

fun List<CharacterEntity>.toDto(): List<Character> = map(CharacterEntity::toDto)
fun List<ru.sumbul.rickandmorty.characters.entity.Character>.toEntity(): List<CharacterEntity> =
    map(CharacterEntity::fromDto)