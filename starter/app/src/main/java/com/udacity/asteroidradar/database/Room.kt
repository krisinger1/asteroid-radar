package com.udacity.asteroidradar.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao{
    @Query("select * from databaseasteroid where closeApproachDate>= :date order by closeApproachDate")
    fun getAsteroids(date:String) : LiveData<List<DatabaseAsteroid>>

    @Query("delete from databaseasteroid where closeApproachDate < :date")
    fun removeAsteroids(date:String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids : DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase(){
    abstract val asteroidDao : AsteroidDao}

private lateinit var INSTANCE : AsteroidsDatabase

fun getDatabase(context: Context):AsteroidsDatabase{
    Log.i("Room","getting database")
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, AsteroidsDatabase::class.java,
                    "asteroids").build()
        }
    }
    return INSTANCE
}
