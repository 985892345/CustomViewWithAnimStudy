package com.ndhzs.lib.section4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 13:18
 */
class MySection4View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 这个 paint 你可以把它看成是“画笔”
    private val mBlackPaint = Paint().apply {
        color = Color.BLACK // 设置画笔颜色为黑色
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
        canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
        canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
        canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
        canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
        canvas?.drawRect(0F, 0F, 100F, 200F, mBlackPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}