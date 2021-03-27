package com.udacity.asteroidradar.Repository

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateTimeHelper
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDataBase
import com.udacity.asteroidradar.db.AsteroidEntity
import com.udacity.asteroidradar.db.PictureOfTheDayEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class Repository(private val asteroidDataBase: AsteroidDataBase) {


    suspend fun fetchAsteroidsOnline(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val response =
                    RetrofitInstance.nasaApi.fetchAsteroidsOnline("neo/rest/v1/feed?start_date=${DateTimeHelper.getCurrentDay()}&end_date=${DateTimeHelper.getEndDay()}&api_key=${Constants.API_KEY}")
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
                    return@withContext true
                }
                return@withContext false
            }
        } catch (e: Throwable) {
            return false
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

    suspend fun fetchImgOfTheDayOnline(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val response =
                    RetrofitInstance.nasaApi.fetchImgOfTheDay("planetary/apod?api_key=${Constants.API_KEY}")
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
                    Timber.i("inseriu")
                    return@withContext true
                }

                return@withContext false
            }
        } catch (e: Throwable) {
            return false
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

    suspend fun deledOldDataFromDB (){
        withContext(Dispatchers.IO){
            asteroidDataBase.asteroidDao.deleteOldData(DateTimeHelper.getCurrentDay())
        }
    }
}