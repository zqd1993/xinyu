package com.live.module.bill.adapter

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.bill.R
import com.live.module.bill.bean.BillVquDetailSectionBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage

/**
 * author: Lau
 * date: 2022/4/20
 * description:
 */
class BillVquDetailAdapter(private val type: Int) :
    BaseSectionQuickAdapter<BillVquDetailSectionBean, BaseViewHolder>(
        R.layout.bill_vqu_item_detail_header,
        R.layout.bill_vqu_item_bill_details
    ) {

    private var typeface: Typeface? = null

    override fun convert(holder: BaseViewHolder, item: BillVquDetailSectionBean) {
        val ivAvatar = holder.getView<ImageView>(R.id.siv_item_vqu_bill_details_avatar)

        ivAvatar.vquLoadCircleImage(
            NetBaseUrlConstant.IMAGE_URL + item.icon,
        )
        holder.setText(R.id.tv_bill_vqu_item_details_name, item.fromUsername)
        holder.setText(R.id.tv_bill_vqu_item_details_date, item.createTime)
        holder.setText(R.id.tv_vqu_item_bill_details_reason, item.systemStr)

        if (type == 1) {
            holder.setText(R.id.tv_bill_vqu_item_details_num, item.changeValue)
            holder.setText(R.id.tv_vqu_item_bill_details_diamond_txt, R.string.vqu_diamond)
        } else {
            holder.setText(R.id.tv_bill_vqu_item_details_num, item.changeValueNew)
            holder.setText(R.id.tv_vqu_item_bill_details_diamond_txt, R.string.common_vqu_yuan)
        }



        if (typeface != null) {
            holder.getView<TextView>(R.id.tv_bill_vqu_item_details_num).typeface = typeface
        }
    }

    override fun convertHeader(helper: BaseViewHolder, item: BillVquDetailSectionBean) {
        helper.setText(R.id.tv_bill_vqu_item_detail_header_date, item.date)
    }

    fun setTypeface(tf: Typeface) {
        typeface = tf
    }
}
