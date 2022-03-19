package com.ndhzs.lib.section2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.ndhzs.lib.R

class ViewClassifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_classify)

        val textView: TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.button)

        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)
        val frameLayout: FrameLayout = findViewById(R.id.frameLayout)
    }
}