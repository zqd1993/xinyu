package com.live.module.vip.adapter

import android.graphics.Paint
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.vip.R
import com.live.module.vip.bean.VipPayInfoBean
import com.live.module.vip.bean.VipRechargeBean
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.utils.FontsFamily
import com.mshy.VInterestSpeed.common.utils.UserManager

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
class VipRechargeAdapter :
    BaseQuickAdapter<VipRechargeBean, BaseViewHolder>(R.layout.vip_item_recharge) {
    var select: Int = -1

    //    //推荐选项
//    var optimalIndex = 1
    override fun convert(holder: BaseViewHolder, item: VipRechargeBean) {
        var adapterPosition = holder.adapterPosition
        holder.setText(R.id.tv_month, item.vip_duration)

        val tvPrice = holder.getView<TextView>(R.id.tv_price)
        tvPrice.typeface = FontsFamily.tfDDinExpBold

        val userInfo = UserManager.userInfo
        if (userInfo?.vip != null && userInfo?.vip!! > 0) {
            holder.setText(R.id.tv_price, item.renewPrice.toString())
        } else {
            holder.setText(R.id.tv_price, item.price.toString())
        }
        holder.setText(R.id.tv_price_pre, "原价" + item.oldPrice.toString())
        holder.setText(R.id.tv_discount, item.province)
        val tvMonth = holder.getView<TextView>(R.id.tv_month)
        var shadowLayout = holder.getView<RelativeLayout>(R.id.sl_score)
        var shapeTextView = holder.getView<TextView>(R.id.tv_discount)
        var tvPricePre = holder.getView<TextView>(R.id.tv_price_pre)
        tvPricePre.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        if (select == -1 && item.vip_suggest == 1) {
            select = adapterPosition
        }

        shadowLayout.isSelected = select == adapterPosition
        tvMonth.isSelected = select == adapterPosition

//        if (select == adapterPosition) {
//            holder.setBackgroundColor(
//                R.id.cl_text_root,
//                context.resources.getColor(R.color.color_FCF5ED)
//            )
////            shadowLayout.setShadowColor(context.resources.getColor(R.color._alpha_C78D61_45))
//
//            holder.setTextColor(R.id.tv_month, context.resources.getColor(R.color.color_D18A59))
//            holder.setTextColor(
//                R.id.label_price_unit,
//                context.resources.getColor(R.color.color_D18A59)
//            )
//            holder.setTextColor(R.id.tv_price, context.resources.getColor(R.color.color_D18A59))
//            holder.setTextColor(R.id.tv_price_pre, context.resources.getColor(R.color.color_D18A59))
//            holder.setTextColor(R.id.tv_discount, context.resources.getColor(R.color.color_934800))
//
//            shapeTextView.background =
//                context.resources.getDrawable(R.drawable.shape_vqu_bg_vip_recharge_text_selected)
//            if (item.vip_suggest == 1) {
//                holder.setGone(R.id.tv_label, false)
//            }
//            holder.setGone(R.id.v_recharge_stroke, false)
//        } else {
//            holder.setBackgroundColor(
//                R.id.cl_text_root,
//                context.resources.getColor(R.color.color_FFFFFF)
//            )
////            shadowLayout.setShadowColor(context.resources.getColor(R.color._30A3AABE))
//
//
//            holder.setTextColor(R.id.tv_month, context.resources.getColor(R.color.color_273145))
//            holder.setTextColor(
//                R.id.label_price_unit,
//                context.resources.getColor(R.color.color_273145)
//            )
//            holder.setTextColor(R.id.tv_price, context.resources.getColor(R.color.color_273145))
//            holder.setTextColor(R.id.tv_price_pre, context.resources.getColor(R.color.color_8B96B5))
//            holder.setTextColor(R.id.tv_discount, context.resources.getColor(R.color.color_8B96B5))
//            shapeTextView.background =
//                context.resources.getDrawable(R.drawable.shape_vqu_bg_vip_recharge_text_unselected)
//            holder.setGone(R.id.tv_label, true)
//                .setGone(R.id.v_recharge_stroke, true)
//        }

        shadowLayout.setViewClickListener(1) {

            if (select == adapterPosition) {
                return@setViewClickListener
            }
            select = adapterPosition
            notifyDataSetChanged()
        }
    }

    fun getPayInfo(): VipPayInfoBean {
        val payInfoBean = VipPayInfoBean()
        val vipRechargeBean = data[select]
        val userInfo = UserManager.userInfo

        //目前只开通VIP=1
        payInfoBean.vip_id = "1"

//        LogUtil.e("money000=${payInfoBean.money}")
        payInfoBean.vip_goods_id = vipRechargeBean.id.toString()
        if (userInfo?.vip!! > 0) {
            payInfoBean.type = "start"
            payInfoBean.money = vipRechargeBean.renewPrice.toString()
        } else {
            payInfoBean.type = "none"
            payInfoBean.money = vipRechargeBean.price.toString()
        }
        return payInfoBean
    }
}