package com.live.module.info.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.live.module.info.R
import com.mshy.VInterestSpeed.common.ui.view.video.NoClickControlVideo

/**
 * author: Tany
 * date: 2022/8/15
 * description:
 */
class  BannerImageVideoHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var videoView: NoClickControlVideo = itemView.findViewById(R.id.video)
    var ivImg: ImageView = itemView.findViewById(R.id.iv_img)
    var ivPlay: ImageView = itemView.findViewById(R.id.iv_play)
}