package com.kimleehanjang.lunchbox.refactoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kimleehanjang.lunchbox.databinding.SourceSearchListItemBinding
import com.kimleehanjang.lunchbox.refactoring.data.Place

class SearchAdapter(val onClick:(Place)->Unit):ListAdapter<Place,SearchAdapter.ViewHolder>(SearchPlaceDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SourceSearchListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: SourceSearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            binding.sourceSearchListItemName.text = place.place_name
            var address = ""
            if (place.road_address_name != null && place.road_address_name != "")
                address = place.road_address_name
            else if (place.address_name != null && place.address_name != "")
                address = place.address_name
            binding.sourceSearchListItemAddress.text = address
            binding.sourceSearchListParentLayout.setOnClickListener {
                onClick(place)
            }
        }
    }

}

class SearchPlaceDiffCallback:DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.address_name == newItem.address_name || oldItem.road_address_name == oldItem.road_address_name
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.address_name == newItem.address_name || oldItem.road_address_name == oldItem.road_address_name
    }

}