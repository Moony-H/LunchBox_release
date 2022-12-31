package com.kimleehanjang.lunchbox.refactoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseFragment<T : ViewBinding>() : Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    protected abstract val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) -> T


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = viewBindingInflater(inflater, container, false)


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}