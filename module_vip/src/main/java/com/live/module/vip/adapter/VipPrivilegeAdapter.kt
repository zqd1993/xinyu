package com.live.module.vip.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.vip.R
import com.live.module.vip.bean.VipPrivilegeBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage

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
    }
}