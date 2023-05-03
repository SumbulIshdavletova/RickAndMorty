package ru.sumbul.rickandmorty.locations.data.mapper

import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.domain.model.Location
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapFromEntity(locationEntity: LocationEntity) = Location(
        locationEntity.id,
        locationEntity.name,
        locationEntity.type,
        locationEntity.dimension,
        locationEntity.residents,
        locationEntity.url,
        locationEntity.created
    )


    fun mapToEntity(dto: Location) =
        LocationEntity(
            dto.id,
            dto.name,
            dto.type,
            dto.dimension,
            dto.residents,
            dto.url,
            dto.created,
        )

    fun mapFromListEntity(locations: List<LocationEntity>): List<Location> {
        return locations.map {
            Location(
                it.id,
                it.name,
                it.type,
                it.dimension,
                it.residents,
                it.url,
                it.created,
            )
        }
    }

    fun mapToListEntity(locations: List<Location>): List<LocationEntity> {
        return locations.map {
            LocationEntity(
                it.id,
                it.name,
                it.type,
                it.dimension,
                it.residents,
                it.url,
                it.created,
            )
        }
    }
}