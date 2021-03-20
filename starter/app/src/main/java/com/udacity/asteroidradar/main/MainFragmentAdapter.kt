package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class MainFragmentAdapter : RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.tv_asteroid_name)
        var itemDate: TextView = itemView.findViewById(R.id.tv_asteroid_date)
        var itemImg: ImageView = itemView.findViewById(R.id.iv_asteroid_img)
    }

    private var _listItems = emptyList<Asteroid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.asteroid_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = _listItems[position].codename
        holder.itemDate.text = _listItems[position].closeApproachDate
        holder.itemImg.setImageResource(R.drawable.ic_status_potentially_hazardous)
    }

    override fun getItemCount(): Int = _listItems.size

    fun addAsteroidList(list: ArrayList<Asteroid>) {
        _listItems = list
        notifyDataSetChanged()
    }
}