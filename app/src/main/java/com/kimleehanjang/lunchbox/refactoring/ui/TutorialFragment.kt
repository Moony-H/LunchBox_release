package com.kimleehanjang.lunchbox.refactoring.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2
import com.kimleehanjang.lunchbox.R


import com.kimleehanjang.lunchbox.databinding.FragmentTutorialBinding
import com.kimleehanjang.lunchbox.refactoring.others.TutorialViewPagerAdapter
import com.kimleehanjang.lunchbox.refactoring.data.DynamicViewId
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag

class TutorialFragment: Fragment() {

    private var _binding: FragmentTutorialBinding? = null
    private val binding: FragmentTutorialBinding
        get() = _binding!!

    private var prevPosition = 0
    private val indicatorImageViewList = mutableListOf<ImageView>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTutorialBinding.inflate(inflater, container, false)


        val viewPagerAdapter = TutorialViewPagerAdapter()
        binding.fragmentTutorialViewPager.adapter = viewPagerAdapter
        binding.fragmentTutorialViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val count = viewPagerAdapter.itemCount

        for (i in 1 until count) {
            val imageView = ImageView(requireContext())
            val layoutParams =
                ConstraintLayout.LayoutParams(
                    resources.getDimension(R.dimen.size_dot).toInt(), resources.getDimension(
                        R.dimen.size_dot
                    ).toInt()
                )
            if (i == 1) {

                layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.rightToLeft = DynamicViewId.TUTORIAL_INDICATOR_BASE_ID + i + 1
                layoutParams.rightMargin = resources.getDimension(R.dimen.margin_dot_right).toInt()
            } else if (i == count - 1) {

                layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
                layoutParams.leftToRight = DynamicViewId.TUTORIAL_INDICATOR_BASE_ID + i - 1
            } else {
                layoutParams.leftToRight = DynamicViewId.TUTORIAL_INDICATOR_BASE_ID + i - 1
                layoutParams.rightToLeft = DynamicViewId.TUTORIAL_INDICATOR_BASE_ID + i + 1
                layoutParams.rightMargin = resources.getDimension(R.dimen.margin_dot_right).toInt()
            }

            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

            imageView.id = DynamicViewId.TUTORIAL_INDICATOR_BASE_ID + i
            imageView.setImageResource(R.drawable.basic_circle)
            imageView.layoutParams = layoutParams
            indicatorImageViewList.add(imageView)
            binding.fragmentTutorialIndicatorLayout.addView(imageView)
        }

        binding.fragmentTutorialViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("position", "$position")
                if (position + 1 != count) {

                    //사용자가 뒤로도 페이지를 넘길 수 있음. 그래서 prev.
                    val prevImageView = indicatorImageViewList[prevPosition]
                    prevImageView.layoutParams.width =
                        resources.getDimension(R.dimen.size_dot).toInt()
                    prevImageView.layoutParams.height =
                        resources.getDimension(R.dimen.size_dot).toInt()


                    val imageView = indicatorImageViewList[position]
                    imageView.layoutParams.width =
                        resources.getDimension(R.dimen.size_dot_selected).toInt()
                    imageView.layoutParams.height =
                        resources.getDimension(R.dimen.size_dot_selected).toInt()
                    imageView.requestLayout()
                    prevPosition = position

                } else {
                    parentFragmentManager.commit {
                        remove(this@TutorialFragment)
                        replace(
                            R.id.activity_main_container,
                            MainFragment(),
                            FragmentTag.FRAGMENT_MAIN.tag
                        )
                    }
                }


            }

        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}