package com.ndhzs.lib.section3.test

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ndhzs.lib.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_card0)

        FrameLayout(this, null, 0, R.style.myCardView_style)

        val frameLayout = FrameLayout(this)
        frameLayout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.setBackgroundColor(Color.BLACK)
    }
}