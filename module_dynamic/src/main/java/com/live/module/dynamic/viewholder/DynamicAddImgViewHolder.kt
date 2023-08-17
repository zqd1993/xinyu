package com.live.module.dynamic.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.live.module.dynamic.R

class DynamicAddImgViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var mImg: ImageView = itemView.findViewById(R.id.iv_img)
    var mIvDel: ImageView = itemView.findViewById(R.id.iv_del)
}