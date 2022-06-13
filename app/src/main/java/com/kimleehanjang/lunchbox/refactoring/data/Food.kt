package com.kimleehanjang.lunchbox.refactoring.data

import androidx.core.content.ContextCompat
import com.kimleehanjang.lunchbox.R

enum class Food(
    val num:Int,
    val krName:String,
    val engName:String,
    val pinIconId:Int,
    val normalIconImageId:Int,
    val buttonOnImageId:Int,
    val buttonOffImageId:Int,
    ) {
    KOREAN(
        0,
        "한식",
        "KOREAN",
        R.drawable.image_map_korean_food_marker,
        R.drawable.image_icon_korean_food,
        R.drawable.image_icon_korean_food_button_on,
        R.drawable.image_icon_korean_food_button_off
    ),

    WESTERN(
        1,
        "양식",
        "WESTERN",
        R.drawable.image_map_western_food_marker,
        R.drawable.image_icon_western_food,
        R.drawable.image_icon_western_food_button_on,
        R.drawable.image_icon_western_food_button_off
    ),

    CHINESE(
        2,
        "중식",
        "CHINESE",
        R.drawable.image_map_chinese_food_marker,
        R.drawable.image_icon_chinese_food,
        R.drawable.image_icon_chinese_food_button_on,
        R.drawable.image_icon_chinese_food_button_off
    ),

    JAPANESE(
        3,
        "일식",
        "JAPANESE",
        R.drawable.image_map_japanese_food_marker,
        R.drawable.image_icon_japanese_food,
        R.drawable.image_icon_japanese_food_button_on,
        R.drawable.image_icon_japanese_food_button_off
    ),

    DINER(
        4,
        "분식",
        "DINER",
        R.drawable.image_map_diner_food_marker,
        R.drawable.image_icon_diner_food,
        R.drawable.image_icon_diner_food_button_on,
        R.drawable.image_icon_diner_food_button_off
    ),

    CHICKEN(
        5,
        "치킨",
        "CHICKEN",
        R.drawable.image_map_chicken_food_marker,
        R.drawable.image_icon_chicken_food,
        R.drawable.image_icon_chicken_food_button_on,
        R.drawable.image_icon_chicken_food_button_off
    ),

    DESSERT(
        6,
        "간식",
        "DESSERT",
        R.drawable.image_map_dessert_food_marker,
        R.drawable.image_icon_dessert_food,
        R.drawable.image_icon_dessert_food_button_on,
        R.drawable.image_icon_dessert_food_button_off
    )

}