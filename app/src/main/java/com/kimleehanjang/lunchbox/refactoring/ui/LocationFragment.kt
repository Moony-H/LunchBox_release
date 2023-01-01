package com.kimleehanjang.lunchbox.refactoring.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.FragmentLocationBinding
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag

import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>(), View.OnClickListener {

    override val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLocationBinding
        get() = FragmentLocationBinding::inflate
    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)


        Log.d("LocationFragment", "onCreateView")


        //현재 사용자와 상호작용 하고 있는 프래그먼트 알림
        viewModel.setFrontFragment(FragmentTag.FRAGMENT_LOCATION.fragment_num)

        //모바일 광고 설정
        context?.let {
            MobileAds.initialize(it) {
                Log.d("test", "ad done")
            }
            val adRequest = AdRequest.Builder().build()
            binding.fragmentLocationAdview.loadAd(adRequest)
        }


        binding.fragmentLocationSearch.fragmentLocationButtonSearch.setOnClickListener(this)
        binding.fragmentLocationNext.setOnClickListener(this)
        binding.fragmentLocationUserPosition.setOnClickListener(this)


        viewModel.selectedAddress.observe(viewLifecycleOwner) { address: String ->
            binding.fragmentLocationAddressText.text = address
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        Log.d("testing fragment", "on Stop")
    }

    override fun onResume() {
        super.onResume()
        Log.d("testing fragment", "onResume")
        viewModel.isTracking = true
    }


    override fun onClick(view: View?) {
        when (view) {
            binding.fragmentLocationNext -> {

                Log.d("LocationButtonFragment", "next button clicked")
                viewModel.selectedPosition.value?.let {

                    parentFragmentManager.commit {
                        replace(
                            R.id.fragment_main_ui_container,
                            FoodListFragment(),
                            FragmentTag.FRAGMENT_FOOD_LIST.tag
                        )
                        addToBackStack(FragmentTag.FRAGMENT_FOOD_LIST.tag)
                    }
                    viewModel.isTracking = false
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "맵을 터치하거나 주소를 검색하여 중심점을 선택해 주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            binding.fragmentLocationUserPosition -> {
                Log.d("LocationButtonFragment", "position button clicked")
                viewModel.userPosition.value?.let { viewModel.setSelectedPosition(it) }
                viewModel.isTracking = true
            }
            binding.fragmentLocationSearch.fragmentLocationButtonSearch -> {
                Log.d("LocationButtonFragment", "search view clicked")
                parentFragmentManager.commit {
                    replace(
                        R.id.fragment_main_ui_container,
                        SearchFragment(),
                        FragmentTag.FRAGMENT_SEARCH.tag
                    )
                    addToBackStack(FragmentTag.FRAGMENT_SEARCH.tag)

                }
            }
        }
    }

}