package moony

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class TouchableConstraintLayout:ConstraintLayout {
    private var onIntercept = { _: MotionEvent? -> false }
    private var onTouch = { _: MotionEvent? -> false }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        //true를 반환하면 Intercept
        if (onIntercept(ev))
            return true
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (onTouch(event))
            return true
        return super.onTouchEvent(event)
    }

    fun setOnInterceptTouchEvent(doing: (ev: MotionEvent?) -> Boolean) {
        onIntercept = doing

    }

    fun setOnTouchEvent(doing: (ev: MotionEvent?) -> Boolean) {
        onTouch = doing
    }

}