package com.ndhzs.lib.section6

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StyleableRes
import androidx.core.content.ContextCompat

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/1/13
 */
internal interface BaseViewAttrs {

    fun <T> newAttrs(
        view: View,
        attrs: AttributeSet?,
        @StyleableRes
        styleableId: IntArray,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
        func: Typedef.() -> T
    ): T = Companion.newAttrs(view, attrs, styleableId, defStyleAttr, defStyleRes, func)

    companion object {
        fun <T> newAttrs(
            view: View,
            attrs: AttributeSet?,
            @StyleableRes
            styleableId: IntArray,
            defStyleAttr: Int = 0,
            defStyleRes: Int = 0,
            func: Typedef.() -> T
        ): T {
            val ty = view.context.obtainStyledAttributes(attrs, styleableId, defStyleAttr, defStyleRes)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 这是保存在 Debug 模式中能看到的信息，具体怎么查看，你可以去看看这个方法的源码
                view.saveAttributeDataForStyleable(view.context, styleableId, attrs, ty, defStyleAttr, defStyleRes)
            }
            try {
                return Typedef(ty, view.context).func()
            } finally {
                ty.recycle()
            }
        }
    }

    class Typedef(val ty: TypedArray, private val context: Context) {
        fun Int.int(defValue: Int): Int = this.int(ty, defValue)
        fun Int.color(defValue: Int): Int = this.color(ty, defValue)
        fun Int.colorById(@ColorRes defValueId: Int): Int = this.color(ContextCompat.getColor(context, defValueId))
        fun Int.dimens(defValue: Int): Int = this.dimens(ty, defValue)
        fun Int.dimens(defValue: Float): Float = this.dimens(ty, defValue)
        fun Int.layoutDimens(defValue: Int): Int = this.layoutDimens(ty, defValue)
        fun Int.dimensById(@DimenRes defValueId: Int): Int = this.dimens(context.resources.getDimensionPixelSize(defValueId))
        fun Int.string(defValue: String? = null): String = this.string(ty, defValue)
        fun Int.boolean(defValue: Boolean): Boolean = this.boolean(ty, defValue)
        fun Int.float(defValue: Float): Float = this.float(ty, defValue)
        internal inline fun <reified E: RuntimeException> Int.intOrThrow(
            attrsName: String): Int = this.intOrThrow<E>(ty, attrsName)
        internal inline fun <reified E: RuntimeException> Int.stringOrThrow(
            attrsName: String): String = this.stringOrThrow<E>(ty, attrsName)
    }
}

internal fun Int.int(ty: TypedArray, defValue: Int): Int {
    return ty.getInt(this, defValue)
}

internal fun Int.color(ty: TypedArray, defValue: Int): Int {
    return ty.getColor(this, defValue)
}

internal fun Int.dimens(ty: TypedArray, defValue: Int): Int {
    return ty.getDimensionPixelSize(this, defValue)
}

internal fun Int.dimens(ty: TypedArray, defValue: Float): Float {
    return ty.getDimension(this, defValue)
}

internal fun Int.layoutDimens(ty: TypedArray, defValue: Int): Int {
    return ty.getLayoutDimension(this, defValue)
}

internal fun Int.string(ty: TypedArray, defValue: String? = null): String {
    return ty.getString(this) ?: defValue ?: ""
}

internal fun Int.boolean(ty: TypedArray, defValue: Boolean): Boolean {
    return ty.getBoolean(this, defValue)
}

internal fun Int.float(ty: TypedArray, defValue: Float): Float {
    return ty.getFloat(this, defValue)
}

internal inline fun <reified E: RuntimeException> Int.intOrThrow(
    ty: TypedArray, attrsName: String
): Int {
    if (!ty.hasValue(this)) {
        throw E::class.java.getConstructor(String::class.java)
            .newInstance("属性 $attrsName 没有被定义！")
    }
    return this.int(ty, 0)
}

internal inline fun <reified E: java.lang.RuntimeException> Int.stringOrThrow(
    ty: TypedArray, attrsName: String
): String {
    if (!ty.hasValue(this)) {
        throw E::class.java.getConstructor(String::class.java)
            .newInstance("属性 $attrsName 没有被定义！")
    }
    return this.string(ty)
}