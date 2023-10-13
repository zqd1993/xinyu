package com.live.module.bill.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.bill.R
import com.live.module.bill.bean.BillVquAccountInfo
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.utils.ResUtils

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
class BillVquAccountAdapter :
    BaseQuickAdapter<BillVquAccountInfo, BaseViewHolder>(R.layout.bill_vqu_item_account) {

    init {
        addChildClickViewIds(R.id.stv_bill_vqu_item_account_edit)
    }

    override fun convert(holder: BaseViewHolder, item: BillVquAccountInfo) {
        val tvName = holder.getView<TextView>(R.id.tv_bill_vqu_item_account_name)
        val tvAccount = holder.getView<TextView>(R.id.tv_bill_vqu_item_account_id)
        val tvPhone = holder.getView<TextView>(R.id.tv_bill_vqu_item_account_phone)
        val ivIcon = holder.getView<ImageView>(R.id.iv_bill_vqu_item_account_type)
        val tvBankName = holder.getView<TextView>(R.id.tv_bill_vqu_item_bank_name)
        tvAccount.text = "提现账号：${item.cardAccount}"
        tvPhone.text = "手机号：${item.mobile}"
        tvBankName.text = "账户类型: ${item.bank}"
        ivIcon.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.icon)
        if (item.isMonthLimit == 1) {
            ivIcon.alpha = 0.3f
            tvName.text = "姓名：${item.cardName}（本月提现超额）"
            tvName.setTextColor(ResUtils.getColor(R.color.color_A3AABE))
            tvAccount.setTextColor(ResUtils.getColor(R.color.color_A3AABE))
            tvPhone.setTextColor(ResUtils.getColor(R.color.color_A3AABE))
        } else {
            ivIcon.alpha = 1f
            tvName.text = "姓名：${item.cardName}"
            tvName.setTextColor(ResUtils.getColor(R.color.color_273145))
            tvAccount.setTextColor(ResUtils.getColor(R.color.color_273145))
            tvPhone.setTextColor(ResUtils.getColor(R.color.color_273145))
        }

        holder.setGone(R.id.stv_bill_vqu_item_account_edit, item.status != 0)
    }
}