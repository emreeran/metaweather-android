package com.emreeran.weather.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.emreeran.weather.db.entity.Location

/**
 * Created by Emre Eran on 2.08.2018.
 */
@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(location: Location)
}