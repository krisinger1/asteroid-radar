package com.udacity.asteroidradar.main
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseStringToAsteroidList
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


//    private val _asteroidList = MutableLiveData<List<Asteroid>>()
//    val asteroidList : LiveData<List<Asteroid>>
//        get()=_asteroidList

    private val _imgUrl = MutableLiveData<String>()
    val imgUrl : LiveData<String>
        get()= _imgUrl

    private val _imgTitle = MutableLiveData<String>()
    val imgTitle : LiveData<String>
        get()= _imgTitle

    private val _status = MutableLiveData<String>()
    val status : LiveData<String>
        get()=_status



    init{

        Log.i("ViewModel", " in viewmodel init block")
        getImageOfDay()
        getAsteroids()

    }

    val asteroidList = asteroidsRepository.asteroids

    private fun getImageOfDay() {
        Log.i("viewmodel", "trying to get image of the day")
        viewModelScope.launch{
            try {
                var imageOfDay = NasaApi.retrofitService.getImageOfDay("DEMO_KEY")
                if (imageOfDay.isImage) {
                    _imgUrl.value = imageOfDay.url
                    _imgTitle.value = imageOfDay.title
                } else {
                    _imgUrl.value = "https://apod.nasa.gov/apod/image/2001/STSCI-H-p2006a-h-1024x614.jpg"
                    _imgTitle.value = imageOfDay.title

                }
                Log.i("viewmodel", imageOfDay.url)
            }
            catch(e: Exception){
                Log.i("viewmodel","failed to get image of the day")
            }
        }
    }


    private fun getAsteroids(){
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids(Constants.API_KEY)
        }
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