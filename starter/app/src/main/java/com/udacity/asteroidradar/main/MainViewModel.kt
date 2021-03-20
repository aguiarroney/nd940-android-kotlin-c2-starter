package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>> = _asteroidList

    private val listTeste = ArrayList<Asteroid>()

    init {
        val a = Asteroid(1, "teste", "06-10-1994", 20.0, 30.0, 555.0, 288.0, true)
        val b = Asteroid(1, "teste1", "06-10-1994", 20.0, 30.0, 555.0, 288.0, true)
        val c = Asteroid(1, "teste2", "06-10-1994", 20.0, 30.0, 555.0, 288.0, true)
        val d = Asteroid(1, "teste3", "06-10-1994", 20.0, 30.0, 555.0, 288.0, true)
        val e = Asteroid(1, "teste4", "06-10-1994", 20.0, 30.0, 555.0, 288.0, true)

        listTeste.add(a)
        listTeste.add(b)
        listTeste.add(c)
        listTeste.add(d)
        listTeste.add(e)
    }

    fun addList(){
        _asteroidList.value = listTeste
    }
}