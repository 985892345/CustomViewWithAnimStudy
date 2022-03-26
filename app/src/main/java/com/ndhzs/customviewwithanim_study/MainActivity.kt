package com.ndhzs.customviewwithanim_study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.ndhzs.lib.section7.MyDialogFragment
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ndhzs.lib.R.layout.layout_section7)

//        val dialog = MyDialog(this)
//        dialog.show()

//        val dialogFm = MyDialogFragment()
//        dialogFm.show(supportFragmentManager, "123")
        val view = findViewById<View>(com.ndhzs.lib.R.id.myView7)
//        view.post {
//            val width = view.width
//            val height = view.height
//        }
//
//        view.doOnLayout {
//            val width = view.width
//            val height = view.height
//        }

//        thread {
//            sleep(1000)
//            runOnUiThread {
//                view.invalidate()
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("ggg", "(MainActivity.kt:37)-->> " +
                "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ggg", "(MainActivity.kt:57)-->> " +
                "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ggg", "(MainActivity.kt:63)-->> " +
                "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ggg", "(MainActivity.kt:69)-->> " +
                "onDestroy")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }
}