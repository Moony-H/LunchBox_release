package com.kimleehanjang.lunchbox.refactoring.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PlacesData(
    //@SerializedName("meta")
    val meta: KeywordMeta,
    //@SerializedName("documents")
    val documents:MutableList<Place>
)


data class KeywordMeta(
    //@SerializedName("total_count")
    val total_count:Int,
    //@SerializedName("pageable_count")
    val pageable_count:Int,
    //@SerializedName("is_end")
    val is_end:Boolean
)


data class Place(
    //@SerializedName("place_name")
    val place_name:String,
    //@SerializedName("category_name")
    val category_name:String,
    //@SerializedName("phone")
    val phone:String,
    //@SerializedName("address_name")
    val address_name:String,
    //@SerializedName("road_address_name")
    val road_address_name:String,
    @SerializedName("x")
    val longitude:Double,//longitude
    @SerializedName("y")
    val latitude:Double,
    //@SerializedName("place_url")
    val place_url:String,
    //@SerializedName("distance")
    val distance:String
)
