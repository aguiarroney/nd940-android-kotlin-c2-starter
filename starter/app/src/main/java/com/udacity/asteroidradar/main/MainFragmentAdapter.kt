package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class MainFragmentAdapter(private val listener: OnAsteroidItemClickListener) :
    RecyclerView.Adapter<MainFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var itemName: TextView = itemView.findViewById(R.id.tv_asteroid_name)
        var itemDate: TextView = itemView.findViewById(R.id.tv_asteroid_date)
        var itemImg: ImageView = itemView.findViewById(R.id.iv_asteroid_img)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position.toLong())
            }
        }
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
        if (_listItems[position].isPotentiallyHazardous)
            holder.itemImg.setImageResource(R.drawable.ic_status_potentially_hazardous)
        else
            holder.itemImg.setImageResource(R.drawable.ic_status_normal)
    }

    override fun getItemCount(): Int = _listItems.size

    fun addAsteroidList(list: ArrayList<Asteroid>) {
        _listItems = list
        notifyDataSetChanged()
    }

    interface OnAsteroidItemClickListener {
        fun onItemClick(position: Long)
    }
}

//class AsteroidDiffCallBack: DiffUtil.ItemCallback<Asteroid>(){
//    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//}
//
//class AsteroidItemListener(val clickListener: (asteroidId : Long) -> Unit){
//    fun onClick(asteroid: Asteroid) = clickListener(asteroid.id)
//}