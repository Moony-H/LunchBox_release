package com.kimleehanjang.lunchbox.refactoring.data


import android.annotation.SuppressLint
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.kimleehanjang.lunchbox.refactoring.manager.Utils
import com.kimleehanjang.lunchbox.refactoring.LunchApplication
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

//marker를 상속 받으면 쉽지만 못받으니 사용하는 메서드만 다시 정의하자.
class Pin(val place: Place,val foodEnum:Food) {

    private val marker = Marker()


    init {
        marker.position = LatLng(place.latitude, place.longitude)
        marker.icon = OverlayImage.fromResource(foodEnum.pinIconId)
        marker.height = Utils.dpToPixel(30)
        marker.width = Utils.dpToPixel(30)
        marker.anchor = PointF(0.5f, 0.5f)
    }

    fun setMap(map: NaverMap?) {
        marker.map = map
    }


    fun setOnPinClicked(onClick: (Pin) -> Unit) {
        marker.setOnClickListener {
            onClick(this)
            true
        }
    }

    fun getPosition(): LatLng {
        return marker.position
    }
}