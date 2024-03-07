package com.live.module.info.adapter

import android.graphics.Paint
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.info.R
import com.mshy.VInterestSpeed.common.bean.ProtectionOptionBean

class ProtectionOptionAdapter :
    BaseQuickAdapter<ProtectionOptionBean, BaseViewHolder>(R.layout.adapter_protection_pay_item) {

    override fun convert(holder: BaseViewHolder, item: ProtectionOptionBean) {
        holder.setText(R.id.protection_option_name, item.name)
        holder.setText(R.id.protection_option_amount, item.amount)
        holder.setText(R.id.protection_option_old_amount, "原价：${item.originalAmount}")
        val protectionOptionOldAmountTv =
            holder.getView<TextView>(R.id.protection_option_old_amount)
        protectionOptionOldAmountTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        val parent = holder.getView<LinearLayout>(R.id.parent_ll)
        parent.isSelected = item.isSelected
    }
}