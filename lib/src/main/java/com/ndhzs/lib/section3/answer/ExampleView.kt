package com.ndhzs.lib.section3.answer

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/19 15:23
 */

/**
 *
 * 这样继承代表你只允许在代码中动态生成
 * ```
 *     val view = ExampleView0(context)
 * ```
 */
class ExampleView0(
    context: Context
) : View(context) {
}

/**
 * 这样继承代表你只允许在 xml 中生成
 * ```
 *     <xxx.ExampleView1
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent" />
 * ```
 */
class ExampleView1(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {
}

/**
 *
 */
class ExampleView2 : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )
}

class ExampleView3 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
}