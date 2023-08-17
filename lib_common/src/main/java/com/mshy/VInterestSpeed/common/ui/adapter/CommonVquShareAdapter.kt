package com.mshy.VInterestSpeed.common.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.CommonVquShareBean


/**
 * author: Tany
 * date: 2022/4/28
 * description:
 */
class CommonVquShareAdapter(data:MutableList<CommonVquShareBean>) : BaseQuickAdapter<CommonVquShareBean,BaseViewHolder>(R.layout.common_vqu_item_share,data) {
    override fun convert(holder: BaseViewHolder, item: CommonVquShareBean) {
      holder.getView<ImageView>(R.id.iv_img).setImageResource(item.imgId)
      holder.getView<TextView>(R.id.tv_title).text=context.resources.getString(item.titleId)

    }
}