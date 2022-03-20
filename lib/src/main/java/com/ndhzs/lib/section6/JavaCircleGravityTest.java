package com.ndhzs.lib.section6;

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 22:07
 */
public class JavaCircleGravityTest {
    void test1(@CircleGravityKt int a) {

    }

    void test2() {
        // 真奇怪，java 这里很报错，但 kt 那里不会
        test1(999);
    }
}
