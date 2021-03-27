package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateTimeHelper
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Repository.Repository
import kotlinx.coroutines.launch
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import com.udacity.asteroidradar.db.AsteroidDataBase.Companion.getInstance

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    //holds the list of asteroids
    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private val _navigationToDetial = MutableLiveData<Long>()
    val navigationToDetail get() = _navigationToDetial

    //holds the pic of the day
    private val _picOfTheDayUrl = MutableLiveData<String>()
    val pictureOfDay get() = _picOfTheDayUrl

    val repository: Repository by lazy {
        val database = getInstance(getApplication())
        Repository(database)
    }


    private fun _fetchAsteroidsOnline() {
        viewModelScope.launch {
            repository.fetchAsteroidsOnline("neo/rest/v1/feed?start_date=${DateTimeHelper.getCurrentDay()}&end_date=${DateTimeHelper.getEndDay()}&api_key=${Constants.API_KEY}")
        }
    }

    fun fetchAsteroidsFromDB() {
        viewModelScope.launch {
            var list = repository.fetchAsterodoisFromDB()
            if (list.isNullOrEmpty()) {
                _fetchAsteroidsOnline()
            }
            list = repository.fetchAsterodoisFromDB()
            _asteroidList.value = list
        }
    }

    private fun _fetchPicOfTheDay() {
        viewModelScope.launch {
           repository.fetchImgOfTheDayOnline("planetary/apod?api_key=${Constants.API_KEY}")
        }
    }

    fun fetchPicOfTheDayFromDB(){
        viewModelScope.launch {
            var img: PictureOfDay? = repository.fetchImgOfTheDayFromDB()
            Log.i("imagem", "${img}")
            if(img == null){
                _fetchPicOfTheDay()
            }
            img = repository.fetchImgOfTheDayFromDB()
            _picOfTheDayUrl.value = img?.url.toString()
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