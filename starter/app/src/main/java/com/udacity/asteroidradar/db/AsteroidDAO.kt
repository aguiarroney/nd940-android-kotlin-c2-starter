package com.udacity.asteroidradar.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDAO {
    @Insert
    fun insert(asteroid: AsteroidEntity)

    @Query("SELECT * FROM asteroid_table WHERE id = :key")
    fun getAsteroid(key: Long): AsteroidEntity?
}