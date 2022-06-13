package com.kimleehanjang.lunchbox.refactoring.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimleehanjang.lunchbox.refactoring.data.PlacesData
import com.kimleehanjang.lunchbox.refactoring.data.RetrofitRepository
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val retrofitRepository:RetrofitRepository): ViewModel() {
    private val _searchResult = MutableLiveData<PlacesData>()
    val searchResult: LiveData<PlacesData>
        get() = _searchResult

    fun searchPlaceAsync(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = retrofitRepository.getRelativePlaceAsync(query)
            Log.d("SearchViewModel", "request place")
            //사용자가 검색 창에 텍스트를 쓸 때 마다 결과 값을 줄 것인데,
            //만약 사용자가 악의적으로 텍스트를 마구잡이로 변화 시킬 때, 너무 많은 api 호출이 일어나지 않게 하기 위해
            //delay 를 사용한다.
            delay(500)
            _searchResult.postValue(response.await())
        }
    }
}