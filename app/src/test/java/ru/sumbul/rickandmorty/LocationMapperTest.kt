package ru.sumbul.rickandmorty

import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity
import ru.sumbul.rickandmorty.locations.data.mapper.LocationMapper

class LocationMapperTest {

    var location = mockk<ru.sumbul.rickandmorty.locations.domain.model.Location>()
    var locationEntity = mockk<LocationEntity>()

    var locations = mockk<List<ru.sumbul.rickandmorty.locations.domain.model.Location>>()
    var locationsEntity = mockk<List<LocationEntity>>()

    private val mapper = LocationMapper()

    @Test
    fun returnLocationFromLocationEntity() {
        location = ru.sumbul.rickandmorty.locations.domain.model.Location(
            1, "Earth",
            "Planet", "Dimention", listOf("1st", "2nd"), "url",
            "created"
        )

        locationEntity = LocationEntity(
            1, "Earth",
            "Planet", "Dimention", listOf("1st", "2nd"), "url",
            "created"
        )

        assertEquals(location, mapper.mapFromEntity(locationEntity))
    }

    @Test
    fun returnLocationEntityFromLocation() {
        location = ru.sumbul.rickandmorty.locations.domain.model.Location(
            1, "Earth",
            "Planet", "Dimention", listOf("1st", "2nd"), "url",
            "created"
        )

        locationEntity = LocationEntity(
            1, "Earth",
            "Planet", "Dimention", listOf("1st", "2nd"), "url",
            "created"
        )

        assertEquals(locationEntity, mapper.mapToEntity(location))
    }

    @Test
    fun returnListOfLocationFromListOfLocationEntity() {
        locations = listOf(
            ru.sumbul.rickandmorty.locations.domain.model.Location(
                1, "Earth",
                "Planet", "Dimention", listOf("1st", "2nd"), "url",
                "created"
            ), ru.sumbul.rickandmorty.locations.domain.model.Location(
                2, "Bepis 9",
                "Planet", "unknown", listOf("1st", "2nd"), "url",
                "2017"
            )
        )

        locationsEntity = listOf(
            LocationEntity(
                1, "Earth",
                "Planet", "Dimention", listOf("1st", "2nd"), "url",
                "created"
            ), LocationEntity(
                2, "Bepis 9",
                "Planet", "unknown", listOf("1st", "2nd"), "url",
                "2017"
            )
        )

        assertEquals(locations, mapper.mapFromListEntity(locationsEntity))
    }

    @Test
    fun returnListOfLocationEntityFromListOfLocation() {
        locations = listOf(
            ru.sumbul.rickandmorty.locations.domain.model.Location(
                1, "Earth",
                "Planet", "Dimention", listOf("1st", "2nd"), "url",
                "created"
            ), ru.sumbul.rickandmorty.locations.domain.model.Location(
                2, "Bepis 9",
                "Planet", "unknown", listOf("1st", "2nd"), "url",
                "2017"
            )
        )

        locationsEntity = listOf(
            LocationEntity(
                1, "Earth",
                "Planet", "Dimention", listOf("1st", "2nd"), "url",
                "created"
            ), LocationEntity(
                2, "Bepis 9",
                "Planet", "unknown", listOf("1st", "2nd"), "url",
                "2017"
            )
        )

        assertEquals(locationsEntity, mapper.mapToListEntity(locations))
    }


}