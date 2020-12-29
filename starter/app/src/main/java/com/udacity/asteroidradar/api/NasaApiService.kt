package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaApiService{
    @GET("neo/rest/v1/feed?api_key=DEMO_KEY")
//    suspend fun getAsteroids(@Query("start_date") start : String,
//                             @Query("api_key") api :String)
//    suspend fun getAsteroids(@Query("api_key") api_key : String): String
    fun getAsteroids(): Call<String>
}

// build Moshi object
//private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .build()

//build retrofit object
private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

object NasaApi {
    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}

