package ru.sumbul.rickandmorty.characters

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.characters.api.CharacterApi
import ru.sumbul.rickandmorty.characters.db.CharacterDb
import ru.sumbul.rickandmorty.characters.domain.model.CharacterDomain
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.entity.toEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val characterDb: CharacterDb,
    private val characterApi: CharacterApi,
) : RemoteMediator<Int, CharacterEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val body = characterApi.getCharacters(
                page = loadKey
            )
            val info = body.body()?.info
            val characters = body.body()?.results ?: emptyList()
            val responseData = mutableListOf<CharacterDomain>()
            responseData.addAll(characters)

            val nextPage = info?.next
            val prevPage = info?.prev

            characterDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDb.characterDao().clearAll()
                }
                characterDb.characterDao().upsertAll(responseData.toEntity())
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