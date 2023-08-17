package com.live.module.bill.adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.bill.R
import com.mshy.VInterestSpeed.common.bean.CommonTantaRechargeOptions
import com.mshy.VInterestSpeed.common.utils.FontsFamily

/**
 * author: Lau
 * date: 2022/4/15
 * description:
 */
class BillTantaRechargeAdapter :
    BaseQuickAdapter<CommonTantaRechargeOptions, BaseViewHolder>(R.layout.bill_tanta_item_recharge_options) {
    override fun convert(holder: BaseViewHolder, item: CommonTantaRechargeOptions) {
        val clRoot = holder.getView<ConstraintLayout>(R.id.cl_bill_tanta_item_recharge_options_root)
        val tvNum = holder.getView<TextView>(R.id.tv_tanta_item_bill_recharge_options_num)
//        val tvDiamond = holder.getView<TextView>(R.id.tv_bill_vqu_item_recharge_potions_diamond)
        val tvSale = holder.getView<TextView>(R.id.tv_tanta_item_bill_recharge_options_sale)
        val tvPrice = holder.getView<TextView>(R.id.tv_tanta_item_bill_recharge_options_price)

        tvNum.typeface = FontsFamily.tfDDinExpBold
        tvPrice.typeface = FontsFamily.tfDDinExpBold

        clRoot.isSelected = item.isSelected
        tvNum.isSelected = item.isSelected
//        tvDiamond.isSelected = item.isSelected
        tvSale.isSelected = item.isSelected
        tvPrice.isSelected = item.isSelected

        tvNum.text = item.amount.toString()
        tvPrice.text = "¥${item.price}"
        val btnText = item.btnText
        if (!btnText.isNullOrEmpty()) {
            val saleText = StringBuilder()
            btnText.forEachIndexed { index, s ->
                if (index < btnText.size - 1) {
                    saleText.append(s).append("\n")
                } else {
                    saleText.append(s)
                }
            }
            if (saleText.isBlank()) {
                tvSale.visibility = View.GONE
            } else {
                tvSale.visibility = View.VISIBLE
            }
            tvSale.text = saleText

        } else {
            tvSale.visibility = View.GONE
        }
    }
}