package com.ndhzs.lib.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ndhzs.lib.R

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/25 17:20
 */
class RvAdapter : RecyclerView.Adapter<RvAdapter.RvVH>() {
    class RvVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mAnimationView: AnimationView = itemView.findViewById(R.id.item_animation)
    }

    fun getVHolder(position: Int, call: VHolderCallback) {
        notifyItemChanged(position, call)

//        getVHolder(1) {
//            // 开始动画
//            it.setIsRecyclable(false)
//            it.mAnimationView.startAnim()
//        }
//        notifyItemChanged(1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvVH {
        return RvVH(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RvVH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                if (it is VHolderCallback) {
                    it.call(holder)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RvVH, position: Int) {
    }

    override fun getItemCount(): Int = 100

    fun interface VHolderCallback {
        fun call(holder: RvVH)
    }
}