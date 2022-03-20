package com.ndhzs.lib.section6

import android.util.AttributeSet
import android.view.View
import com.ndhzs.lib.R
import com.ndhzs.lib.section6.BaseViewAttrs.Companion.newAttrs

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 21:37
 */
class MySection6Attrs(
    circleRadius: Int,
    circleGravity: CircleGravityEnum
) : BaseViewAttrs {
    companion object {
        fun newInstance(
            view: View,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int
        ): MySection6Attrs {
            return newAttrs(view, attrs, R.styleable.MySection6View, defStyleAttr, defStyleRes) {
                MySection6Attrs(
                    R.styleable.MySection6View_my6_circleRadius.dimens(CIRCLE_RADIUS),
                    R.styleable.MySection6View_my6_circleGravity.circleGravity(ty)
                )
            }
        }

        const val CIRCLE_RADIUS = 100
    }
}