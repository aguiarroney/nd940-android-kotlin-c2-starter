package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Repository.Repository
import kotlinx.coroutines.launch
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    val repository by lazy { Repository() }


    fun fetchAsteroids() {
        viewModelScope.launch {
            val response = repository.fetchAsteroids("neo/rest/v1/feed?start_date=2021-03-22&end_date=2021-03-29&api_key=${Constants.API_KEY}")
            if(response.isSuccessful){
                val jsonString = response.body()
                val json = JSONObject(jsonString)
                val asteroidListFromService = parseAsteroidsJsonResult(json)
                _asteroidList.value = asteroidListFromService
            }
            else{
                Log.i("erro fetch", "!!!! ${response.body()}")
            }
        }
    }

}