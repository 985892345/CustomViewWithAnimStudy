package com.ndhzs.lib.section6

import androidx.annotation.IntDef

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 23:26
 */

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    CircleGravityKt.LEFT,
    CircleGravityKt.CENTER,
    CircleGravityKt.RIGHT,
)
annotation class CircleGravityKt {
    companion object {
        const val LEFT = 0
        const val CENTER = 1
        const val RIGHT = 2
    }
}
