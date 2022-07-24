package com.kimleehanjang.lunchbox.refactoring.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimleehanjang.lunchbox.refactoring.data.*
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val retrofitRepository: RetrofitRepository): ViewModel() {

    //지금 가장 앞에 나와있는, 유저와 상호작용 하고 있는 fragment
    private val _frontFragment = MutableLiveData<Int>()
    val frontFragment: LiveData<Int>
        get() = _frontFragment

    //gps 위치
    private val _userPosition = MutableLiveData<LatLng>()
    val userPosition: LiveData<LatLng>
        get() = _userPosition

    //사용자가 검색을 원하는 위치
    private val _selectedPosition = MutableLiveData<LatLng>()
    val selectedPosition: LiveData<LatLng>
        get() = _selectedPosition

    //위치의 주소 값
    private val _selectedAddress = MutableLiveData<String>()
    val selectedAddress: LiveData<String>
        get() = _selectedAddress


    //검색할 원의 반경
    private val _circleRadius = MutableLiveData(100)
    val circleRadius: LiveData<Int>
        get() = _circleRadius


    //검색한 반경 안의 음식점들
    private val _allPin = MutableLiveData<MapPins>()
    val allPin: LiveData<MapPins>
        get() = _allPin

    //recycler view 에 넣을 list
    private val _listPins = MutableLiveData<List<Pin>>()
    val listPins: LiveData<List<Pin>>
        get() = _listPins

    //사용자가 궁금해 하는 음식점
    private val _selectedPin = MutableLiveData<Pin?>()
    val selectedPin: LiveData<Pin?>
        get() = _selectedPin

    var isTracking=true


    private val _buttonState = MutableLiveData<List<Boolean>>(
        mutableListOf(
            true,
            false,
            false,
            false,
            false,
            false,
            false
        )
    )
    val buttonState: LiveData<List<Boolean>>
        get() = _buttonState

    fun setFrontFragment(fragmentTagNumber: Int) {
        _frontFragment.value = fragmentTagNumber
    }


    fun setCircleRadius(radius: Int) {
        _circleRadius.value = radius
    }

    fun setListPins() {
        Log.d("button", "state ${_buttonState.value}")
        _allPin.value?.let { mapPins ->
            if (mapPins.mapPins.isNotEmpty()) {
                val pins = mapPins.getListPins(_buttonState.value!!)
                //핀을 맵에서 지우기
                _listPins.value?.let { beforePins ->
                    beforePins.forEach {
                        it.setMap(null)
                    }
                }
                //핀 다시 지정.
                _listPins.value = pins
            }


        }

    }


    fun setButtonState(buttonStates: List<Boolean>) {
        _buttonState.value = buttonStates
    }

    fun setSelectedPin(pin: Pin?) {
        _selectedPin.value = pin
    }

    fun setSelectedPinRandom() {
        val random = Random()
        _listPins.value?.let {
            _selectedPin.value = it[random.nextInt(it.size)]
        }
    }

    fun setUserPosition(latLng: LatLng) {
        _userPosition.value = latLng

    }

    fun setSelectedPosition(latLng: LatLng) {
        _selectedPosition.value = latLng
    }


    fun setSelectedAddress(latLng: LatLng) {
        var result = ""
        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            val response = retrofitRepository.getAddress(latLng)
            response?.let {
                result = if (it.documents.isEmpty())
                    "주소 없음"
                else {
                    val road = it.documents[0].road_address
                    val address = it.documents[0].address
                    if (road != null && road.address_name != "")
                        road.address_name
                    else if (address != null && address.address_name != "")
                        address.address_name
                    else
                        "주소 없음"
                }

            } ?: run {
                result = "주소 없음"
            }
            _selectedAddress.postValue(result)
        }

    }

    fun setAllPinsNull() {
        val temp = mutableListOf<List<Pin>>()
        _allPin.value = MapPins(temp)
    }

    fun setListPinNull() {
        _listPins.value?.forEach { pin ->
            pin.setMap(null)
        }
        _listPins.value = mutableListOf<Pin>()
    }

    fun setAllPins(onPinClicked: (Pin) -> Unit) {


        viewModelScope.launch(Dispatchers.IO+coroutineExceptionHandler) {
            if (_selectedPosition.value != null && _circleRadius.value != null) {

                val awaitList = ArrayList<Deferred<List<Place>>>()
                Food.values().forEachIndexed { _, food ->
                    awaitList.add(getOneMenuPin(food.krName))
                }
                val response = awaitList.awaitAll()

                val result = mutableListOf<List<Pin>>()
                val foodList = Food.values()

                response.forEachIndexed { index, list ->
                    val oneMenuPins = mutableListOf<Pin>()
                    list.forEach {
                        val pin = Pin(it, foodList[index])
                        pin.setOnPinClicked(onPinClicked)
                        oneMenuPins.add(pin)
                    }
                    result.add(oneMenuPins)
                }

                _allPin.postValue(MapPins(result))
            }


        }


    }

    private suspend fun getOneMenuPin(keyword: String): Deferred<List<Place>> {

        val wait = viewModelScope.async(Dispatchers.IO+coroutineExceptionHandler) {
            val result = mutableListOf<Place>()
            if (_selectedPosition.value != null && _circleRadius.value != null) {
                var page = 1
                while (isActive) {
                    val response = retrofitRepository.getPlaceAsync(
                        _selectedPosition.value!!,
                        keyword,
                        _circleRadius.value!!,
                        page
                    )
                    val oneMenu = response.await()
                    result.addAll(oneMenu.documents)
                    if (!oneMenu.meta.is_end)
                        page++
                    else {
                        Log.d("api request stop", "cause: is_end")
                        break
                    }


                    if (page > 3) {
                        break
                    }


                }

            }
            return@async result
        }
        return wait
    }

    private val coroutineExceptionHandler= CoroutineExceptionHandler{_, throwable->
        throwable.printStackTrace()

    }

}