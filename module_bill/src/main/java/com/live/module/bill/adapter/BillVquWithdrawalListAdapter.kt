package com.live.module.bill.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.bill.R
import com.live.module.bill.bean.WithdrawalDetailItem

/**
 * author: Lau
 * date: 2022/6/21
 * description:
 */
class BillVquWithdrawalListAdapter :
    BaseQuickAdapter<WithdrawalDetailItem, BaseViewHolder>(R.layout.bill_vqu_item_withdrawal_list) {
    override fun convert(holder: BaseViewHolder, item: WithdrawalDetailItem) {

        holder.setText(
            R.id.tv_bill_vqu_item_withdrawal_list_get_money,
            "提现金额：${item.cashMoney}元"
        )

        holder.setText(
            R.id.tv_bill_vqu_item_withdrawal_list_real_money,
            "实际到账：${item.realCashMoney}元"
        )


        holder.setText(
            R.id.tv_bill_vqu_item_withdrawal_list_account,
            "提现账号：${item.bank} (${item.cardAccount})"
        )

        holder.setText(
            R.id.tv_bill_vqu_item_withdrawal_create_time,
            "提现时间：${item.createTimeText}"
        )

        holder.setText(
            R.id.tv_bill_vqu_item_withdrawal_order_no,
            "提现单号：${item.orderNo}"
        )

        holder.setText(
            R.id.tv_bill_vqu_item_withdrawal_status,
            item.statusText
        )

        holder.setTextColor(
            R.id.tv_bill_vqu_item_withdrawal_status,
            Color.parseColor("#${item.statusColor}")
        )

    }
}