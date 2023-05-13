package ru.sumbul.rickandmorty

import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.entity.LocationEmbedded
import ru.sumbul.rickandmorty.characters.data.entity.OriginEmbedded
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.model.Location
import ru.sumbul.rickandmorty.characters.domain.model.Origin

class CharacterMapperTest {

    var charactersEntity = mockk<List<CharacterEntity>>()
    var characterEntity = mockk<CharacterEntity>()
    var character = mockk<ru.sumbul.rickandmorty.characters.domain.model.Character>()
    var characters = mockk<List<ru.sumbul.rickandmorty.characters.domain.model.Character>>()

    private val mapper = CharacterMapper()

    @Test
    fun returnListOfCharactersFromListOfCharacterEntity() {
        charactersEntity = listOf(
            CharacterEntity(1,"Rick","alive", "human",
                "unknown", "male", OriginEmbedded("Earth","url"),
                LocationEmbedded("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ),  CharacterEntity(3,"Morty","alive", "human",
                "unknown", "male", OriginEmbedded("Earth","url"),
                LocationEmbedded("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ))

        characters = listOf(
            ru.sumbul.rickandmorty.characters.domain.model.Character(1,"Rick","alive", "human",
                "unknown", "male", Origin("Earth","url"),
                Location("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ),  ru.sumbul.rickandmorty.characters.domain.model.Character(3,"Morty","alive", "human",
                "unknown", "male", Origin("Earth","url"),
                Location("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ))

        assertEquals(characters, mapper.mapCharactersFromDb(charactersEntity))
    }

    @Test
    fun returnListOfCharactersEntityFromListOfCharacters() {
        charactersEntity = listOf(
            CharacterEntity(1,"Rick","alive", "human",
                "unknown", "male", OriginEmbedded("Earth","url"),
                LocationEmbedded("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ),  CharacterEntity(3,"Morty","alive", "human",
                "unknown", "male", OriginEmbedded("Earth","url"),
                LocationEmbedded("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ))

        characters = listOf(
            ru.sumbul.rickandmorty.characters.domain.model.Character(1,"Rick","alive", "human",
                "unknown", "male", Origin("Earth","url"),
                Location("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ),  ru.sumbul.rickandmorty.characters.domain.model.Character(3,"Morty","alive", "human",
                "unknown", "male", Origin("Earth","url"),
                Location("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            ))

        assertEquals(charactersEntity, mapper.mapToEntity(characters))
    }

    @Test
    fun returnCharacterEntityFromCharacter() {
        characterEntity =  CharacterEntity(1,"Rick","alive", "human",
                "unknown", "male", OriginEmbedded("Earth","url"),
                LocationEmbedded("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            )

        character = ru.sumbul.rickandmorty.characters.domain.model.Character(1,"Rick","alive", "human",
                "unknown", "male", Origin("Earth","url"),
                Location("Earth", "Earth-2"), "url",
                listOf("1st","2nd"), "url","created"
            )

        assertEquals(characterEntity, mapper.mapFromDb(character))
    }

    @Test
    fun returnCharacterFromCharacterEntity() {
        characterEntity =  CharacterEntity(1,"Rick","alive", "human",
            "unknown", "male", OriginEmbedded("Earth","url"),
            LocationEmbedded("Earth", "Earth-2"), "url",
            listOf("1st","2nd"), "url","created"
        )

        character = ru.sumbul.rickandmorty.characters.domain.model.Character(1,"Rick","alive", "human",
            "unknown", "male", Origin("Earth","url"),
            Location("Earth", "Earth-2"), "url",
            listOf("1st","2nd"), "url","created"
        )

        assertEquals(character, mapper.mapToDb(characterEntity))
    }


}