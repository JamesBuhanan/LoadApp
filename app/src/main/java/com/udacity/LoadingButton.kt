package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var buttonBackgroundColor = 0
    private var buttonTextColor = 0

    private val buttonAnimator = ValueAnimator()

    private val downloadText = "Download"
    private val loadingText = "Downloading"

    val TAG = "LoadingButton"

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { prop, old, new ->

//        when (new) {
//            ButtonState.Loading -> {
//                startTheAnimation()
//            }
//            ButtonState.Completed -> {
//                stopAnimation()
//            }
//        }
    }

    private fun stopAnimation() {
        Log.d(TAG, "Stop animation")
        buttonAnimator.cancel()
        invalidate()
    }

    private fun startTheAnimation() {
        Log.d(TAG, "Start animation")
        buttonAnimator.apply {
            setFloatValues(0f, widthSize.toFloat())
            duration = 2500
            addUpdateListener { valueAnimator ->
                valueAnimator.apply {
                    animationProgress = animatedValue as Float
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    interpolator = LinearInterpolator()
                }
                invalidate()
            }
            start()
        }

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }
    @Volatile
    var animationProgress = 0.0f

    init {
        isClickable = true


        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonBackgroundColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            buttonTextColor = getColor(R.styleable.LoadingButton_buttonTextColor, 0)


        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = buttonBackgroundColor
        canvas?.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        val buttonText = if (buttonState == ButtonState.Loading) loadingText else downloadText

        if (buttonState == ButtonState.Loading) {
            paint.color = Color.BLUE
            canvas?.drawRect(
                0f, 0f,
                ((animationProgress)).toFloat(), heightSize.toFloat(), paint
            )
            paint.color = Color.GREEN
            canvas?.drawArc(
                RectF(
                    (heightSize / 16).toFloat(),
                    (heightSize / 16).toFloat(),
                    (heightSize * 14 / 16).toFloat(),
                    (heightSize * 14 / 16).toFloat()
                ), 0f,
                360 * (animationProgress / widthSize), true, paint
            )
        }
        paint.color = buttonTextColor
        canvas?.drawText(buttonText, (widthSize / 2).toFloat(), (heightSize / 2).toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}