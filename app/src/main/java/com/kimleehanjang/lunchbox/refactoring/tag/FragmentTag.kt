package com.kimleehanjang.lunchbox.refactoring.tag

enum class FragmentTag(val fragment_num:Int,val tag:String) {
    FRAGMENT_MAIN(0, "fragment_main"),
    FRAGMENT_LOCATION(1, "fragment_location"),
    FRAGMENT_FOOD_LIST(2, "fragment_food_list"),
    FRAGMENT_FOOD_DETAIL(3, "fragment_food_detail"),
    FRAGMENT_SEARCH(4, "fragment_search"),
    FRAGMENT_TUTORIAL(5, "fragment_tutorial"),
    FRAGMENT_START(6, "fragment_start")
}
