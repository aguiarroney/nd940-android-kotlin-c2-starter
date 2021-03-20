package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private val listTeste = ArrayList<Asteroid>()

}