package ru.sumbul.rickandmorty.characters.domain
//
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import androidx.room.withTransaction
//import retrofit2.HttpException
//import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
//import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
//import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
//import ru.sumbul.rickandmorty.characters.data.local.db.RemoteKeyMediatorDb
//import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
//import ru.sumbul.rickandmorty.characters.entity.RemoteKeyMediatorEntity
//import ru.sumbul.rickandmorty.characters.entity.toEntity
//import java.io.IOException
//
//@OptIn(ExperimentalPagingApi::class)
//class CharacterRemoteMediator(
//    private val characterDb: CharacterDb,
//    private val characterApi: CharacterApi,
//    private val remoteKeyDao: FilterDao,
//    //  private val remoteKeyMediatorDb: RemoteKeyMediatorDb
//) : RemoteMediator<Int, CharacterEntity>() {
//
//    // get filtered characters
//    // 2 functions or 2 remote mediators
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, CharacterEntity>
//    ): MediatorResult {
//        return try {
//
//            val loadKey = when (loadType) {
//                LoadType.REFRESH -> 1
//                LoadType.PREPEND -> return MediatorResult.Success(
//                    endOfPaginationReached = true
//                )
//                LoadType.APPEND -> {
////                //    val lastItem = remoteKeyDao.min() ?: return MediatorResult.Success(false)
////                    if (lastItem == null) {
////                        1
////                    } else {
////                        lastItem
////                    }
//                    val lastItem = state.lastItemOrNull()
//                    if (lastItem == null) {
//                        1
//                    } else {
//                        (lastItem.id / state.config.pageSize) + 1
//                    }
//                }
//            }
//
//            val body = characterApi.getCharacters(
//                    page = loadKey
//                )
//
//
//            val info = body.body()?.info
//            val characters = body.body()?.results ?: emptyList()
//            val responseData = mutableListOf<ru.sumbul.rickandmorty.characters.entity.Character>()
//            responseData.addAll(characters)
//
//            val nextPage = info?.next.toString().substringAfterLast("/", "0")
//            val prevPage = info?.prev
//
//            characterDb.withTransaction {
//                when (loadType) {
//                    LoadType.REFRESH -> {
//                        remoteKeyDao.upsert(RemoteKeyMediatorEntity(nextPage))
//                        characterDb.characterDao().clearAll()
//                    }
//                    LoadType.APPEND -> {
//                        remoteKeyDao.upsert(RemoteKeyMediatorEntity(nextPage))
//                    }
//                    else -> {}
//                }
//                characterDb.characterDao().upsertAll(responseData.toEntity())
//            }
//            MediatorResult.Success(
//                endOfPaginationReached = responseData.isEmpty()
//            )
//        } catch (e: IOException) {
//            MediatorResult.Error(e)
//        } catch (e: HttpException) {
//            MediatorResult.Error(e)
//        }
//    }
//}