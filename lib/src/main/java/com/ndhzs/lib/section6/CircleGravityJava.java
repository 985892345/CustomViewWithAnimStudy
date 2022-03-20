package com.ndhzs.lib.section6;

import static java.lang.annotation.RetentionPolicy.SOURCE;
import androidx.annotation.IntDef;
import java.lang.annotation.Retention;

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/20 23:29
 */
@Retention(SOURCE)
@IntDef({CircleGravityJava.LEFT, CircleGravityJava.CENTER, CircleGravityJava.RIGHT})
public @interface CircleGravityJava {
    public static int LEFT = 0;
    public static int CENTER = 1;
    public static int RIGHT = 2;
}

