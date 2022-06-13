package com.kimleehanjang.lunchbox.refactoring.data


import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {

    @GET("v2/local/search/keyword.json")

    //주위 반경까지 검색(음식 랜덤 돌릴때 사용) 페이지 있음.
    fun getPlaceAsync(
        @Header("Authorization") key: String,
        @Query("query") Keyword: String,
        @Query("category_group_code") category_group_code: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String,
        @Query("radius") radius: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String

    ): Deferred<PlacesData>

    //장소 검색용
    @GET("v2/local/search/keyword.json")
    fun getRelativePlaceAsync(
        @Header("Authorization") key: String,
        @Query("query") Keyword: String,
    ): Deferred<PlacesData>

    //주소로 변환
    @GET("/v2/local/geo/coord2address.json")
    fun getAddressAsync(
        @Header("Authorization") key: String,
        @Query("x") longitude: String,
        @Query("y") latitude: String
    ): Deferred<AddressData?>


}
