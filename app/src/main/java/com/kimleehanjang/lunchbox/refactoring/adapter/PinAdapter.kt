package com.kimleehanjang.lunchbox.refactoring.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.kimleehanjang.lunchbox.databinding.SourcePlaceListItemBinding
import com.kimleehanjang.lunchbox.refactoring.data.Pin


class PinAdapter(val onClick:(Pin)->Unit): ListAdapter<Pin, PinAdapter.ViewHolder>(PinDiffCallback()) {

    inner class ViewHolder(val binding: SourcePlaceListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pin: Pin) {


            //음식점 이름
            binding.sourcePlaceListPlaceName.text = pin.place.place_name

            //음식점 거리
            val distance = pin.place.distance + "m"
            binding.sourcePlaceListItemDistance.text = distance

            //음식점 카테고리
            val categoryName = pin.place.category_name
            var index = 0
            categoryName.forEachIndexed { i, char ->
                if (char == '>') {
                    index = i + 1
                    return@forEachIndexed
                }
            }
            binding.sourcePlaceListItemCategory.text = categoryName.substring(index)


            //음식점 전화번호
            var phone = ""
            phone = if (pin.place.phone == "")
                "N/A"
            else
                pin.place.phone
            binding.sourcePlaceListItemPhoneText.text = phone

            //음식점 아이콘
            binding.sourcePlaceListItemIcon.setImageResource(pin.foodEnum.normalIconImageId)
            //클릭 리스너
            binding.sourcePlaceListItemParentLayout.setOnClickListener {
                onClick(pin)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SourcePlaceListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PinDiffCallback : DiffUtil.ItemCallback<Pin>() {

    override fun areItemsTheSame(oldItem: Pin, newItem: Pin): Boolean {
        return oldItem.place.address_name == newItem.place.address_name
    }

    override fun areContentsTheSame(oldItem: Pin, newItem: Pin): Boolean {
        return oldItem.place.address_name == newItem.place.address_name
    }
}