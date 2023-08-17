package com.live.module.info.adapter

import android.view.View
import com.live.module.info.R
import com.live.module.info.bean.Album
import com.live.module.info.viewholder.InfoVquImgBannerViewHolder
import com.mshy.VInterestSpeed.common.ui.view.video.NoClickControlVideo
import com.zhpan.bannerview.BaseBannerAdapter

/**
 * Created by Tany on 2021/11/1.
 * Desc:
 */


class InfoVquImgBannerAdapter : BaseBannerAdapter<Album, InfoVquImgBannerViewHolder>() {
    var myVideo: NoClickControlVideo? = null
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.info_tanta_banner_info
    }

    override fun createViewHolder(itemView: View, viewType: Int): InfoVquImgBannerViewHolder {
        return InfoVquImgBannerViewHolder(itemView)
    }
    fun getVideoPlayer(): NoClickControlVideo? {
        return myVideo
    }
    override fun onBind(
        holder: InfoVquImgBannerViewHolder?,
        data: Album?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data,position, pageSize)
        if(position==0){
            myVideo = holder?.itemView?.findViewById<NoClickControlVideo>(R.id.video)
        }

    }
}