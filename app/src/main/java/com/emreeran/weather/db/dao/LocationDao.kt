package com.emreeran.weather.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.emreeran.weather.db.entity.Location
import com.emreeran.weather.db.entity.LocationQuery
import com.emreeran.weather.vo.LocationQueryWithLocations

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Dao
abstract class LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(location: Location)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertIfNotExists(location: Location)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertQuery(query: LocationQuery)

    /**
     * Replacing location will update created at value
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(locations: List<Location>)

    @Query("SELECT * FROM locations WHERE woeId = :id")
    abstract fun findLocationByIdSync(id: Int): Location

    @Query("SELECT * FROM locations WHERE woeId IN (:ids)")
    abstract fun listLocationsByIds(ids: List<Int>): List<Location>

    @Query("SELECT * FROM locations ORDER BY distance ASC LIMIT 1")
    abstract fun getNearestLocationAsLiveData(): LiveData<Location>

    @Query("SELECT * FROM location_query WHERE queryString = :queryString")
    abstract fun findLocationQuery(queryString: String): LocationQuery?

    @Transaction
    open fun listQueryLocations(queryString: String): LocationQueryWithLocations {
        val query = findLocationQuery(queryString)

        query?.let {
            return LocationQueryWithLocations(query, listLocationsByIds(it.locationIds))
        }

        return LocationQueryWithLocations(query)
    }
}