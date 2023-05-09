package ru.sumbul.rickandmorty.locations.data.local.dao


import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.reactivex.rxjava3.core.Single
import ru.sumbul.rickandmorty.locations.data.entity.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM LocationEntity")
    fun pagingSource(): PagingSource<Int, LocationEntity>

    @Upsert
    suspend fun upsert(location: LocationEntity)

    @Upsert
    suspend fun upsertAll(location: List<LocationEntity>)

    @Query("DELETE FROM LocationEntity")
    suspend fun clearAll()

    @Query("SELECT * FROM LocationEntity")
    fun getAll(): LiveData<List<LocationEntity>>

    @Query("SELECT * FROM LocationEntity WHERE (:name IS NULL OR name LIKE '%' || :name || '%') " +
            "AND (:type IS NULL OR type LIKE '%' || :type || '%')" +
            "And (:dimension IS NULL OR dimension LIKE '%' || :dimension || '%')")
    fun getFilteredLocations (name: String?, type: String?, dimension: String?): List<LocationEntity>

    @Query("SELECT * FROM LocationEntity WHERE id = :id")
    fun getLocationById(id: Int): LocationEntity

}