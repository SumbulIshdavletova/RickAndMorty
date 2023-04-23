package ru.sumbul.rickandmorty.characters.entity

import androidx.room.*
import ru.sumbul.rickandmorty.characters.domain.model.CharacterDomain
import ru.sumbul.rickandmorty.characters.domain.model.CharacterLocation
import ru.sumbul.rickandmorty.characters.domain.model.CharacterOrigin
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
    fun toDto() = CharacterDomain(
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
        fun fromDto(dto:CharacterDomain) =
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
    fun toDto() = CharacterOrigin(name, url)

    companion object {
        fun fromDto(dto: CharacterOrigin) = dto.let {
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
    fun toDto() = CharacterLocation(name, url)

    companion object {
        fun fromDto(dto: CharacterLocation) = dto.let {
            LocationEmbedded(it.name, it.url)
        }
    }
}

fun List<CharacterEntity>.toDto(): List<CharacterDomain> = map(CharacterEntity::toDto)
fun List<CharacterDomain>.toEntity(): List<CharacterEntity> =
    map(CharacterEntity::fromDto)