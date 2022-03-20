package com.ndhzs.lib.section6

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ndhzs.lib.R

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 19:30
 */
class MySection6View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var mCircleRadius = 100
    private var mCircleGravity = 0

    init {
        if (attrs != null) {
            val ty = context.obtainStyledAttributes(attrs, R.styleable.MySection6View)
            mCircleRadius = ty.getDimensionPixelSize(
                R.styleable.MySection6View_my6_circleRadius,
                mCircleRadius
            )
            mCircleGravity = ty.getInt(
                R.styleable.MySection6View_my6_circleGravity,
                mCircleGravity
            )
            ty.recycle()
        }
    }
}