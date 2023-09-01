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
class ContactTantaInvitedAdapter :
    BaseQuickAdapter<ContactVquInvitedData, BaseViewHolder>(R.layout.contact_vqu_item_invited_record_list) {
    override fun convert(holder: BaseViewHolder, item: ContactVquInvitedData) {
        holder.getView<ImageView>(R.id.iv_contact_vqu_item_invited_record_list_avatar)
            .vquLoadRoundImage(
                NetBaseUrlConstant.IMAGE_URL + item.avatar,
                context.dp2px(10f),
                R.mipmap.ic_common_head_def
            )

        holder.setText(R.id.tv_contact_vqu_item_invited_record_list_name, item.nickname)
        holder.setText(R.id.tv_contact_vqu_item_invited_record_list_id, "心语号：" + item.usercode)
        holder.setText(R.id.tv_contact_vqu_item_invited_record_list_time, item.addTime)
    }
}