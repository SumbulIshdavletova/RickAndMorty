package ru.sumbul.rickandmorty.characters.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//data class RemoteKey(val label: String, val nextPage: Int?)

@Entity
data class RemoteKeyEntity(
    @PrimaryKey
    val label: String, val nextPage: Int?
)
//{
//    fun toDto() = RemoteKey(
//        label, nextPage
//    )
//
//    companion object {
//        fun fromDto(dto: RemoteKey) =
//            RemoteKeyEntity(dto.label, dto.nextPage)
//
//    }
//}
//
//
//fun List<RemoteKeyEntity>.toDto(): List<RemoteKey> = map(RemoteKeyEntity::toDto)
//fun List<RemoteKey>.toEntity(): List<RemoteKeyEntity?> =
//    map(RemoteKeyEntity::fromDto)
