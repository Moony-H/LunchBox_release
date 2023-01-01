package com.kimleehanjang.lunchbox.refactoring.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kimleehanjang.lunchbox.databinding.FragmentFoodListBinding
import com.kimleehanjang.lunchbox.databinding.FragmentSlideBinding
import com.kimleehanjang.lunchbox.refactoring.data.DynamicViewId
import com.kimleehanjang.lunchbox.refactoring.data.Food
import com.kimleehanjang.lunchbox.refactoring.util.ToggleButtonFactory
import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel

class TestFragment : Fragment() {
    private var _binding: FragmentSlideBinding? = null
    private val binding: FragmentSlideBinding
        get() = _binding!!
    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireParentFragment() })

    //아무리 생각해도 버튼의 상태는 mvvm 중에 view가 담당해야 하는 것 같다.
    private val toggleButtonState = mutableListOf<Boolean>()

    init {
        Food.values().forEach { _ ->
            toggleButtonState.add(false)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentSlideBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}