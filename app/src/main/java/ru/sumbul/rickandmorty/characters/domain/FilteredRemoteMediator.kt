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
import ru.sumbul.rickandmorty.characters.data.entity.RemoteKeyEntity
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.data.mapper.CharacterMapper
import ru.sumbul.rickandmorty.characters.domain.model.*
import ru.sumbul.rickandmorty.error.ApiError
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FilteredRemoteMediator(
    // private val query: String,
    private val characterDb: CharacterDb,
    private val characterApi: CharacterApi,
    private val filterDao: FilterDao,
    private val remoteKeyDao: RemoteKeyDao,
    private val mapper: CharacterMapper,
) : RemoteMediator<Int, CharacterEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {

        return try {
            val name: String = filterDao.getName()
            val status: String = filterDao.getStatus()
            val gender: String = filterDao.getGender()

//            val loadKey = when (loadType) {
//                LoadType.REFRESH -> 1
//                LoadType.PREPEND ->
//                    return MediatorResult.Success(endOfPaginationReached = true)
//                LoadType.APPEND -> {
//                    val remoteKey = characterDb.withTransaction {
//                        //    remoteKeyDao.remoteKeyByQuery(query)
//                        remoteKeyDao.getNextPage()
//                    }
//                        ?: return MediatorResult.Success(
//                            endOfPaginationReached = true
//                        )
////                    if (remoteKey.nextPage == null) {
////                        return MediatorResult.Success(
////                            endOfPaginationReached = true
////                        )
////                    }
//                    remoteKey
//                }
//            }

            val result = when (loadType) {
                LoadType.REFRESH -> {
                    characterApi.getCharacters(
                        1, name, status, gender
                    )

                }
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val page = remoteKeyDao.getNextPage() ?: return MediatorResult.Success(false)
                    characterApi.getCharacters(page, name, status, gender)
                }
            }


            //  val result = characterApi.getCharacters(loadKey, name, status, gender)
            if (!result.isSuccessful) {
                throw ApiError(result.code(), result.message())
            }
            val body = result.body() ?: throw ApiError(
                result.code(),
                result.message(),
            )
            var nextPage: String? = null
            nextPage = body.info.next ?: return MediatorResult.Success(
                endOfPaginationReached = true
            )
            val uri = Uri.parse(nextPage)
            val nextPageQuery = uri.getQueryParameter("page")
            val nextPageNumber = nextPageQuery?.toInt()

            val characters = body.results ?: emptyList()
            val responseData = mutableListOf<Character>()
            responseData.addAll(characters)

            characterDb.filterDao().clear()
            characterDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDb.remoteKeyDao().clear()
                    characterDb.filterDao().clear()
                    characterDb.characterDao().clearAll()
                    characterApi.getCharacters(1, "", "", "")
                }

                //   characterDb.filterDao().clear()

                if (nextPage != null) {
                    characterDb.remoteKeyDao()
                        .insert(RemoteKeyEntity("query", nextPageNumber))
                }

                characterDb.filterDao().clear()
                if (name != null) {
                    if (status != null) {
                        if (gender != null) {
                            characterDb.filterDao().upsert(FilterEntity(name, status, gender))
                        }
                    }
                }

                characterDb.characterDao().upsertAll(mapper.mapCharactersToDb(responseData))
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