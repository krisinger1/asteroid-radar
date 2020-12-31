package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseStringToAsteroidList
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.sql.DatabaseMetaData


class AsteroidsRepository(private val database : AsteroidsDatabase){
    val asteroids : LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()){
        it.asDomainModel()
    }

    //refresh the database with data from network
    suspend fun refreshAsteroids(api_key: String){
        var asteroidArray = emptyArray<DatabaseAsteroid>()

        withContext(Dispatchers.IO){
            try {
                Log.i("AsteroidRepository", "trying to get asteroids")

                var jsonResult = NasaApi.retrofitService.getAsteroids(api_key)
                var list = parseStringToAsteroidList(jsonResult)

                if (list.size > 0) {
                    Log.i("AsteroidRepository", "List 0 ="+list[0].codename)

                    asteroidArray=toDatabaseModel(list)
                    Log.i("AsteroidRepository", "asteroidArray 0 ="+asteroidArray[0].codename)

                    database.asteroidDao.insertAll(*asteroidArray)
                    Log.i("AsteroidRepository", "${asteroids==null}")

                }
                else{
                    Log.i("AsteroidRepository", "no asteroids in list")
                }
            }
            catch (e: Exception){
                Log.i("AsteroidRepository", " in catch block. getAsteroids failed.")
                database.asteroidDao.insertAll(*asteroidArray)

            }
        }
    }

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

