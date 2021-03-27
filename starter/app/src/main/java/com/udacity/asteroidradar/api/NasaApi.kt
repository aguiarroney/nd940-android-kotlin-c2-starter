package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NasaApi {
    @GET
    suspend fun fetchAsteroidsOnline(@Url url: String): Response<String>

    @GET
    suspend fun fetchImgOfTheDay(@Url url: String): Response<String>
}