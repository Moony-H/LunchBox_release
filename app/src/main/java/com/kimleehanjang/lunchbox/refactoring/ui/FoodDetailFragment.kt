package com.kimleehanjang.lunchbox.refactoring.ui

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.FragmentFoodDetailBinding
import com.kimleehanjang.lunchbox.databinding.FragmentFoodListBinding
import com.kimleehanjang.lunchbox.refactoring.manager.Utils
import com.kimleehanjang.lunchbox.refactoring.data.DynamicViewId
import com.kimleehanjang.lunchbox.refactoring.data.Food
import com.kimleehanjang.lunchbox.refactoring.data.Pin
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag
import com.kimleehanjang.lunchbox.refactoring.util.ToggleButtonFactory
import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class FoodDetailFragment: BaseFragment<FragmentFoodDetailBinding>(),View.OnClickListener {

    override val viewBindingInflater: (inflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> FragmentFoodDetailBinding
        get() = FragmentFoodDetailBinding::inflate


    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var findRoadButton: ImageView

    private lateinit var webView: WebView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("FoodDetailFragment", "onCreateView")
        binding.fragmentFoodDetailSlide.fragmentFoodDetailCloseButton.setOnClickListener(this)

        //현재 사용자와 상호작용 하고 있는 프래그먼트 알림.
        viewModel.setFrontFragment(FragmentTag.FRAGMENT_FOOD_DETAIL.fragment_num)

        val radius = "반경: ${viewModel.circleRadius.value}m"
        binding.fragmentFoodDetailCard.fragmentFoodCircleBoundText.text = radius
        binding.fragmentFoodDetailCard.fragmentFoodBaseAddressText.text =
            viewModel.selectedAddress.value

        //동적인 랜덤 버튼 생성
        findRoadButton = ImageView(context)
        val randomButtonWidth =
            resources.getDimension(R.dimen.size_fragment_restaurant_bubble_width)
        val randomButtonHeight =
            resources.getDimension(R.dimen.size_fragment_restaurant_bubble_height)
        findRoadButton.layoutParams =
            ViewGroup.LayoutParams(randomButtonWidth.toInt(), randomButtonHeight.toInt())
        findRoadButton.id = DynamicViewId.FIND_LOAD_BUTTON_ID
        findRoadButton.setImageResource(R.drawable.image_icon_find_road)
        findRoadButton.setOnClickListener(this)
        binding.fragmentFoodDetailMainLayout.addView(findRoadButton)
        binding.fragmentFoodDetailParentLayout.setOnLayout {
            setRandomButtonPosition()
        }


        //슬라이드 레이아웃의 callback 설정
        binding.fragmentFoodDetailParentLayout.setOnSlideViewCaptured {
            findRoadButton.visibility = View.GONE
        }

        binding.fragmentFoodDetailParentLayout.setOnSlideViewSettled {
            setRandomButtonPosition()
            findRoadButton.visibility = View.VISIBLE
        }

        //val rect = Rect(0, 1309, 582, 1484)
        //binding.fragmentFoodDetailParentLayout.setNonTouchArea(listOf(rect))

        webView = binding.fragmentFoodDetailSlide.fragmentFoodDetailSlideUpWebView
        val webViewSettings = webView.settings
        webViewSettings.apply {
            this.javaScriptEnabled = true
            this.loadWithOverviewMode = true
            this.useWideViewPort = true
            this.cacheMode = WebSettings.LOAD_NO_CACHE
            this.domStorageEnabled = true
        }
        webView.webViewClient = WebViewClient()
        viewModel.selectedPin.value?.let {
            binding.fragmentFoodDetailSlide.fragmentFoodDetailCategory.text = it.foodEnum.engName
            webView.loadUrl(it.place.place_url)
        }

        viewModel.selectedPin.observe(viewLifecycleOwner, selectedPinObserver)
        viewModel.circleRadius.observe(viewLifecycleOwner, circleRadiusObserver)

        binding.fragmentFoodDetailParentLayout.setScrollView(webView.id)



        return binding.root
    }



    override fun onClick(view: View?) {
        when (view) {
            binding.fragmentFoodDetailSlide.fragmentFoodDetailCloseButton -> {
                Log.d("FoodDetailFragment", "close button clicked")
                parentFragmentManager.popBackStack()
            }
            findRoadButton -> {
                Log.d("FoodDetailFragment", "find road button clicked")
                connectKakaoMap()
            }
        }
    }

    private fun setRandomButtonPosition() {
        val firstSlideTop = binding.fragmentFoodDetailParentLayout.getSlideViewTop()
        val firstSlideRight = binding.fragmentFoodDetailParentLayout.getSlideViewRight()
        findRoadButton.x = (firstSlideRight - findRoadButton.width - Utils.dpToPixel(20)).toFloat()
        findRoadButton.y = (firstSlideTop - findRoadButton.height).toFloat()
    }

    private fun connectKakaoMap() {
        try {
            val start = viewModel.selectedPosition.value
            val end = viewModel.selectedPin.value
            if (start != null && end != null) {
                val url =
                    "kakaomap://route?sp=${start.latitude},${start.longitude}&ep=${end.place.latitude},${end.place.longitude}&by=FOOT"
                //Uri 로 카카오맵 호출
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        } catch (e: Exception) {
            //카카오맵이 설치되지 않은 경우 설치페이지로 이동.
            Toast.makeText(context, "카카오 맵이 설치되지 않아 설치 페이지로 이동합니다.", Toast.LENGTH_SHORT).show()
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=net.daum.android.map"))
            startActivity(intent)

        }

    }

    private val selectedPinObserver = Observer { pin: Pin ->

        binding.fragmentFoodDetailSlide.fragmentFoodDetailCategory.text = pin.foodEnum.engName
        webView.loadUrl(pin.place.place_url)
    }

    private val circleRadiusObserver = Observer { radius: Int ->
        val text = "반경: ${radius}m"
        binding.fragmentFoodDetailCard.fragmentFoodCircleBoundText.text = text

    }
}