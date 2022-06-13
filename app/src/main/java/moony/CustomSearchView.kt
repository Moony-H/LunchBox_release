package moony

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kimleehanjang.lunchbox.R

class CustomSearchView:ConstraintLayout {

    private lateinit var imageView: ImageView
    private lateinit var iconImage: Drawable
    private var font: Typeface? = null


    private var hint: String = "장소 검색"
    private var hintSize = 55F
    var editText: EditText? = null
    private var textView: TextView? = null
    private var isImage = false


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomSearchView)
            val image = typeArray.getDrawable(R.styleable.CustomSearchView_moony_searchView_icon)

            if (image == null) {
                throw IllegalStateException("Image ResourceId is not defined")
            } else {
                iconImage = image
            }

            isImage =
                typeArray.getBoolean(R.styleable.CustomSearchView_moony_searchView_isImage, false)
            hint =
                typeArray.getString(R.styleable.CustomSearchView_moony_searchView_Hint).toString()
            hintSize = typeArray.getDimension(
                R.styleable.CustomSearchView_moony_searchView_HintTextSize,
                20F
            )


            typeArray.recycle()


        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFinishInflate() {
        super.onFinishInflate()
        val childNum = childCount;
        if (childNum > 0)
            throw IllegalStateException("Custom SearchView cannot have child")

        val imageViewHeight = ((hintSize * 27) / 22).toInt()
        val imageViewWidth = (imageViewHeight * 28) / 27
        setImageView(iconImage, imageViewWidth, imageViewHeight)

        if (isImage)
            setTextView()
        else
            setEditTextView()


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    private fun setTextView() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.topToTop = LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = LayoutParams.PARENT_ID
        layoutParams.leftToLeft = LayoutParams.PARENT_ID
        layoutParams.rightToRight = LayoutParams.PARENT_ID
        textView = TextView(context)
        textView?.let { textView ->
            textView.layoutParams = layoutParams
            textView.text = hint
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintSize)
            textView.setTextColor(ContextCompat.getColor(context, R.color.gray))
            //textView.text
            //textView.typeface=resources.getFont(R.font.nanum_square_round_b)
            this.addView(textView)

        }


    }


    private fun setEditTextView() {
        val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.topToTop = LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = LayoutParams.PARENT_ID
        layoutParams.leftToLeft = LayoutParams.PARENT_ID
        layoutParams.rightToRight = LayoutParams.PARENT_ID
        editText = EditText(context)

        editText?.let { editText ->
            editText.layoutParams = layoutParams
            editText.hint = hint
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintSize)
            editText.setTextColor(ContextCompat.getColor(context, R.color.gray))
            editText.setHintTextColor(ContextCompat.getColor(context, R.color.gray))
            editText.background = null
            editText.isSingleLine = true
            //editText.typeface=resources.getFont(R.font.nanum_square_round_b)
            this.addView(editText)

        }
    }

    private fun setImageView(image: Drawable, width: Int, height: Int) {
        val layoutParams = LayoutParams(width, height)
        layoutParams.topToTop = LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = LayoutParams.PARENT_ID
        layoutParams.leftToLeft = LayoutParams.PARENT_ID
        layoutParams.leftMargin = (dpToPixel(18))
        imageView = ImageView(context)
        imageView.setImageDrawable(image)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = layoutParams

        this.addView(imageView)


    }

    fun setEditTextCallback(callback: TextWatcher) {
        editText?.addTextChangedListener(callback) ?: run {

        }
    }

    private fun dpToPixel(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }


}