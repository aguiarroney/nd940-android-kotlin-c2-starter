package com.udacity.asteroidradar.Repository

import com.squareup.picasso.Downloader
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.Deferred
import retrofit2.Response

class Repository {

    suspend fun fetchAsteroids(url: String) : Response<String>{
        return RetrofitInstance.nasaApi.fetchAsteroids(url)
    }

//    suspend fun fetchImgOfTheDay(): Response<>
}