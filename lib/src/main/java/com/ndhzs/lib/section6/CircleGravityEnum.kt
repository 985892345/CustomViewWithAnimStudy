package com.ndhzs.lib.section6

import android.content.res.TypedArray

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 21:38
 */
enum class CircleGravityEnum(val i: Int) {
    LEFT(0),
    CENTER(1),
    RIGHT(2);

    companion object {
        fun default() = CENTER

        fun get(i: Int): CircleGravityEnum {
            return when (i) {
                LEFT.i -> LEFT
                CENTER.i -> CENTER
                RIGHT.i -> RIGHT
                else -> default()
            }
        }
    }
}

fun Int.circleGravity(ty: TypedArray): CircleGravityEnum {
    return CircleGravityEnum.get(ty.getInt(this, CircleGravityEnum.default().i))
}
