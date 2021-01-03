package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidListAdapter

@BindingAdapter("imageOfDayUrl")
fun bindImageOfDay(imageView : ImageView, imgUrl:String?){

    if (imgUrl!=null){
        Picasso.get()
                .load(imgUrl)
                .into(imageView)
    }
    else{
        Picasso.get()
                // Photo by Jeremy Thomas on Unsplash https://unsplash.com/photos/E0AHdsENmDg
                // placeholder photo to load when can't get image of the day
                .load(R.drawable.space_placeholder)
                .into(imageView)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?){
    val adapter = recyclerView.adapter as AsteroidListAdapter
        adapter.submitList(data)
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
