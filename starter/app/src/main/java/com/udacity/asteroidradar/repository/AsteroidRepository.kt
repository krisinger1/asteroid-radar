package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseStringToAsteroidList
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class AsteroidsRepository(private val database : AsteroidsDatabase){

    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT)
    private val today: String = dateFormat.format(Date())

    // only get asteroids from today on
    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids(today)){
        it.asDomainModel()
    }

    suspend fun removeOldAsteroids(){
        Log.i("repository","in remove method")
        withContext(Dispatchers.IO) {
            database.asteroidDao.removeAsteroids(today)
            Log.i("repository","removed asteroids")
        }
    }


    //refresh the database with data from network
    suspend fun refreshAsteroids(api_key: String){

        withContext(Dispatchers.IO){
            try {
                Log.i("AsteroidRepository", "trying to get asteroids")

                val jsonResult = NasaApi.retrofitService.getAsteroids(api_key)
                // parse JSON string into List of Asteroids
                val list = parseStringToAsteroidList(jsonResult)

                // make sure there is something in the list
                if (list.isNotEmpty()) {
                    val asteroidArray=toDatabaseModel(list)
                    database.asteroidDao.insertAll(*asteroidArray)
                    Log.i("repository", "getAsteroids successful")
                }
                else{
                    Log.i("AsteroidRepository", "no asteroids in list")
                }
            }
            catch (e: Exception){
                Log.i("AsteroidRepository", " in catch block. getAsteroids failed. "+ e.printStackTrace())

            }
        }
    }

    // method to convert domain objects to database objects to insert in database
    private fun toDatabaseModel(list : List<Asteroid>): Array<DatabaseAsteroid>{
        return list.map{
            DatabaseAsteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter =it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }.toTypedArray()
    }
}

