package ru.sumbul.rickandmorty.characters.data.mapper

import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.entity.LocationEmbedded
import ru.sumbul.rickandmorty.characters.data.entity.OriginEmbedded
import ru.sumbul.rickandmorty.characters.domain.model.Character
import ru.sumbul.rickandmorty.characters.domain.model.Location
import ru.sumbul.rickandmorty.characters.domain.model.Origin
import javax.inject.Inject

class CharacterMapper @Inject constructor() {

    fun mapToDb(characterEntity: CharacterEntity): ru.sumbul.rickandmorty.characters.domain.model.Character {
        return ru.sumbul.rickandmorty.characters.domain.model.Character(
            id = characterEntity.id,
            name = characterEntity.name,
            status = characterEntity.status,
            image = characterEntity.image,
            species = characterEntity.species,
            gender = characterEntity.gender,
            type = characterEntity.type,
            created = characterEntity.created,
            origin = Origin(name = characterEntity.origin.name, url = characterEntity.origin.url),
            location = Location(
                name = characterEntity.location.name,
                url = characterEntity.location.url
            ),
            episode = characterEntity.episode,
            url = characterEntity.url
        )
    }


    fun mapFromDb(dto: ru.sumbul.rickandmorty.characters.domain.model.Character) =
        CharacterEntity(
            dto.id,
            dto.name,
            dto.status,
            dto.species,
            dto.type,
            dto.gender,
            OriginEmbedded.fromDto(dto.origin),
            LocationEmbedded.fromDto(dto.location),
            dto.image,
            dto.episode,
            dto.url,
            dto.created,
        )

    //toDto
    fun mapCharactersFromDb(characters: List<CharacterEntity>): List<ru.sumbul.rickandmorty.characters.domain.model.Character> {
        return characters.map {
            Character(
                id = it.id,
                name = it.name,
                status = it.status,
                image = it.image,
                species = it.species,
                gender = it.gender,
                type = it.type,
                created = it.created,
                origin = Origin(name = it.origin.name, url = it.origin.url),
                location = Location(
                    name = it.location.name,
                    url = it.location.url
                ),
                episode = it.episode,
                url = it.url
            )
        }
    }

    fun mapToEntity(characters: List<ru.sumbul.rickandmorty.characters.domain.model.Character>): List<CharacterEntity> {
        return characters.map {
            CharacterEntity(
                id = it.id,
                name = it.name,
                status = it.status,
                image = it.image,
                species = it.species,
                gender = it.gender,
                type = it.type,
                created = it.created,
                origin = OriginEmbedded(name = it.origin.name, url = it.origin.url),
                location = LocationEmbedded(
                    name = it.location.name,
                    url = it.location.url
                ),
                episode = it.episode,
                url = it.url
            )
        }
    }
}