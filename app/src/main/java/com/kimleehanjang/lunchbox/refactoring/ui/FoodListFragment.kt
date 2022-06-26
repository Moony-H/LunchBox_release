package com.kimleehanjang.lunchbox.refactoring.ui

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.FragmentFoodListBinding
import com.kimleehanjang.lunchbox.refactoring.manager.Utils
import com.kimleehanjang.lunchbox.refactoring.adapter.PinAdapter
import com.kimleehanjang.lunchbox.refactoring.data.DynamicViewId
import com.kimleehanjang.lunchbox.refactoring.data.Food
import com.kimleehanjang.lunchbox.refactoring.data.Pin
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag
import com.kimleehanjang.lunchbox.refactoring.util.ToggleButtonFactory
import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoodListFragment: Fragment(),View.OnClickListener {

    //이 프래그먼트가 하는 일:
    //1. viewModel 에서 검색된 핀 값을 받아서 사용자에게 목록을 보여 준다.
    //2. viewModel 의 사용자에게 보여 줄 핀들을 바꾼다.
    private var _binding: FragmentFoodListBinding? = null
    private val binding: FragmentFoodListBinding
        get() = _binding!!
    private lateinit var adapter: PinAdapter

    private lateinit var randomButton: ImageView
    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })


    //아무리 생각해도 버튼의 상태는 mvvm 중에 view가 담당해야 하는 것 같다.
    private var toggleButtonState = mutableListOf<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodListBinding.inflate(inflater, container, false)
        Log.d("FoodListFragment", "onCreateView")

        //현재 사용자와 상호작용 하고 있는 프래그먼트 알림.
        viewModel.setFrontFragment(FragmentTag.FRAGMENT_FOOD_LIST.fragment_num)

        viewModel.setSelectedPin(null)


        toggleButtonState = viewModel.buttonState.value as MutableList<Boolean>
        //음식 고르기 버튼들을 동적으로 세팅한다.
        context?.let { context ->
            val toggleButtonFactory = ToggleButtonFactory()
            val buttons = toggleButtonFactory.getButtons(context) { button, state ->
                toggleButtonState[button.id - DynamicViewId.FOOD_BUTTON_BASE_ID] = state
                viewModel.setButtonState(toggleButtonState)
            }

            toggleButtonState.forEachIndexed { index, b ->
                if (b)
                    buttons[index].setOn()
                else
                    buttons[index].setOff()
            }

            buttons.forEach {
                binding.fragmentFoodListSlideLayout.fragmentFoodListLinearLayout.addView(it)
            }


        }


        //동적인 랜덤 버튼 생성
        randomButton = ImageView(context)
        val randomButtonWidth =
            resources.getDimension(R.dimen.size_fragment_restaurant_bubble_width)
        val randomButtonHeight =
            resources.getDimension(R.dimen.size_fragment_restaurant_bubble_height)
        randomButton.layoutParams =
            ViewGroup.LayoutParams(randomButtonWidth.toInt(), randomButtonHeight.toInt())
        randomButton.id = DynamicViewId.RANDOM_BUTTON_ID
        randomButton.setImageResource(R.drawable.image_icon_random_button)
        randomButton.setOnClickListener(this)
        binding.fragmentFoodListMainLayout.addView(randomButton)
        binding.fragmentFoodListParentLayout.setOnLayout {
            setRandomButtonPosition()
        }


        //슬라이드 레이아웃의 callback 설정
        binding.fragmentFoodListParentLayout.setOnSlideViewCaptured {
            Log.d("gone", "gone")
            randomButton.visibility = View.GONE
        }

        binding.fragmentFoodListParentLayout.setOnSlideViewSettled {
            setRandomButtonPosition()
            Log.d("visible", "visible")
            randomButton.visibility = View.VISIBLE
        }
        //val rect = Rect(0, 1309, 582, 1484)
        //binding.fragmentFoodListParentLayout.setNonTouchArea(listOf(rect))

        viewModel.circleRadius.observe(viewLifecycleOwner) {
            it?.let {
                val text = "반경: ${it}m"
                binding.fragmentFoodListCard.fragmentFoodCircleBoundText.text = text
            }
        }

        val text = "반경: ${viewModel.circleRadius.value}m"
        binding.fragmentFoodListCard.fragmentFoodCircleBoundText.text = text
        binding.fragmentFoodListCard.fragmentFoodBaseAddressText.text =
            viewModel.selectedAddress.value


        binding.fragmentFoodListParentLayout.setScrollView(R.id.fragment_food_list_recyclerView)



        adapter = PinAdapter { viewModel.setSelectedPin(it) }
        binding.fragmentFoodListSlideLayout.fragmentFoodListRecyclerView.layoutManager =
            LinearLayoutManager(context)
        binding.fragmentFoodListSlideLayout.fragmentFoodListRecyclerView.adapter = adapter
        adapter.submitList(viewModel.listPins.value)


        viewModel.listPins.observe(viewLifecycleOwner) { pins: List<Pin> ->
            adapter.submitList(pins)
        }


        viewModel.selectedPin.observe(viewLifecycleOwner) { pin: Pin? ->
            pin?.let {
                Log.d("FoodListFragment", "selected pin observed")
                if (viewModel.frontFragment.value != null && viewModel.frontFragment.value!! != FragmentTag.FRAGMENT_FOOD_DETAIL.fragment_num) {
                    parentFragmentManager.commit {
                        replace(
                            R.id.fragment_main_ui_container,
                            FoodDetailFragment(),
                            FragmentTag.FRAGMENT_FOOD_DETAIL.tag
                        )
                        addToBackStack(FragmentTag.FRAGMENT_FOOD_DETAIL.tag)
                    }
                }

            }

        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FoodListFragment", "on DestroyView")


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FoodListFragment", "on Destroy")
        _binding = null
        //viewModel.circleRadius.removeObservers(requireActivity())
        //viewModel.listPins.removeObserver(listPinObserve)
        //viewModel.selectedPin.removeObserver(selectedPinObserver)
        viewModel.setAllPinsNull()
        viewModel.setListPinNull()
        viewModel.setSelectedPin(null)
    }


    override fun onClick(view: View?) {
        when (view) {
            randomButton -> {
                Log.d("FoodListFragment", "random button clicked")
                parentFragmentManager.commit {
                    viewModel.setSelectedPinRandom()
                }
            }
        }

    }


    private fun setRandomButtonPosition() {
        val firstSlideTop = binding.fragmentFoodListParentLayout.getSlideViewTop()
        val firstSlideRight = binding.fragmentFoodListParentLayout.getSlideViewRight()
        randomButton.x = (firstSlideRight - randomButton.width - Utils.dpToPixel(20)).toFloat()
        randomButton.y = (firstSlideTop - randomButton.height).toFloat()
    }

}