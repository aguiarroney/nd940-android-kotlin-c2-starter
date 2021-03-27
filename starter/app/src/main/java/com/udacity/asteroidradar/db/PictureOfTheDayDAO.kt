package com.udacity.asteroidradar.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface PictureOfTheDayDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pic: PictureOfTheDayEntity)

    @Query("SELECT * FROM picture_of_the_day_table ORDER BY id DESC LIMIT 1")
    fun getPicFromDB(): PictureOfTheDayEntity?

    @Query("DELETE FROM picture_of_the_day_table")
    fun clear()
}