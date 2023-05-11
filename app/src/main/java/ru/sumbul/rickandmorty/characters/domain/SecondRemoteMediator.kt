package ru.sumbul.rickandmorty.characters.domain

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.characters.data.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.data.entity.FilterEntity
import ru.sumbul.rickandmorty.characters.data.entity.FilteredCharactersEntity
import ru.sumbul.rickandmorty.characters.data.entity.RemoteKeyEntity
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilteredCharactersDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.data.mapper.FilterChMapper
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.domain.model.Character
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SecondRemoteMediator @Inject constructor(
    private val characterDb: CharacterDb,
    private val characterApi: CharacterApi,
    private val filterDao: FilterDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val mapper: CharacterMapper,
    private val filteredCharactersDao: FilteredCharactersDao,
    private val filterChMapper: FilterChMapper,
) : RemoteMediator<Int, FilteredCharactersEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FilteredCharactersEntity>
    ): MediatorResult {

        return try {
            val name: String? = filterDao.getName()
            val status: String? = filterDao.getStatus()
            val species: String? = filterDao.getSpecies()
            val type: String? = filterDao.getType()
            val gender: String? = filterDao.getGender()

            val result = when (loadType) {
                LoadType.REFRESH -> {
                    characterDb.characterDao().getFilteredCharacters(name,
                            status, species, type, gender
                )
                    //       characterDb.filterCharacterDao().getFilteredCharacters(name, status, species, type, gender)

                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val page = remoteKeyDao.getNextPage() ?: return MediatorResult.Success(false)
                    characterDb.characterDao().getFilteredCharacters(
                        name,
                      status, species, type, gender
                    )
                }
            }

//            if (!result.isSuccessful) {
//                MediatorResult.Success(
//                    endOfPaginationReached = true
//                )
//
//            }
//            val body = result.body()
//            if (body == null) {
//                MediatorResult.Success(
//                    endOfPaginationReached = true
//                )
//            }
//
//            if (body != null) {
//                if (body.results.isEmpty()) {
//                    emptyList<ru.sumbul.rickandmorty.characters.domain.model.Character>()
//                    characterDb.characterDao().clearAll()
//                }
//            }
//            var nextPage: Any? = null
//            if (body != null) {
//                if (body.info.next == null) {
//                    nextPage = body.info.pages
//                } else {
//                    nextPage = body.info.next
//                }
//            }
//            val uri = Uri.parse(nextPage.toString())
//            val nextPageQuery = uri?.getQueryParameter("page")
//            val nextPageNumber = nextPageQuery?.toInt()
//
//
//            val characters = body?.results ?: emptyList()
            result
            val characters = mapper.mapCharactersFromDb(result)
            val responseData = mutableListOf<Character>()
            responseData.addAll(characters)

            characterDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDb.filterDao().clear()
                   characterDb.filterCharacterDao().clearAll()
                    characterDb.remoteKeyDao().clear()
               //     characterDb.characterDao().clearAll()
                }

                //    characterDb.remoteKeyDao().insert(RemoteKeyEntity("query", nextPageNumber))
                characterDb.filterDao().upsert(FilterEntity(1, name, status, species, type, gender))
                characterDb.filterCharacterDao().upsertAll(filterChMapper.mapToEntity(responseData))
             //  characterDb.characterDao().upsertAll(mapper.mapToEntity(responseData))

            }

            MediatorResult.Success(
                endOfPaginationReached = responseData.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}