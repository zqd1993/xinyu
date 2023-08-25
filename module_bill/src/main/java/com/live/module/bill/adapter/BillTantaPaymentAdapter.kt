package com.live.module.bill.adapter

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.live.module.bill.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mshy.VInterestSpeed.common.bean.pay.RechargeRoute

class BillTantaPaymentAdapter :
    BaseQuickAdapter<RechargeRoute, BaseViewHolder>(R.layout.bill_tanta_adapter_payment) {

    override fun convert(holder: BaseViewHolder, item: RechargeRoute) {
        val clRoot = holder.getView<ConstraintLayout>(R.id.cl_root)
        val tvPayment = holder.getView<TextView>(R.id.tv_payment)
        tvPayment.text = item.alias
        clRoot.isSelected = item.checked
    }

}