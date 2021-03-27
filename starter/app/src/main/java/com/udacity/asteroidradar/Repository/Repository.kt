package com.udacity.asteroidradar.Repository

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDataBase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.PictureOfTheDayEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Repository(private val asteroidDataBase: AsteroidDataBase) {


    suspend fun fetchAsteroidsOnline(url: String) {
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.nasaApi.fetchAsteroidsOnline(url)
            if (response.isSuccessful) {
                val jsonString = response.body()
                val json = JSONObject(jsonString)
                val asteroidListFromService = parseAsteroidsJsonResult(json)
                asteroidDataBase.asteroidDao.insert(asteroidListFromService.map {
                    AsteroidEntity(
                        id = it.id,
                        codename = it.codename,
                        closeApproachDate = it.closeApproachDate,
                        absoluteMagnitude = it.absoluteMagnitude,
                        estimatedDiameter = it.estimatedDiameter,
                        relativeVelocity = it.relativeVelocity,
                        distanceFromEarth = it.distanceFromEarth,
                        isPotentiallyHazardous = it.isPotentiallyHazardous
                    )
                })
                Log.i("DATABASE", "Inseriu no DB")
            }
        }

    }

    suspend fun fetchAsterodoisFromDB(): List<Asteroid>? {
        return withContext(Dispatchers.IO) {
            asteroidDataBase.asteroidDao.getAllAsteroids()?.map {
                Asteroid(
                    id = it.id,
                    codename = it.codename,
                    closeApproachDate = it.closeApproachDate,
                    absoluteMagnitude = it.absoluteMagnitude,
                    estimatedDiameter = it.estimatedDiameter,
                    relativeVelocity = it.relativeVelocity,
                    distanceFromEarth = it.distanceFromEarth,
                    isPotentiallyHazardous = it.isPotentiallyHazardous
                )
            }
        }
    }

    suspend fun fetchImgOfTheDayOnline(url: String) {
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.nasaApi.fetchImgOfTheDay(url)
            if (response.isSuccessful) {
                val jsonString = response.body()
                val json = JSONObject(jsonString)
                val pic = PictureOfTheDayEntity(
                    1,
                    json["media_type"].toString(),
                    json["title"].toString(),
                    json["url"].toString()
                )
                asteroidDataBase.pictureOfTheDayDAO.insert(pic)
                Log.i("img fetch", "inseriu")
            } else {
                Log.i("img fetch", "${response.code()}")
            }
        }
    }

    suspend fun fetchImgOfTheDayFromDB(): PictureOfDay? {
        return withContext(Dispatchers.IO) {
            val picEntity: PictureOfTheDayEntity =
                asteroidDataBase.pictureOfTheDayDAO.getPicFromDB()
                    ?: return@withContext null
            return@withContext PictureOfDay(picEntity.mediaType, picEntity.title, picEntity.url)
        }
    }
}