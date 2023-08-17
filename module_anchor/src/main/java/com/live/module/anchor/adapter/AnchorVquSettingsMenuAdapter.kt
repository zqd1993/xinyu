package com.live.module.anchor.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.anchor.R
import com.mshy.VInterestSpeed.common.bean.CommonVquMenuBean

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
class AnchorVquSettingsMenuAdapter :
    BaseQuickAdapter<CommonVquMenuBean, BaseViewHolder>(R.layout.anchor_vqu_item_settings_menu) {
    override fun convert(holder: BaseViewHolder, item: CommonVquMenuBean) {
        holder.setImageResource(R.id.iv_anchor_vqu_item_setting_menu_icon, item.icon)
        holder.setText(R.id.tv_anchor_vqu_item_settings_menu_name, item.title)
    }
}