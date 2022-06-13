package moony

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import com.kimleehanjang.lunchbox.refactoring.manager.Utils.Companion.dpToPixel

class UpLayout : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var nonTouchArea = listOf<Rect>()


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    return !isNonTouchArea(it.x, it.y)
                }
                else -> {

                }
            }


        }
        return super.onTouchEvent(event)

    }


    private fun isNonTouchArea(x: Float, y: Float): Boolean {

        nonTouchArea.forEach { rect ->
            if (y >= (rect.top) && y <= (rect.bottom) && x >= rect.left && x <= rect.right)
                return true
        }
        return false


    }

    fun setNonTouchArea(list: List<Rect>) {
        val newList = mutableListOf<Rect>()
        list.forEach { rect ->
            val newRect = Rect()
            newRect.top = dpToPixel(rect.top)
            newRect.bottom = dpToPixel(rect.bottom)
            newRect.left = dpToPixel(rect.left)
            newRect.right = dpToPixel(rect.right)
            newList.add(newRect)

        }

        nonTouchArea = newList


    }

}