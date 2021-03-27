package com.udacity.asteroidradar.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [AsteroidEntity::class, PictureOfTheDayEntity::class], version = 2)
abstract class AsteroidDataBase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDAO
    abstract val pictureOfTheDayDAO: PictureOfTheDayDAO

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDataBase? = null

        fun getInstance(context: Context): AsteroidDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDataBase::class.java,
                        "asteroid_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}