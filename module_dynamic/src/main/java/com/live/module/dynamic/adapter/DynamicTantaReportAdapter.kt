package com.live.module.dynamic.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.dynamic.R
import com.mshy.VInterestSpeed.common.bean.DynamicVquReportBean

/**
 * author: Tany
 * date: 2022/4/21
 * description:
 */
class DynamicTantaReportAdapter() :
    BaseQuickAdapter<DynamicVquReportBean, BaseViewHolder>(R.layout.dynamic_tanta_item_report) {
    var selPos = -1
    override fun convert(holder: BaseViewHolder, item: DynamicVquReportBean) {
        holder.getView<TextView>(R.id.tv_content).text = item.name
        holder.getView<ImageView>(R.id.iv_sel).isSelected = selPos == holder.layoutPosition

    }

    fun setSel(pos: Int) {
        selPos = pos
        notifyDataSetChanged()
    }


    fun getSelId(): Int {
        if (selPos == -1) {
            return 0
        } else {
            return data[selPos].id
        }
    }
}