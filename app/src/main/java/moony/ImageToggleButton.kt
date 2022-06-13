package moony

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.kimleehanjang.lunchbox.R

class ImageToggleButton: AppCompatImageView ,View.OnClickListener {
    private var onImage: Drawable? = null
    private var offImage: Drawable? = null
    private var buttonState: Boolean = false
    private var onButtonStateChanged: (ImageToggleButton, Boolean) -> Unit =
        { _: ImageToggleButton, _: Boolean -> }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ImageToggleButton)
            //버튼이 활성화 되었을 때 사용할 이미지
            typeArray.getDrawable(R.styleable.ImageToggleButton_moony_onImage)?.let {
                onImage = it
            }

            //버튼이 비활성화 되었을 때 사용할 이미지
            typeArray.getDrawable(R.styleable.ImageToggleButton_moony_offImage)?.let {
                offImage = it
            }
            //초기에 활성화 되어 있을지 안되어 있을지 결정하기
            buttonState =
                typeArray.getBoolean(R.styleable.ImageToggleButton_moony_default_value, false)
            typeArray.recycle()


        }
        super.setOnClickListener(this)
        if (buttonState)
            setOn()
        else
            setOff()


    }

    override fun onFinishInflate() {

        super.onFinishInflate()
        //객체화 되었을 때 초기 값으로 설정.
        if (onImage == null) {
            throw IllegalStateException("ImageToggleButton/ please set OnImage")
        }
        if (offImage == null) {
            throw IllegalStateException("ImageToggleButton/ please set OffImage")
        }

    }

    override fun onClick(view: View?) {
        view?.let {
            if (buttonState) {
                setOff()
            } else {
                setOn()
            }
            onButtonStateChanged(this, buttonState)

        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        //super.setOnClickListener(l)
    }


    fun setToggleListener(callback: ImageToggleButtonCallback) {

        //view 의 onClickListener 가 작동할 때 같이 callback 작동.
        this.setOnClickListener { view ->
            if (!buttonState) {
                setOn()
                callback.buttonStateChanged(view as ImageToggleButton, buttonState)
            } else {
                setOff()
                callback.buttonStateChanged(view as ImageToggleButton, buttonState)
            }
        }
    }

    //혹시나 동적으로 생성할 때, 이미지 설정 가능한 메소드.
    fun setOnImage(image: Drawable) {
        onImage = image
        if (buttonState)
            setOff()
        else
            setOn()
    }

    //동적으로 생성할 때 이미지 설정 가능한 메소드.
    fun setOffImage(image: Drawable) {
        offImage = image
        if (buttonState)
            setOff()
        else
            setOn()
    }

    fun setOnButtonStateChanged(callback: (ImageToggleButton, Boolean) -> Unit) {
        onButtonStateChanged = callback
    }

    //On 이미지로 변경 후 버튼 상태 On 으로 변경
    fun setOn() {
        this.setImageDrawable(onImage)
        buttonState = true
    }

    //Off 이미지로 변경 후 버튼 상태 Off 로 변경
    fun setOff() {
        this.setImageDrawable(offImage)
        buttonState = false
    }

    //버튼 상태 확인
    fun getState(): Boolean {
        return buttonState
    }


}