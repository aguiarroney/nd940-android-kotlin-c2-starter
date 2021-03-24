package com.udacity.asteroidradar.Repository

import android.util.Log
import com.squareup.picasso.Downloader
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.Deferred
import retrofit2.Response

class Repository {

    suspend fun fetchAsteroids(url: String) : Response<String>?{
        return try{
            RetrofitInstance.nasaApi.fetchAsteroids(url)
        } catch (e: Exception){
            return null
        }
    }

    suspend fun fetchImgOfTheDay(url: String): Response<String>{
        return RetrofitInstance.nasaApi.fetchImgOfTheDay(url)
    }
}