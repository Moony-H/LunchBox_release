package com.kimleehanjang.lunchbox.refactoring.ui

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimleehanjang.lunchbox.databinding.FragmentSearchBinding
import com.kimleehanjang.lunchbox.refactoring.adapter.SearchAdapter
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag
import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel
import com.kimleehanjang.lunchbox.refactoring.viewModels.SearchViewModel
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })
    private val searchViewModel: SearchViewModel by viewModels()

    private lateinit var adapter: SearchAdapter

    private var isSearchable = true
    private var newQuery = ""
    private var oldQuery = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)


        //현재 사용자와 상호작용 하고 있는 프래그먼트 알림
        mainViewModel.setFrontFragment(FragmentTag.FRAGMENT_LOCATION.fragment_num)

        binding.fragmentSearchEdittext.postDelayed({
            binding.fragmentSearchEdittext.requestFocus()
            showKeyboard(binding.fragmentSearchEdittext)

        }, 100)


        binding.fragmentSearchEdittext.addTextChangedListener({ _: CharSequence?, _: Int, _: Int, _: Int -> },
            { s: CharSequence?, _: Int, _: Int, _: Int ->
                Log.d("SearchFragment", "text changed ${s.toString()}")
                newQuery = s.toString()


                if (isSearchable && newQuery != "") {
                    isSearchable = false
                    oldQuery = newQuery
                    searchViewModel.searchPlaceAsync(oldQuery)
                }

            }
        )
        adapter = SearchAdapter { place ->

            mainViewModel.setSelectedPosition(LatLng(place.latitude, place.longitude))
            Log.d("SearchFragment", "search result item clicked")
            parentFragmentManager.popBackStack()
        }

        binding.addressSearchRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.addressSearchRecyclerView.adapter = adapter
        binding.addressSearchRecyclerView.itemAnimator = null

        searchViewModel.searchResult.observe(viewLifecycleOwner) { placeData ->
            isSearchable = true
            adapter.submitList(placeData.documents)
            //검색 하는 도중에 사용자가 텍스트를 바꾸었을 때
            if (newQuery != oldQuery) {
                Log.d("SearchFragment", "diff")
                oldQuery = newQuery
                if (oldQuery != "") {
                    isSearchable = false
                    Log.d("SearchFragment", "request query: $oldQuery")
                    searchViewModel.searchPlaceAsync(oldQuery)
                } else
                    isSearchable = true
            }

        }


        return binding.root
    }

    private fun showKeyboard(editText: EditText) {
        val inputMethodManager =
            activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, 0)

    }
}