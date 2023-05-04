package ru.sumbul.rickandmorty.characters.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.rxjava3.annotations.NonNull

data class Filter(
    var name: String? = "",
    var status: String? = "",
    var species: String? = "",
    var type: String? = "",
    var gender: String? = "",
)

@Entity
data class FilterEntity(
    @PrimaryKey
    @NonNull
    var name: String = "",
    var status: String? = null,
    var species: String? = null,
    var type: String? =  null,
    var gender: String? =  null,
) {
    fun toDto() = Filter(
        name,
        status, species, type,
        gender,
    )

    companion object {
        fun fromDto(dto: Filter) =
            dto.name?.let {
                FilterEntity(
                    it,
                    dto.status, dto.species, dto.type,
                    dto.gender
                )
            }
    }
}


fun List<FilterEntity>.toDto(): List<Filter> = map(FilterEntity::toDto)
fun List<Filter>.toEntity(): List<FilterEntity?> =
    map(FilterEntity.Companion::fromDto)