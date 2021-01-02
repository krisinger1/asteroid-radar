package com.udacity.asteroidradar

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidListAdapter
import java.security.AccessController.getContext

@BindingAdapter("imageOfDayUrl")
fun bindImageOfDay(imageView : ImageView, imgUrl:String?){
    imgUrl?.let{
        Picasso.get()
                .load(imgUrl)
                .into(imageView);
    }
//    if (imgUrl!=null){
//        Picasso.get()
//                .load(imgUrl)
//                .into(imageView)
//    }
//    else{
//        Picasso.get()
//                .load(R.drawable.placeholder_picture_of_day)
//                .into(imageView)
//    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?){
    val adapter = recyclerView.adapter as AsteroidListAdapter
    //if (data!=null) {
        adapter.submitList(data)
    //}
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription="Potentially Hazardous"
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription="Not Hazardous"

    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription="Potentially Hazardous Asteroid Image"
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription="Not Hazardous Asteroid Image"

    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
