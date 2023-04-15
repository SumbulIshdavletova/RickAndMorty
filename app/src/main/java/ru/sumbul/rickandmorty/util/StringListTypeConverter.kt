package ru.sumbul.rickandmorty.util

import androidx.room.TypeConverter

class StringListTypeConverter {

    @TypeConverter
    fun fromString(string: String?): List<String>? {
        return string?.split(",")
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}