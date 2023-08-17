package com.live.module.auth.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.auth.R
import com.mshy.VInterestSpeed.common.bean.CommonVquMenuBean
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView
import com.mshy.VInterestSpeed.common.utils.ResUtils

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
class AuthVquCenterMenuAdapter :
    BaseQuickAdapter<CommonVquMenuBean, BaseViewHolder>(R.layout.auth_vqu_item_center_menu) {
    override fun convert(holder: BaseViewHolder, item: CommonVquMenuBean) {
        holder.setText(R.id.tv_auth_vqu_item_center_menu_title, item.title)

//        holder.setText(R.id.iv_auth_tanta_item_center_menu_icon,item.icon)


        holder.setImageResource(R.id.iv_auth_tanta_item_center_menu_icon, item.icon)

        holder.setText(R.id.tv_auth_vqu_item_center_menu_desc, item.desc)

        val tvStatus = holder.getView<ShapeTextView>(R.id.tv_auth_vqu_item_center_menu_status)
        val ivIcon = holder.getView<ImageView>(R.id.iv_auth_tanta_item_center_menu_icon)

//        tvStatus.isSelected = item.isSelected

        if (item.isSelected) {
            tvStatus.text = "已认证"
            ivIcon.isSelected = true
            tvStatus.solidColor = ResUtils.getColor(R.color.color_cccccc)
        } else {
            tvStatus.text = "去认证"
            ivIcon.isSelected = false
            tvStatus.solidColor = ResUtils.getColor(R.color.color_FF7AC2)
        }

    }
}