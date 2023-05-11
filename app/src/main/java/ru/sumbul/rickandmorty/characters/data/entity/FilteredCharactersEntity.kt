package ru.sumbul.rickandmorty.characters.data.entity

import androidx.room.*
import ru.sumbul.rickandmorty.characters.domain.model.Location
import ru.sumbul.rickandmorty.characters.domain.model.Origin
import ru.sumbul.rickandmorty.util.StringListTypeConverter

@Entity
data class FilteredCharactersEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded
    val origin: OriginEmbedded2,
    @Embedded
    val location: LocationEmbedded2,
    val image: String,
    @field:TypeConverters(StringListTypeConverter::class)
    val episode: List<String>,
    val url: String,
    val created: String,
) {

}

data class OriginEmbedded2(
    @ColumnInfo(name = "origin_name_2")
    val name: String,

    @ColumnInfo(name = "origin_url_2")
    val url: String,
)
{
    fun toDto() = Origin(name, url)

    companion object {
        fun fromDto(dto: Origin) = dto.let {
            OriginEmbedded2(it.name, it.url)
        }
    }
}

data class LocationEmbedded2(
    @ColumnInfo(name = "location_name_2")
    val name: String,

    @ColumnInfo(name = "location_url_2")
    val url: String,
)
{
    fun toDto() = Location(name, url)

    companion object {
        fun fromDto(dto: Location) = dto.let {
            LocationEmbedded2(it.name, it.url)
        }
    }
}

