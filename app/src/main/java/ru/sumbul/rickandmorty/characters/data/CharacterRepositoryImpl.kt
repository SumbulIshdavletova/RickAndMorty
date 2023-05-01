package ru.sumbul.rickandmorty.characters.data

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.sumbul.rickandmorty.characters.domain.FilteredRemoteMediator
import ru.sumbul.rickandmorty.characters.data.remote.CharacterApi
import ru.sumbul.rickandmorty.characters.data.local.dao.CharacterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.FilterDao
import ru.sumbul.rickandmorty.characters.data.local.dao.RemoteKeyDao
import ru.sumbul.rickandmorty.characters.data.local.db.CharacterDb
import ru.sumbul.rickandmorty.characters.domain.CharacterRepository
import ru.sumbul.rickandmorty.characters.entity.CharacterEntity
import ru.sumbul.rickandmorty.characters.entity.Filter
import ru.sumbul.rickandmorty.characters.entity.FilterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepositoryImpl @Inject constructor(
    private val dao: CharacterDao,
    private val api: CharacterApi,
    private val filterDao: FilterDao,
    private val appDb: CharacterDb,
    private val remoteKeyDao: RemoteKeyDao
) : CharacterRepository {


    @OptIn(ExperimentalPagingApi::class)
    override val characterPagingFlow: Flow<PagingData<ru.sumbul.rickandmorty.characters.entity.Character>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = FilteredRemoteMediator(appDb, api, filterDao, remoteKeyDao),
            pagingSourceFactory = dao::getPagingSource,
        ).flow.map {
            it.map(CharacterEntity::toDto)
        }

//    override suspend fun filterCharacters(name: String, status: String, gender: String) {
//        val body: FilterEntity = FilterEntity(name, status, gender)
//        try {
//            filterDao.upsert(body)
//        } catch (e: Exception) {
//            throw e
//        }
//    }
}