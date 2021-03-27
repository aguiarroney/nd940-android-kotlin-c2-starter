package com.udacity.asteroidradar.Repository

import android.util.Log
import com.squareup.picasso.Downloader
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDataBase
import com.udacity.asteroidradar.db.AsteroidEntity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

class Repository(private val asteroidDataBase: AsteroidDataBase) {


    suspend fun fetchAsteroidsOnline(url: String) {
        withContext(Dispatchers.IO) {
            val response = RetrofitInstance.nasaApi.fetchAsteroidsOnline(url)
            if (response.isSuccessful) {
                val jsonString = response.body()
                val json = JSONObject(jsonString)
                val asteroidListFromService = parseAsteroidsJsonResult(json)
                asteroidDataBase.asteroidDao.clear()
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

    suspend fun fetchImgOfTheDay(url: String): Response<String> {
        return RetrofitInstance.nasaApi.fetchImgOfTheDay(url)
    }
}