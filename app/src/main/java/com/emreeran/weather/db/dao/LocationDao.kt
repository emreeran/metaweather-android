package com.emreeran.weather.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emreeran.weather.db.entity.Location

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: Location)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIfNotExists(location: Location)

    /**
     * Replacing location will update created at value
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<Location>)

    @Query("SELECT * FROM locations ORDER BY distance ASC LIMIT 1")
    fun getNearestLocationAsLiveData(): LiveData<Location>
}