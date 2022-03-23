package com.ndhzs.lib.section7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ndhzs.lib.R

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/22 17:15
 */
class MyDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return LayoutInflater.from(requireContext()).inflate(R.layout.layout_section7, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val density = requireContext().resources.displayMetrics.density
//        dialog?.window?.setLayout(
//            (density * 400).toInt(),
//            (density * 300).toInt()
//        )

        val lp = view.layoutParams
//        lp.width = (density * 400).toInt()
//        lp.height = (density * 300).toInt()
    }
}