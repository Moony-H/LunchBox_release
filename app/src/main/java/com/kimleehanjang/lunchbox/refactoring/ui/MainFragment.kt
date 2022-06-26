package com.kimleehanjang.lunchbox.refactoring.ui

import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.databinding.FragmentMainBinding
import com.kimleehanjang.lunchbox.refactoring.manager.CircleManager
import com.kimleehanjang.lunchbox.refactoring.manager.MapUnitConverter
import com.kimleehanjang.lunchbox.refactoring.manager.Utils
import com.kimleehanjang.lunchbox.refactoring.tag.FragmentTag

import com.kimleehanjang.lunchbox.refactoring.viewModels.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import dagger.hilt.android.AndroidEntryPoint

//이 프래그먼트가 하는 일:
//1. 맵 관리. 맵에 관한 처리는 이곳에서 한다.
//2. 맵 관리와 마찬가지로 서클에 관한 것도 여기서 처리한다.
@AndroidEntryPoint
class MainFragment: Fragment() ,OnMapReadyCallback {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    //viewModel
    private val viewModel: MainViewModel by viewModels(ownerProducer = { requireActivity() })

    //naverMap
    private var map: NaverMap? = null
    private var beforeFragment = 1


    //circleOverlay
    private val circle = CircleOverlay()

    //selectedPosition Pin
    private val marker = Marker()

    //radius marker
    private val radiusMarker = Marker()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        //현재 사용자와 상호작용 하고 있는 프래그먼트 알림
        viewModel.setFrontFragment(FragmentTag.FRAGMENT_MAIN.fragment_num)


        //서클 세팅

        circle.color = ContextCompat.getColor(requireContext(), R.color.transparent_main)
        circle.radius = 100.0
        circle.globalZIndex = 10000


        //위치 마커 세팅
        marker.icon = OverlayImage.fromResource(R.drawable.image_character_egg_pin)
        marker.width = Utils.dpToPixel(35)
        marker.height = Utils.dpToPixel((35 * 74) / 99)
        marker.anchor = PointF(0.5f, 0.5f)


        //반지름 마커 세팅
        radiusMarker.icon = OverlayImage.fromResource(R.drawable.image_map_circle_diamond_shadow)
        radiusMarker.width = Utils.dpToPixel(50)
        radiusMarker.height = Utils.dpToPixel(50)
        radiusMarker.globalZIndex = 300000
        radiusMarker.anchor = PointF(0.5f, 0.5f)


        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragment_main_map_container) as MapFragment?
                ?: MapFragment.newInstance().also { mapFragment ->
                    childFragmentManager.commit {
                        add(R.id.fragment_main_map_container, mapFragment)
                    }
                }
        mapFragment.getMapAsync(this)


        val locationFragment =
            childFragmentManager.findFragmentByTag(FragmentTag.FRAGMENT_LOCATION.tag) as LocationFragment?
        locationFragment ?: run {
            childFragmentManager.commit {
                add(
                    R.id.fragment_main_ui_container,
                    LocationFragment(),
                    FragmentTag.FRAGMENT_LOCATION.tag
                )

            }
        }



        viewModel.frontFragment.observe(viewLifecycleOwner) { fragment_number ->
            if (isMoveOnListFragment(fragment_number)) {
                Log.d("MainFragment", "move on List fragment")
                circle.radius = 100.0
                viewModel.selectedPosition.value?.let { position -> circle.center = position }
                circle.map = map
                radiusMarker.position =
                    MapUnitConverter.convertPixelToLatLngByRelativePosition(map!!, marker, 100F)
                radiusMarker.map = map
            }

            if (isMoveOnLocationFragment(fragment_number)) {
                circle.map = null
                radiusMarker.map = null
            }
            beforeFragment = fragment_number


        }


        viewModel.userPosition.observe(viewLifecycleOwner) { latLng ->
            Log.d("MainFragment", "user position changed: lat:${latLng.latitude}, lng:${latLng.longitude}")
            if(viewModel.isTracking){
                viewModel.setSelectedPosition(latLng)
            }

        }

        viewModel.selectedPosition.observe(viewLifecycleOwner) {
            moveCamera(it)
            marker.position = it
            marker.map = map
            viewModel.setSelectedAddress(it)
        }

        viewModel.buttonState.observe(viewLifecycleOwner) {
            viewModel.setListPins()
        }


        viewModel.allPin.observe(viewLifecycleOwner) {
            viewModel.setListPins()
        }

        viewModel.listPins.observe(viewLifecycleOwner) {
            it.forEach { pin ->
                pin.setMap(map)
            }
        }

        viewModel.selectedPin.observe(viewLifecycleOwner) {
            it?.let {
                moveCameraWithZoom(it.getPosition())
            }
        }




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        Log.d("MainFragment", "on map ready")
        this.map = naverMap

        val circleManager =
            CircleManager(requireContext(), binding.fragmentMainTouchableLayout, naverMap)
        circleManager.setDrawEnable(circle, radiusMarker, 500.0, {
            viewModel.setCircleRadius(circle.radius.toInt())
        }) {
            viewModel.setAllPins {
                moveCameraWithZoom(it.getPosition())
                viewModel.setSelectedPin(it)
            }
        }




        //초기 카메라 이동(혹은 화면 회전 시 카메라 이동)
        val cameraPosition = naverMap.cameraPosition
        if (!isCameraAndSelectedPositionSame(cameraPosition))
            viewModel.selectedPosition.value?.let {
                Log.d("move","first ${it.latitude} ${it.longitude}")
                moveCamera(it)
                marker.position = it
                marker.map = map
            }

        naverMap.setOnMapClickListener { _, latLng ->
            if (viewModel.frontFragment.value != FragmentTag.FRAGMENT_LOCATION.fragment_num)
                return@setOnMapClickListener
            viewModel.setSelectedPosition(latLng)
            viewModel.isTracking=false
        }
    }


    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(latLng).animate(CameraAnimation.Easing, 500)
        map?.moveCamera(cameraUpdate)
    }

    private fun moveCameraWithZoom(latLng: LatLng) {
        val cameraUpdate =
            CameraUpdate.scrollAndZoomTo(latLng, 17.0).animate(CameraAnimation.Easing, 500)
        map?.moveCamera(cameraUpdate)
    }


    private fun isCameraAndSelectedPositionSame(cameraPosition: CameraPosition): Boolean {
        viewModel.selectedPosition.value?.let { selectedPosition ->
            val cPositionLat = cameraPosition.target.latitude
            val cPositionLng = cameraPosition.target.longitude
            val sPositionLat = selectedPosition.latitude
            val sPositionLng = selectedPosition.longitude
            if (cPositionLat == sPositionLat && cPositionLng == sPositionLng)
                return true
            return false
        } ?: run {
            return false
        }

    }

    private fun isMoveOnListFragment(num: Int): Boolean {
        if (beforeFragment == FragmentTag.FRAGMENT_LOCATION.fragment_num &&
            num == FragmentTag.FRAGMENT_FOOD_LIST.fragment_num
        ) {
            return true
        }
        return false
    }

    private fun isMoveOnLocationFragment(num: Int): Boolean {
        if (beforeFragment == FragmentTag.FRAGMENT_FOOD_LIST.fragment_num &&
            num == FragmentTag.FRAGMENT_LOCATION.fragment_num
        ) {
            return true
        }
        return false
    }

}