package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidListAdapter(val clickListener : AsteroidListener) : ListAdapter<Asteroid, AsteroidListAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(private var binding: ListItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid){
            binding.asteroid=asteroid
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemAsteroidBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(item)
            }
        holder.bind(item)
    }

    class AsteroidListener(val onClickListener : (asteroid:Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) =onClickListener(asteroid)
    }
}



class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem==newItem
    }

}
