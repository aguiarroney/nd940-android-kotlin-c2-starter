package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.AsteroidListItemBinding

class MainFragmentAdapter(val clickListener: AsteroidItemListener) : ListAdapter<Asteroid, MainFragmentAdapter.ViewHolder>(AsteroidDiffCallBack()) {

    class ViewHolder private constructor (val binding: AsteroidListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Asteroid, clickListener: AsteroidItemListener){
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

//        var itemName: TextView = itemView.findViewById(R.id.tv_asteroid_name)
//        var itemDate: TextView = itemView.findViewById(R.id.tv_asteroid_date)
//        var itemImg: ImageView = itemView.findViewById(R.id.iv_asteroid_img)

        companion object {
            fun from (parent: ViewGroup) : ViewHolder {
                val layoutInflater =LayoutInflater.from(parent.context)
                val binding = AsteroidListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    private var _listItems = emptyList<Asteroid>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
//        holder.itemName.text = _listItems[position].codename
//        holder.itemDate.text = _listItems[position].closeApproachDate
//        if(_listItems[position].isPotentiallyHazardous)
//            holder.itemImg.setImageResource(R.drawable.ic_status_potentially_hazardous)
//        else
//            holder.itemImg.setImageResource(R.drawable.ic_status_normal)
    }

    override fun getItemCount(): Int = _listItems.size

    fun addAsteroidList(list: ArrayList<Asteroid>) {
        _listItems = list
        notifyDataSetChanged()
    }
}

class AsteroidDiffCallBack: DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

}

class AsteroidItemListener(val clickListener: (asteroidId : Long) -> Unit){
    fun onClick(asteroid: Asteroid) = clickListener(asteroid.id)
}