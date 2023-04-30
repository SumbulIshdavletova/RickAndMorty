package ru.sumbul.rickandmorty.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.episodes.dao.EpisodeDao
import ru.sumbul.rickandmorty.episodes.entity.Episode
import ru.sumbul.rickandmorty.episodes.entity.EpisodeEntity
import ru.sumbul.rickandmorty.episodes.entity.toDto
import ru.sumbul.rickandmorty.episodes.entity.toEntity
import ru.sumbul.rickandmorty.error.ApiError
import ru.sumbul.rickandmorty.error.NetworkError
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterDetailRepository @Inject constructor(

    val api: CharacterApi,
    private val episodeDao: EpisodeDao
) : Repository {
    override val data: LiveData<List<Episode>> = episodeDao.getAll().map(List<EpisodeEntity>::toDto)

    val episode: MutableList<Episode> = mutableListOf()


    var ids: MutableList<Int> = mutableListOf()

    override suspend fun getEpisodes(urls: List<String>) {
        for (url in urls) {
            var result: String = url.substringAfterLast("/", "0")
            ids.add(result.toInt())
        }
        val check = ids.toString()
        try {
            val response = api.getEpisodes(ids.toString())
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body =
                response.body()?.toEntity() ?: throw ApiError(response.code(), response.message())
          //  episode.addAll(body::toDto)
            episodeDao.upsertAll(body)

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw NetworkError
        }
    }

}