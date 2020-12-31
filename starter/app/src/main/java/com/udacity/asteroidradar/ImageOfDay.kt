package com.udacity.asteroidradar

data class ImageOfDay(
        val url:String,
        val media_type:String,
        val title:String)
{
    val isImage
        get()=media_type=="image"
}
