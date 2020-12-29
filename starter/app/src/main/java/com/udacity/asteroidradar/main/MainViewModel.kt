package com.udacity.asteroidradar.main
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(application: Application): AndroidViewModel(application){

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails : LiveData<Asteroid>
        get()=_navigateToAsteroidDetails

//    val asteroidList = asteroidsRepository.asteroids

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList : LiveData<List<Asteroid>>
        get()=_asteroidList

    val imgUrl : String = "https://apod.nasa.gov/apod/image/2001/STSCI-H-p2006a-h-1024x614.jpg"

    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
        get()=_status


    init{

//
//        _asteroidList.value=listOf(asteroid1, asteroid2)
//        getAsteroids("2020-12-28", "DEMO_KEY")
        Log.i("ViewModel", " in viewmodel init block")
        getAsteroids()


    }


    private fun getAsteroids(){
        NasaApi.retrofitService.getAsteroids().enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                _status.value="Failed"+t.message
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
//                _status.value =response.body()
                var list = parseAsteroidsJsonResult(JSONObject(response.body()))
                _status.value="${list==null}"
                if (list.size>0) {
                    _status.value = list[0].codename
                }
                else{
                    _status.value = "list is empty"
                }
            }

        })

//        val asteroid1= Asteroid(1,"AB123","2020-2-2",1.0,.3,5.0, 100.0, true)
//        val asteroid2= Asteroid(1,"CD345","2020-2-2",2.0,.3,4.0, 50.0, false)
//        viewModelScope.launch {
//            try {
//                Log.i("ViewModel", " in coroutine")
//                val jsonResult = NasaApi.retrofitService.getAsteroids()
//                Log.i("AsteroidRepository", "jsonResult")
////                _asteroidList.value = parseAsteroidsJsonResult(JSONObject(jsonResult))
//                _asteroidList.value=listOf(asteroid1, asteroid2)
////            asteroidsRepository.refreshAsteroids("2020-12-28", "DEMO_KEY")
//            } catch (e: Exception){
//                Log.i("AsteroidRepository", "error in coroutine")
//
//            }
//            Log.i("ViewModel", " end  coroutine")
//
//        }

    }

    fun displayAsteroidDetails(asteroid: Asteroid){
        _navigateToAsteroidDetails.value=asteroid
    }

    fun displayAsteroidDetailsComplete(){
        _navigateToAsteroidDetails.value=null
    }

    //Factory for constructing viewModel with parameter
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}