package com.kimleehanjang.lunchbox.refactoring.others

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.SourceTutorialViewPagerBinding
import com.kimleehanjang.lunchbox.refactoring.LunchApplication
import com.kimleehanjang.lunchbox.refactoring.util.Common

class TutorialViewPagerAdapter():RecyclerView.Adapter<TutorialViewPagerAdapter.ViewHolder>() {

    private val title: Array<String> =
        LunchApplication.getApplicationContext().resources.getStringArray(R.array.tutorial_title)
    private val subTitle: Array<String> =
        LunchApplication.getApplicationContext().resources.getStringArray(R.array.tutorial_subtitle)

    //private val guidImage=LunchApplication.getApplicationContext().resources.obtainTypedArray(R.array.tutorial_image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            SourceTutorialViewPagerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    inner class ViewHolder(val binding: SourceTutorialViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            Glide.with(binding.sourceTutorialGuidImage).load(Common.guidImage[position]).into(binding.sourceTutorialGuidImage)
            //binding.sourceTutorialGuidImage.setImageResource(Common.guidImage[position])
            binding.sourceTutorialMainTitle.text = title[position]
            binding.sourceTutorialSubTitle.text = subTitle[position]
        }

    }


}