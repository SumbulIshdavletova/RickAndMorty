package ru.sumbul.rickandmorty

import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import ru.sumbul.rickandmorty.episodes.data.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.data.mapper.EpisodeMapper
import ru.sumbul.rickandmorty.episodes.domain.model.Episode

class EpisodeMapperTest {

    var episode = mockk<Episode>()
    var episodeEntity = mockk<EpisodeEntity>()
    var episodes = mockk<List<Episode>>()
    var episodesEntity = mockk<List<EpisodeEntity>>()

    private val mapper = EpisodeMapper()

    @Test
    fun returnEpisodeFromEpisodeEntity() {
        episode = Episode(1, "Pilot", "01jan", "S01E04",
            listOf("1st","2nd"), "url", "02feb")

        episodeEntity = EpisodeEntity(1, "Pilot", "01jan", "S01E04",
            listOf("1st","2nd"), "url", "02feb")

        assertEquals(episode, mapper.mapToDb(episodeEntity))
    }


    @Test
    fun returnEpisodeEntityFromEpisode() {
        episode = Episode(1, "Pilot", "01jan", "S01E01",
            listOf("1st","2nd"), "url", "02feb")

        episodeEntity = EpisodeEntity(1, "Pilot", "01jan", "S01E01",
            listOf("1st","2nd"), "url", "02feb")

        assertEquals(episodeEntity, mapper.mapFromDb(episode))
    }

    @Test
    fun returnListOfEpisodeEntityFromListOfEpisode() {
        episodes = listOf(Episode(1, "Pilot", "01jan", "S01E04",
            emptyList(), "url", "02feb"), Episode(2, "AnatomyPark","16 Dec",
            "S01E03", listOf("1st","2nd"), "url", "02feb"))

        episodesEntity = listOf(EpisodeEntity(1, "Pilot", "01jan", "S01E04",
            emptyList(), "url", "02feb"), EpisodeEntity(2, "AnatomyPark","16 Dec",
            "S01E03", listOf("1st","2nd"), "url", "02feb"))

        assertEquals(episodesEntity, mapper.mapToEntity(episodes))
    }

    @Test
    fun returnListOfEpisodeFromListOfEpisodeEntity() {
        episodes = listOf(Episode(1, "Pilot", "01jan", "S01E04",
            emptyList(), "url", "02feb"), Episode(2, "AnatomyPark","16 Dec",
            "S01E03", emptyList(), "url", "02feb"))

        episodesEntity = listOf(EpisodeEntity(1, "Pilot", "01jan", "S01E04",
            emptyList(), "url", "02feb") ,EpisodeEntity(2, "AnatomyPark","16 Dec",
            "S01E03", emptyList(), "url", "02feb"))

        assertEquals(episodes, mapper.mapFromEntity(episodesEntity))
    }


}