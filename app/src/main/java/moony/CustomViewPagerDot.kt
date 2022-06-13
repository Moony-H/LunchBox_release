package moony

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.kimleehanjang.lunchbox.R

class CustomViewPagerDot(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var viewPagerResId = -1
    private lateinit var viewPager: ViewPager2
    private val mContext = context

    init {
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomViewPagerDot)
            viewPagerResId =
                typeArray.getResourceId(R.styleable.CustomViewPagerDot_moony_viewPagerId, -1)


            typeArray.recycle()
        }

    }

    override fun onFinishInflate() {

        super.onFinishInflate()
        if (viewPagerResId == -1) {
            throw IllegalStateException("set attribute moony_viewPagerId")
        } else {
            viewPager = findViewById(viewPagerResId)
        }

    }
}