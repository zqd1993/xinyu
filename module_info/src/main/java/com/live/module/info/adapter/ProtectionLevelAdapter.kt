package com.live.module.info.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.info.R
import com.mshy.VInterestSpeed.common.bean.ProtectionOptionBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage

class ProtectionLevelAdapter(data: MutableList<ProtectionOptionBean>) :
    BaseQuickAdapter<ProtectionOptionBean, BaseViewHolder>(R.layout.adatper_protection_level_item, data) {

    override fun convert(holder: BaseViewHolder, item: ProtectionOptionBean) {
        val protectionLevelIm = holder.getView<ImageView>(R.id.protection_level_im)
        if (item.icon.isNotEmpty()) {
            protectionLevelIm.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.icon)
        }
        holder.setText(R.id.protection_level_day_iv, item.expireDay)
    }
}