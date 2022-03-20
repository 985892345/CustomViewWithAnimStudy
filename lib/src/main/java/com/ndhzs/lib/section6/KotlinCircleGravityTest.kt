package com.ndhzs.lib.section6

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 22:09
 */
class KotlinCircleGravityTest {
    fun test1(@CircleGravityKt a: Int) {

    }

    /**
     * 看到了吧，java 能起作用，但 kt 就是不起作用，我认为是编译器没有默认实现这个检查功能
     *
     * 这是在 StackOverFlow 上对于这个问题的讨论：
     * https://stackoverflow.com/questions/37833395/kotlin-annotation-intdef
     * https://stackoverflow.com/questions/35976002/how-to-use-android-support-typedef-annotations-in-kotlin
     *
     * 他们说到可以使用枚举来解决，但枚举较占空间，官方也不推荐我们使用枚举：
     * https://developer.android.com/topic/performance/reduce-apk-size
     * https://i.stack.imgur.com/QrDoh.png
     */
    fun test2() {
        test1(999)
    }

    fun test3(@CircleGravityJava a: Int) {

    }

    /**
     * 但使用 java 来写注解就没得问题
     */
    fun test4() {
        test3(999)
    }
}