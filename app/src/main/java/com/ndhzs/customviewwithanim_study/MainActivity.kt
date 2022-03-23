package com.ndhzs.customviewwithanim_study

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.postDelayed
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
        thread {
            sleep(1000)
            runOnUiThread {
                view.invalidate()
            }
        }

        View.inflate()
    }
}