package com.live.module.mine.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import android.widget.TextView
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
class MineVquCouponAdapter :
    BaseQuickAdapter<MineVquCouponBean, BaseViewHolder>(R.layout.mine_vqu_item_coupon) {
    override fun convert(holder: BaseViewHolder, item: MineVquCouponBean) {
        holder.getView<ImageView>(R.id.iv_mine_vqu_item_coupon_bg_left)
            .vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.img)
        holder.getView<ImageView>(R.id.iv_mine_vqu_item_coupon_bg_right)
            .vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.bgImg)

        holder.setText(R.id.tv_mine_vqu_item_coupon_title, item.name)
        holder.setText(R.id.tv_mine_vqu_item_coupon_desc, item.discount)
        holder.setText(R.id.tv_mine_vqu_item_coupon_time, item.expire)

        val dis = SpannableString(item.dis)
        val disLast = AbsoluteSizeSpan(20, true)
        dis.setSpan(disLast, 1, dis.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.getView<TextView>(R.id.tv_mine_vqu_item_coupon_dis).text = dis
    }
}