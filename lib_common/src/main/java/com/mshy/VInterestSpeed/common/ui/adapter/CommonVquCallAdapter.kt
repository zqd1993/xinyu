package com.mshy.VInterestSpeed.common.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage


class CommonVquCallAdapter(data:MutableList<com.mshy.VInterestSpeed.common.bean.notification.CallNotificationBean>) : BaseQuickAdapter<com.mshy.VInterestSpeed.common.bean.notification.CallNotificationBean,BaseViewHolder>(R.layout.item_notification,data) {

    override fun convert(holder: BaseViewHolder, item: com.mshy.VInterestSpeed.common.bean.notification.CallNotificationBean) {
      holder.getView<TextView>(R.id.vquNoCallName).text=(item.vquNoCallName)
      holder.getView<TextView>(R.id.vquNoCallType).text=(item.vquNoCallType)
      holder.getView<TextView>(R.id.vquNoCallZS).text=(item.vquNoCallZS)
      holder.getView<ImageView>(R.id.image_view).vquLoadRoundImage(R.mipmap.ic_launcher,20)
    }
}