package ru.sumbul.rickandmorty.characters.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

data class Filter(
    var name: String = "",
    var status: String = "",
    var gender: String = "",
)

@Entity
data class FilterEntity(
    @PrimaryKey
    var name: String = "",
    var status: String = "",
    var gender: String = "",
) {
    fun toDto() = Filter(
        name,
        status,
        gender,
    )

    companion object {
        fun fromDto(dto: Filter) =
            FilterEntity(
                dto.name,
                dto.status,
                dto.gender
            )
    }
}


fun List<FilterEntity>.toDto(): List<Filter> = map(FilterEntity::toDto)
fun List<Filter>.toEntity(): List<FilterEntity?> =
    map(FilterEntity::fromDto)