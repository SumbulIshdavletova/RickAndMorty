package ru.sumbul.rickandmorty.Pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.sumbul.rickandmorty.api.CharacterApi
import ru.sumbul.rickandmorty.characters.CharacterDb
import ru.sumbul.rickandmorty.characters.CharacterEntity
import ru.sumbul.rickandmorty.characters.toEntity
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
            val characters = characterApi.getCharacters(
                page = loadKey
            )
            characterDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDb.characterDao().clearAll()
                }
                characterDb.characterDao().upsertAll(characters.toEntity())
            }
            MediatorResult.Success(
                endOfPaginationReached = characters.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}