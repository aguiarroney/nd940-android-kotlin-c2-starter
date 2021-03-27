package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateTimeHelper
import com.udacity.asteroidradar.Repository.Repository
import kotlinx.coroutines.launch
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import com.udacity.asteroidradar.db.AsteroidDataBase.Companion.getInstance

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private val _navigationToDetial = MutableLiveData<Long>()
    val navigationToDetail get() = _navigationToDetial

    private val _picOfTheDayUrl = MutableLiveData<String>()
    val pictureOfDay get() = _picOfTheDayUrl

    val repository:Repository by lazy {
        val database = getInstance(getApplication())
        Repository(database)
    }


    fun fetchAsteroidsOnline() {
        viewModelScope.launch {
            repository.fetchAsteroidsOnline("neo/rest/v1/feed?start_date=${DateTimeHelper.getCurrentDay()}&end_date=${DateTimeHelper.getEndDay()}&api_key=${Constants.API_KEY}")
        }
    }

    fun fetchAsteroidsFromDB(){
        viewModelScope.launch {
            val list = repository.fetchAsterodoisFromDB()
            if(!list.isNullOrEmpty()){
                _asteroidList.value = list
            }
        }
    }

    fun fetchPichOfTheDay() {
        viewModelScope.launch {
            val response =
                repository.fetchImgOfTheDay("planetary/apod?api_key=${Constants.API_KEY}")
            if (response.isSuccessful) {
                val jsonString = response.body()
                val json = JSONObject(jsonString)
                val imgUrl = json["url"]
                _picOfTheDayUrl.value = imgUrl as String?
            } else {
                Log.i("img fetch", "${response.code()}")
            }
        }
    }

    fun onNavigationEnd() {
        _navigationToDetial.value = null
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}