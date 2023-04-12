package ru.sumbul.rickandmorty.characters

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val origin: NameUrlEmbedded,
    @Embedded(prefix = "prefix_")
    val location: NameUrlEmbedded,
    val image: String,
 //   val episode: List<String>,
    val url: String,
    val created: String,
) {
    fun toDto() = ru.sumbul.rickandmorty.characters.Character(
        id,
        name,
        status,
        species,
        type,
        gender,
        origin.toDto(),
        location.toDto(),
        image,
   //     episode,
        url,
        created
    )

    companion object {
        fun fromDto(dto: ru.sumbul.rickandmorty.characters.Character) =
            CharacterEntity(
                dto.id,
                dto.name,
                dto.status,
                dto.species,
                dto.type,
                dto.gender,
                NameUrlEmbedded.fromDto(dto.origin),
                NameUrlEmbedded.fromDto(dto.location),
                dto.image,
      //          dto.episode,
                dto.url,
                dto.created,

                )
    }
}

data class NameUrlEmbedded(
    val name1: String,
    val url2: String,
) {
    fun toDto() = NameUrl(name1, url2)

    companion object {
        fun fromDto(dto: NameUrl) = dto.let {
            NameUrlEmbedded(it.name1, it.url2)
        }
    }
}

fun List<CharacterEntity>.toDto(): List<Character> = map(CharacterEntity::toDto)
fun List<ru.sumbul.rickandmorty.characters.Character>.toEntity(): List<CharacterEntity> =
    map(CharacterEntity::fromDto)