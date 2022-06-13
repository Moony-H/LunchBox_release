package com.kimleehanjang.lunchbox.refactoring.data

import android.util.Log
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.refactoring.LunchApplication
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitRepository @Inject constructor(private val retrofitService: RetrofitService) {


    suspend fun getAddress(latLng: LatLng): AddressData? {

        return retrofitService.getAddressAsync(
            LunchApplication.getApplicationContext().getString(R.string.kakaoRestApiKey),
            latLng.longitude.toString(),
            latLng.latitude.toString()
        ).await()
    }


    fun getPlaceAsync(
        latLng: LatLng,
        keyword: String,
        radius: Int,
        page: Int
    ): Deferred<PlacesData> {
        return retrofitService.getPlaceAsync(
            LunchApplication.getApplicationContext().getString(R.string.kakaoRestApiKey),
            keyword,
            "FD6",
            latLng.longitude.toString(),
            latLng.latitude.toString(),
            radius,
            page,
            15,
            "accuracy"
        )

    }

    fun getRelativePlaceAsync(keyword: String): Deferred<PlacesData> {
        return retrofitService.getRelativePlaceAsync(
            LunchApplication.getApplicationContext().getString(R.string.kakaoRestApiKey),
            keyword
        )

    }


}