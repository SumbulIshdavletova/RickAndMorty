package ru.sumbul.rickandmorty.characters.domain.model

import androidx.room.ColumnInfo
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import ru.sumbul.rickandmorty.util.StringListTypeConverter
import java.util.*


@kotlinx.serialization.Serializable
data class CharacterDomain(
    val id: Int = 0,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: CharacterOrigin,
    val location: CharacterLocation,
    val image: String,
    @TypeConverters(StringListTypeConverter::class)
    val episode: List<String> = emptyList(),
    val url: String,
    val created: String,
) : java.io.Serializable

data class CharacterOrigin(
    @ColumnInfo(name = "origin_name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "origin_url")
    @SerializedName("url")
    val url: String,
)

data class CharacterLocation(
    @ColumnInfo(name = "location_name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "location_url")
    @SerializedName("url")
    val url: String,
)


