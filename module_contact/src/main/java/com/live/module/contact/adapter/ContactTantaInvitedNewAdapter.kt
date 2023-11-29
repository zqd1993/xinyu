package com.live.module.contact.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.contact.R
import com.live.module.contact.bean.ContactVquInvitedData
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage


/**
 * author: Lau
 * date: 2022/6/22
 * description:
 */
class ContactTantaInvitedNewAdapter :
    BaseQuickAdapter<ContactVquInvitedData, BaseViewHolder>(R.layout.contact_vqu_item_invited_record_list) {
    var hideTotalValue = true

    init {
        addChildClickViewIds(
            R.id.llc_value
        )
    }

    override fun convert(holder: BaseViewHolder, item: ContactVquInvitedData) {
        holder.getView<ImageView>(R.id.iv_avatar)
            .vquLoadRoundImage(
                NetBaseUrlConstant.IMAGE_URL + item.avatar,
                context.dp2px(10f),
                R.mipmap.ic_common_head_def
            )

        holder.setGone(R.id.tv_value, hideTotalValue)
        holder.setGone(R.id.label_value, hideTotalValue)

        holder.setText(R.id.tv_name, item.nickname)
        holder.setText(R.id.tv_user_id, "甜缘号：" + item.usercode)
        holder.setText(R.id.tv_sign_time, "注册时间: " + item.addTime)
    }
}