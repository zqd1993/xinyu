package com.live.module.mine.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.mine.R
import com.live.module.mine.bean.MineVquCouponBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
class MineVquTryCardAdapter :
    BaseQuickAdapter<MineVquCouponBean, BaseViewHolder>(R.layout.mine_vqu_item_try_card) {
    override fun convert(holder: BaseViewHolder, item: MineVquCouponBean) {
        holder.getView<ImageView>(R.id.iv_mine_vqu_item_try_card_img)
            .vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.img)
        holder.getView<ImageView>(R.id.iv_mine_vqu_item_try_card_bg)
            .vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.bgImg)

        holder.setText(R.id.tv_mine_vqu_item_try_card_title, item.name)
        holder.setText(R.id.tv_mine_vqu_item_try_card_desc, item.discount)
        holder.setText(R.id.tv_mine_vqu_item_try_card_time, item.expire)
    }
}