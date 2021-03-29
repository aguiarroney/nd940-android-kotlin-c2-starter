package com.udacity.asteroidradar.db

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroid_table WHERE id = :key")
    fun getAsteroid(key: Long): AsteroidEntity?

    @Query("SELECT * FROM asteroid_table ORDER BY date(close_approach_date) ASC")
    fun getAllAsteroids(): List<AsteroidEntity>?

    @Query("DELETE FROM asteroid_table")
    fun clear()

    @Query("DELETE FROM asteroid_table WHERE date(close_approach_date) < date(:today)")
    fun deleteOldData(today: String)

    @Query("SELECT * FROM asteroid_table WHERE date(close_approach_date) = date(:today) ORDER BY date(close_approach_date) ASC")
    fun getTodayAsteroids(today: String): List<AsteroidEntity>?

    @Query("SELECT * FROM asteroid_table WHERE date(:today) <= date(close_approach_date) <= date(:lastDay) ORDER BY date(close_approach_date) ASC")
    fun getWeekAsteroids(today: String, lastDay: String): List<AsteroidEntity>?
}