package com.kimleehanjang.lunchbox.refactoring.util

import android.content.Context
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.kimleehanjang.lunchbox.R
import com.kimleehanjang.lunchbox.refactoring.manager.Utils
import com.kimleehanjang.lunchbox.refactoring.data.DynamicViewId
import com.kimleehanjang.lunchbox.refactoring.data.Food
import moony.ImageToggleButton

class ToggleButtonFactory {

    fun getButtons(
        context: Context,
        callback: (imageToggleButton: ImageToggleButton, buttonState: Boolean) -> Unit
    ): List<ImageToggleButton> {
        val foodButtons = mutableListOf<ImageToggleButton>()

        //의도: 핸드폰 화면 크기에 상관 없이, 내가 지정한 아이콘 너비, 아이콘 최소 간격으로 몇개나 들어가는 지 계산 후, 꼭 맞게 넣는다.
        //(항상 아이콘은 깔끔히 정렬된 상태로 집어 넣어 진다.)

        val dm = context.resources.displayMetrics
        //사용할 수 있는 스크롤 뷰 너비
        val usableWidth =
            dm.widthPixels - (context.resources.getDimension(R.dimen.margin_select_slideLayout_recyclerView_left_and_right) * 2)

        //토글 버튼의 너비
        val iconWidth = context.resources.getDimension(R.dimen.size_select_button_icon_width)

        //토글 버튼의 높이
        val iconHeight = context.resources.getDimension(R.dimen.size_select_button_icon_height)
        //최소 갭
        val leastGap = Utils.dpToPixel(50)

        var iconNumber = (usableWidth / (iconWidth + leastGap)).toInt()
        if (usableWidth - iconNumber * (iconWidth + leastGap) + iconWidth >= 0)
            iconNumber++

        //계산된 아이콘 간격
        val iconGap = ((usableWidth - iconNumber * iconWidth) / (iconNumber - 1)).toInt()
        Food.values().forEachIndexed { index, food ->
            val button = ImageToggleButton(context)
            button.setOnImage(ContextCompat.getDrawable(context, food.buttonOnImageId)!!)
            button.setOffImage(ContextCompat.getDrawable(context, food.buttonOffImageId)!!)
            val layoutParams = LinearLayout.LayoutParams(iconWidth.toInt(), iconHeight.toInt())

            //마지막 버튼은 marginRight 없음
            if (index != Food.values().size - 1) {
                layoutParams.rightMargin = iconGap
            }


            button.layoutParams = layoutParams
            button.id = DynamicViewId.FOOD_BUTTON_BASE_ID + food.num
            button.setOnButtonStateChanged(callback)
            foodButtons.add(button)
        }
        return foodButtons
    }
}