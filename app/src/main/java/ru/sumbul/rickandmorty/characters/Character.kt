package ru.sumbul.rickandmorty.characters

import android.location.Location
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
   val origin: Origin,
    val location: ru.sumbul.rickandmorty.characters.Location,
    val image: String,
  // val episode: List<String>,
    val url: String,
    val created: String,
)

data class Origin(
    @ColumnInfo(name = "origin_name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "origin_url")
    @SerializedName("url")
    val url: String,
)

data class Location(
    @ColumnInfo(name = "location_name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "location_url")
    @SerializedName("url")
    val url: String,
)
