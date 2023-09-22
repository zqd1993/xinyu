package com.live.module.vip.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.vip.R
import com.live.module.vip.bean.VipPrivilegeBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.utils.UserManager

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
class VipPrivilegeAdapter :
    BaseQuickAdapter<VipPrivilegeBean, BaseViewHolder>(R.layout.vip_item_privilege) {
    override fun convert(holder: BaseViewHolder, item: VipPrivilegeBean) {
        holder.getView<ImageView>(R.id.iv_label)
            .vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.img)

        holder.setText(R.id.tv_title, item.name)
        holder.setText(R.id.tv_content, item.des)
        val titleTv = holder.getView<TextView>(R.id.tv_title)
        if(UserManager.userInfo?.gender == 1){
            titleTv.setTextColor(context.resources.getColor(R.color.white))
        } else {
            titleTv.setTextColor(context.resources.getColor(R.color.color_BFA786))
        }
    }
}