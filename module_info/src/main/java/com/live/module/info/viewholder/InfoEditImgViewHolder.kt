package com.live.module.info.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.module.info.R

class InfoEditImgViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var mImg: ImageView = itemView.findViewById(R.id.iv_img)
    var tvState: TextView = itemView.findViewById(R.id.tv_state)
}