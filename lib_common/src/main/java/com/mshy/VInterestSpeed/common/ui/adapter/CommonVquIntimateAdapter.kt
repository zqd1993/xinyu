package com.mshy.VInterestSpeed.common.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.CommonIntimateBean
import com.mshy.VInterestSpeed.common.ext.vquLoadImage

/**
 * author: Tany
 * date: 2022/6/28
 * description:
 */
class CommonVquIntimateAdapter(data:MutableList<CommonIntimateBean>) :
    BaseQuickAdapter<CommonIntimateBean, BaseViewHolder>(R.layout.common_vqu_item_intimate,data) {
    override fun convert(holder: BaseViewHolder, item: CommonIntimateBean) {
        var tvRank = holder.getView<TextView>(R.id.iv_rank)
        var tvIntimate = holder.getView<TextView>(R.id.tv_intimate)
        var tvContent = holder.getView<TextView>(R.id.tv_content)
        var tvState = holder.getView<ImageView>(R.id.tv_state)
        var ivState = holder.getView<ImageView>(R.id.iv_state)
        tvRank.text = (holder.absoluteAdapterPosition + 1).toString()
        tvRank.isSelected = item.unlock == 1
        ivState.vquLoadImage(item.iconUrl)
        tvIntimate.text = item.title
        tvIntimate.isSelected = item.unlock == 1
        tvState.setImageResource(if (item.unlock == 1) {
            R.mipmap.ic_tanta_intimate_unlock
        } else {
            R.mipmap.ic_tanta_intimate_lock
        })
        tvContent.text = item.des
    }

}