package com.ndhzs.lib.section7

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.ndhzs.lib.R

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/22 17:37
 */
class MyDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_section7)
    }
}