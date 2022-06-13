package com.kimleehanjang.lunchbox.refactoring.manager

import com.kimleehanjang.lunchbox.refactoring.LunchApplication

class Utils {

    companion object {

        fun dpToPixel(dp: Int): Int {
            val context = LunchApplication.getApplicationContext()
            return (dp * context.resources.displayMetrics.density).toInt()

        }

    }
}