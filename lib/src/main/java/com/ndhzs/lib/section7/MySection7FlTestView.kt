package com.ndhzs.lib.section7

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/21 21:37
 */
class MySection7FlTestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
//        Log.d("ggg", "(MySection7FlTestView.kt:21)-->> " +
//                "wMode is AT_MOST = ${wMode == MeasureSpec.AT_MOST}")
        if (wMode == MeasureSpec.AT_MOST) {
            super.onMeasure(
                // 别忘了，这里填的 400 单位是 px
                MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
//            Log.d("ggg", "(MySection7FlTestView.kt:32)-->> " +
//                    "measureWidth = $measuredWidth")
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        Log.d("ggg", "(MySection7FlTestView.kt:48)-->> " +
                "visibility: ${visibility == INVISIBLE}")
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        Log.d("ggg", "(MySection7FlTestView.kt:54)-->> " +
                "Focus: $hasWindowFocus")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("ggg", "(MySection7FlTestView.kt:60)-->> " +
                "detach")
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}