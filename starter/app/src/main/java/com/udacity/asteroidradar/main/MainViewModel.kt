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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    //holds the list of asteroids
    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private val _navigationToDetial = MutableLiveData<Long>()
    val navigationToDetail get() = _navigationToDetial

    //holds the pic of the day
    private val _picOfTheDayUrl = MutableLiveData<String>()
    val pictureOfDay get() = _picOfTheDayUrl

    // holds the status of a online fetching
    private val _fetchingAsteroidOnlineStatus = MutableLiveData<Boolean>()
    val fetchingAsteroidOnlineStatus get() = _fetchingAsteroidOnlineStatus

    // holds the status of a picture of the day online fetching
    private val _fetchingPicOnlineStatus = MutableLiveData<Boolean>()
    val fetchingPicOnlineStatus get() = _fetchingPicOnlineStatus

    val repository: Repository by lazy {
        val database = getInstance(getApplication())
        Repository(database)
    }


    private suspend fun _fetchAsteroidsOnline(): Boolean {
        return withContext(Dispatchers.Main) {
            repository.fetchAsteroidsOnline()
        }
    }

    fun fetchAsteroidsFromDB() {
        viewModelScope.launch {
            val list = repository.fetchAsterodoisFromDB()
            if (list.isNullOrEmpty()) {
                _fetchingAsteroidOnlineStatus.value = _fetchAsteroidsOnline()
            } else {
                _asteroidList.value = list
            }
        }
    }

    private suspend fun _fetchPicOfTheDay(): Boolean {
        return withContext(Dispatchers.Main) {
            repository.fetchImgOfTheDayOnline()
        }
    }

    fun fetchPicOfTheDayFromDB() {
        viewModelScope.launch {
            var img: PictureOfDay? = repository.fetchImgOfTheDayFromDB()
            if (img == null) {
                _fetchingPicOnlineStatus.value = _fetchPicOfTheDay()
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