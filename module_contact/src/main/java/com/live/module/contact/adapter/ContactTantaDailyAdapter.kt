package com.live.module.contact.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.contact.R
import com.live.module.contact.bean.ContactVquOverviewBean

/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
class ContactTantaDailyAdapter :
    BaseQuickAdapter<ContactVquOverviewBean, BaseViewHolder>(R.layout.contact_vqu_item_daily_value) {


    override fun convert(holder: BaseViewHolder, item: ContactVquOverviewBean) {

        holder.setText(R.id.tv_date, item.nickname)
            .setText(R.id.tv_value, item.nickname)


    }
}