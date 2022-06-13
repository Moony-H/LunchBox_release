package com.kimleehanjang.lunchbox.refactoring.manager

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kimleehanjang.lunchbox.R
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import moony.TouchableConstraintLayout
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

class CircleManager(private val context: Context, private val parentLayout:TouchableConstraintLayout, private val naverMap: NaverMap) {


    var sensitivity = 50.0F


    //이 클래스의 기능: 원을 그리는 메커니즘(점을 끌어서 그것을 반지름으로 사용하는 원을 그리는 메커니즘)을 직접 구현.
    //지도에 touch 이벤트가 가지 않기 위해(원을 그릴 땐, 지도가 움직이지 않아야 한다.), 지도의 parent layout 이 Intercept touch event 를 클래스 외부에서 재정의 가능한
    //custom constraint layout 이다.

    //따라서 클래스 외부에서 parent layout 에  조건을 주어 onInterceptTouchEvent 를 발생시킬 수 있다.


    fun setDrawEnable(
        circle: CircleOverlay,
        circleMarker: Marker,
        radiusLimit: Double,
        toDoOnDrawing: (event: MotionEvent) -> Unit,
        toDoOnFinished: () -> Unit
    ) {


        //처음 핀 위치 저장
        var circleMarkerLocation: PointF
        var circleMarkerLocationX: Float
        var circleMarkerLocationY: Float

        //둘중에 큰것을 설정.
        sensitivity = if (circleMarker.width > circleMarker.height)
            circleMarker.width.toFloat() / 3
        else
            circleMarker.height.toFloat() / 3


        //센터 마커(계란이) 위치 저장.
        var circleCenterPoint = naverMap.projection.toScreenLocation(circle.center)


        //지도에 touch 가 가지 않기 위해, intercept 조건 추가.
        parentLayout.setOnInterceptTouchEvent { event ->
            event?.let {
                if (it.action == MotionEvent.ACTION_DOWN) {
                    circleMarkerLocation =
                        naverMap.projection.toScreenLocation(circleMarker.position)
                    circleMarkerLocationX = circleMarkerLocation.x
                    circleMarkerLocationY = circleMarkerLocation.y

                    //드래그 되는 점(원을 그리는 점)을 클릭하였을 때
                    if (
                        it.x <= circleMarkerLocationX + sensitivity &&
                        it.x >= circleMarkerLocationX - sensitivity &&
                        it.y <= circleMarkerLocationY + sensitivity &&
                        it.y >= circleMarkerLocationY - sensitivity
                    ) {
                        //intercept 를 true 로 하여 onTouchEvent 를 발동
                        circle.color = ContextCompat.getColor(context, R.color.main_half)
                        circleCenterPoint = naverMap.projection.toScreenLocation(circle.center)
                        return@setOnInterceptTouchEvent true
                    } else {

                        //isTouched=false
                    }
                }


            }
            false

        }
        parentLayout.setOnTouchEvent { event ->

            event?.let {
                when (it.action) {
                    MotionEvent.ACTION_MOVE -> {
                        toDoOnDrawing(it)
                        //중심 점과 드래그 가능한 마커의 x,y 차이 저장.
                        val dx = (circleCenterPoint.x - it.x).toDouble()
                        val dy = (circleCenterPoint.y - it.y).toDouble()

                        //x,y로 반지름 길이(중심 점과 마커의 거리) 측정
                        val radPx = sqrt(dx.pow(2.0) + dy.pow(2.0))

                        //미터를 픽셀 길이로 변환
                        val radiusLimitPx = radiusLimit / naverMap.projection.metersPerPixel

                        //각도 구하기
                        val degree = atan2(dy, dx)

                        //최대 반경은 500m 이다. 따라서 그 안쪽이면 원을 늘리는 것을 허용
                        if (radPx > radiusLimitPx) {


                            val x = radiusLimitPx * kotlin.math.cos(degree) * -1
                            val y = radiusLimitPx * kotlin.math.sin(degree) * -1

                            val circleCenterPx = naverMap.projection.toScreenLocation(circle.center)
                            val latLng = naverMap.projection.fromScreenLocation(
                                PointF(
                                    x.toFloat() + circleCenterPx.x,
                                    y.toFloat() + circleCenterPx.y
                                )
                            )
                            circleMarker.position = latLng
                            circle.radius = radiusLimit
                        }
                        //최대 반경을 넘어가면 사용자의 움직임의 각도만 따라가고, 원의 크기는 늘어나지 않는다.
                        else {

                            //사용자가 터치한 위치와 중심점의 거리가 리미트 보다 작으면 마커는 사용자의 터치 움직임을 따라간다.
                            circleMarker.position =
                                naverMap.projection.fromScreenLocation(PointF(it.x, it.y))
                            circle.radius = circle.center.distanceTo(circleMarker.position)
                        }

                    }

                    MotionEvent.ACTION_UP -> {
                        toDoOnFinished()
                        //touched=false
                        circle.color = ContextCompat.getColor(context, R.color.transparent_main)

                    }
                }


            }
            true

        }


    }

    fun setDrawDisable() {
        parentLayout.setOnInterceptTouchEvent {
            false
        }
        parentLayout.setOnTouchEvent {
            false
        }

    }


}


