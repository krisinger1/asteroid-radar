package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails : LiveData<Asteroid>
        get()=_navigateToAsteroidDetails

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList : LiveData<List<Asteroid>>
        get()=_asteroidList


    init{
        val asteroid1= Asteroid(1,"AB123","2020-2-2",1.0,.3,5.0, 100.0, true)
        val asteroid2= Asteroid(1,"CD345","2020-2-2",2.0,.3,4.0, 50.0, false)

        _asteroidList.value=listOf(asteroid1, asteroid2)
    }

    fun displayAsteroidDetails(asteroid: Asteroid){
        _navigateToAsteroidDetails.value=asteroid
    }

    fun displayAsteroidDetailsComplete(){
        _navigateToAsteroidDetails.value=null
    }
}