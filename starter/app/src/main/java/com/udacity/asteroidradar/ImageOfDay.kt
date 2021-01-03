package com.udacity.asteroidradar

// for moshi to use for image of the day
data class ImageOfDay(
        val url:String,
        val media_type:String,
        val title:String)
{
    val isImage
        get()=media_type=="image"
}
