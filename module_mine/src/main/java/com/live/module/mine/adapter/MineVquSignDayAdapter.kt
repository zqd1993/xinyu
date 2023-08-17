package com.live.module.mine.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.mine.R
import com.mshy.VInterestSpeed.common.bean.Today
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.utils.FontsFamily

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
class MineVquSignDayAdapter :
    BaseQuickAdapter<Today, BaseViewHolder>(R.layout.mine_item_sign_day) {

    var todayCount = 0

    override fun convert(holder: BaseViewHolder, item: Today) {
        val stvTips = holder.getView<TextView>(R.id.stv_mine_item_sign_day_tips)
//        val ivArrow = holder.getView<ImageView>(R.id.iv_mine_item_sign_day_arrow)
//        val ivTick = holder.getView<ImageView>(R.id.iv_mine_item_sign_day_tick)
        val tvCount = holder.getView<TextView>(R.id.tv_mine_item_sign_day_count)
        val ivIcon = holder.getView<ImageView>(R.id.iv_mine_item_sign_day_icon)
        val stvAward = holder.getView<TextView>(R.id.tv_mine_item_sign_day_award)
        val clParent = holder.getView<ConstraintLayout>(R.id.cl_mine_item_sign_day_info_parent)

        stvAward.typeface = FontsFamily.tfDDinExpBold

        if (item.nowDay) {
            tvCount.text = "今天"
        } else {
            tvCount.text = "第" + (holder.layoutPosition + 1) + "天"
        }

        if (!item.list.isNullOrEmpty()) {
            val giftData = item.list[0]
            ivIcon.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + giftData.img)
            stvAward.text = "+${giftData.money}"
        }

        if (item.nowDay) {          //当前未签到

            if (!item.status) {
                stvTips.visibility = View.VISIBLE
                stvTips.text = "签到领取"
            }
//            ivArrow.visibility = View.VISIBLE

//            ivTick.visibility = View.INVISIBLE


            clParent.isSelected = true
            tvCount.isSelected = false
            stvAward.isSelected = false

        } else if (holder.layoutPosition == todayCount) {   //明天
            stvTips.visibility = View.VISIBLE
//            ivArrow.visibility = View.VISIBLE
            stvTips.text = "明日可领"
//            ivTick.visibility = View.INVISIBLE
            clParent.isSelected = false
            tvCount.isSelected = true
            stvAward.isSelected = true
        } else if (holder.layoutPosition > todayCount) {    //大于明天
//            ivArrow.visibility = View.INVISIBLE
            stvTips.visibility = View.INVISIBLE
//            ivTick.visibility = View.INVISIBLE

            clParent.isSelected = false
            tvCount.isSelected = true
            stvAward.isSelected = true

        } else {          //已签到
//            ivArrow.visibility = View.INVISIBLE
            stvTips.visibility = View.INVISIBLE
//            ivTick.visibility = View.VISIBLE

            clParent.isSelected = false
            tvCount.isSelected = true
            stvAward.isSelected = true
        }
    }
}