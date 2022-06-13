package com.kimleehanjang.lunchbox.refactoring.manager

import android.graphics.PointF
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

class MapUnitConverter {
    companion object {
        fun convertPixelToLatLngByRelativePosition(
            naverMap: NaverMap,
            marker: Marker,
            meter: Float
        ): LatLng {

            //마커의 위경도를 픽셀 위치로 바꾼 후 가져옴
            val pixelPositionAtMarker = naverMap.projection.toScreenLocation(marker.position)

            //미터 퍼 픽셀을 가져온 후, 떨어진 곳에 배치하기 위해 meter 를 상수로 나눈다.
            val xPixelHaveToMove = meter / naverMap.projection.metersPerPixel

            //픽셀 위치를 나타내는 객체 PointF로 만든다.
            val circlePinPixelLocation = PointF(
                (pixelPositionAtMarker.x + xPixelHaveToMove).toFloat(),
                pixelPositionAtMarker.y
            )

            //픽셀 위치를 위경도로 바꾼 후 반환

            return naverMap.projection.fromScreenLocation(circlePinPixelLocation)


        }


    }


}