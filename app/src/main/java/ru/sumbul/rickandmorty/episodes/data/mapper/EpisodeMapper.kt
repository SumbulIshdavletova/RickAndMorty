package ru.sumbul.rickandmorty.episodes.data.mapper

import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.domain.model.Episode
import javax.inject.Inject

class EpisodeMapper @Inject constructor() {

    fun mapToDb(episodeEntity: EpisodeEntity): Episode {
        return Episode(
            id = episodeEntity.id,
            name = episodeEntity.name,
            air_date = episodeEntity.air_date,
            episode = episodeEntity.episode,
            characters = episodeEntity.characters,
            url = episodeEntity.url,
            created = episodeEntity.created
        )
    }


    fun mapFromDb(dto: Episode) =
        EpisodeEntity(
            dto.id,
            dto.name,
            dto.air_date,
            dto.episode,
            dto.characters,
            dto.url,
            dto.created,
        )

    fun mapFromEntity(episodes: List<EpisodeEntity>): List<Episode> {
        return episodes.map {
            Episode(
                id = it.id,
                name = it.name,
                air_date = it.air_date,
                episode = it.episode,
                characters = it.characters,
                url = it.url,
                created = it.created
            )
        }
    }

    fun mapToEntity(episodes: List<Episode>): List<EpisodeEntity> {
        return episodes.map {
            EpisodeEntity(
                id = it.id,
                name = it.name,
                air_date = it.air_date,
                episode = it.episode,
                characters = it.characters,
                url = it.url,
                created = it.created
            )
        }
    }
}