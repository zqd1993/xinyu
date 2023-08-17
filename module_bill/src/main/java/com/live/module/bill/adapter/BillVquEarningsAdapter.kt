package com.live.module.bill.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.bill.R
import com.live.module.bill.bean.BillTantaWithdrawOptions
import com.mshy.VInterestSpeed.common.utils.FontsFamily

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
class BillVquEarningsAdapter :
    BaseQuickAdapter<BillTantaWithdrawOptions, BaseViewHolder>(R.layout.bill_vqu_item_withdrawal_options) {
    private var vquIncomeCoinMoney = 0.0



    override fun convert(holder: BaseViewHolder, item: BillTantaWithdrawOptions) {


        val tvPrice = holder.getView<TextView>(R.id.tv_tanta_item_bill_recharge_options_num)
        tvPrice.typeface = FontsFamily.tfDDinExpBold
        tvPrice.text = item.money

        val clRoot = holder.getView<ConstraintLayout>(R.id.cl_bill_vqu_item_withdrawal_options_root)
        clRoot.isSelected = item.selected

        val tvSymbol = holder.getView<ImageView>(R.id.tv_vqu_item_bill_withdrawal_options_symbol)
        tvSymbol.isSelected = vquIncomeCoinMoney >= item.money.toInt()

        tvPrice.isSelected = vquIncomeCoinMoney >= item.money.toInt()

        holder.setGone(
            R.id.tv_bill_vqu_item_withdrawal_options_insufficient,
            vquIncomeCoinMoney >= item.money.toInt()
        )

    }

    fun setVquIncomeCoinMoney(incomeCoinMoney: Double) {
        vquIncomeCoinMoney = incomeCoinMoney
    }
}