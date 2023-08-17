package com.live.module.info.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.info.R
import com.live.module.info.bean.GiftBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage

/**
 * author: Tany
 * date: 2022/4/27
 * description:
 */
class InfoVquGiftAdapter(data:MutableList<GiftBean>) :BaseQuickAdapter<GiftBean,BaseViewHolder>(R.layout.info_tanta_item_gift,data){
    override fun convert(holder: BaseViewHolder, item: GiftBean) {
        holder.getView<ImageView>(R.id.iv_gift).vquLoadImage(NetBaseUrlConstant.IMAGE_URL+item.img)
        holder.getView<TextView>(R.id.tv_gift).text=item.name
        holder.getView<TextView>(R.id.tv_count).text="x"+item.total

    }
}