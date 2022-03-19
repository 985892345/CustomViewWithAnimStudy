package com.ndhzs.lib.section3.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ndhzs.lib.R;

/**
 * ...
 *
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/19 20:23
 */
public class Section3View extends View {
    public Section3View(Context context) {
        // 注意这里调用的是 this，而不是 super
        this(context, null);
    }

    public Section3View(Context context, @Nullable AttributeSet attrs) {
        // 注意这里调用的是 this，而不是 super
        this(context, attrs, R.attr.mySection3View_attrs);
    }

    public Section3View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        // 注意这里调用的是 this，而不是 super
        this(context, attrs, defStyleAttr, R.style.mySection3View_defaultAttrs);
    }

    public Section3View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        // 直到这里才调用 super
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
