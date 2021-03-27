package com.udacity.asteroidradar.db

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDAO {
    @Insert
    fun insert(asteroid: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroid_table WHERE id = :key")
    fun getAsteroid(key: Long): AsteroidEntity?

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC")
    fun getAllAsteroids(): List<AsteroidEntity>?

    @Query("DELETE FROM asteroid_table")
    fun clear()
}