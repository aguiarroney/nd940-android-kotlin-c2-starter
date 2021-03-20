package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import retrofit2.Response

interface NasaApi {

    suspend fun fetchAsteroids(): Response<List<Asteroid>>
}