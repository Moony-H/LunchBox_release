package com.kimleehanjang.lunchbox.refactoring.data

//랜덤으로 하나 뽑기, 사용자가 고른 음식점 모아보기 등등 많은 기능이 "검색된 핀들"의 기능이니
//캡슐화 대상인 것 같다. 캡슐화로 기능을 넣어 놓자

class MapPins(val mapPins:List<List<Pin>>) {

    fun getListPins(buttonState: List<Boolean>): List<Pin> {
        val pins = mutableListOf<Pin>()
        buttonState.forEachIndexed { index, state ->
            if (state)
                pins.addAll(mapPins[index])
        }
        return pins
    }

}