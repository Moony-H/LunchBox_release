package com.kimleehanjang.lunchbox.refactoring.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.FragmentStartBinding
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag

class StartFragment : BaseFragment<FragmentStartBinding>() {


    override val viewBindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStartBinding
        get() = FragmentStartBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.fragmentStartStartButton.setOnClickListener {
            parentFragmentManager.commit {
                remove(this@StartFragment)
                add(
                    R.id.activity_main_container,
                    TutorialFragment(),
                    FragmentTag.FRAGMENT_TUTORIAL.tag
                )
            }
        }
        return binding.root
    }

}