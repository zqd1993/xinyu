package com.live.module.mine.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.mine.R
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.bean.Today
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.utils.FontsFamily

/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
class MineVquDialogSignDayAdapter :
    BaseQuickAdapter<Today, BaseViewHolder>(R.layout.mine_item_dialog_sign_day) {

    var todayCount = 0


    override fun convert(holder: BaseViewHolder, item: Today) {

//        val ivTick = holder.getView<ImageView>(R.id.iv_mine_item_sign_day_tick)
        val tvCount = holder.getView<TextView>(R.id.tv_mine_item_sign_day_count)
        val ivIcon = holder.getView<ImageView>(R.id.iv_mine_item_sign_day_icon)
        val stvAward = holder.getView<TextView>(R.id.tv_mine_item_sign_day_award)
        val sclParent = holder.getView<ConstraintLayout>(R.id.cl_mine_item_sign_day_info_parent)
        val vStroke = holder.getView<View>(R.id.v_mine_item_dialog_sign_day_stroke)

        stvAward.typeface = FontsFamily.tfDDinExpBold

        if (item.nowDay) {
            tvCount.text = "今天"
        } else {
            tvCount.text = "第" + (holder.layoutPosition + 1) + "天"
        }

        if (!item.list.isNullOrEmpty()) {
            val giftData = item.list[0]
            ivIcon.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + giftData.img)
            stvAward.text = giftData.title
        }

        if (item.nowDay) {          //当天未签到

//            ivTick.visibility = View.INVISIBLE

            sclParent.isSelected = true

            tvCount.isSelected = false

            stvAward.isSelected = false

            vStroke.visible()

        } else if (holder.layoutPosition >= todayCount) {    //大于明天
//            ivTick.visibility = View.INVISIBLE

            sclParent.isSelected = false

            tvCount.isSelected = true

            stvAward.isSelected = true

            vStroke.gone()

        } else {          //已签到
//            ivTick.visibility = View.VISIBLE

            sclParent.isSelected = false

            tvCount.isSelected = true

            stvAward.isSelected = true

            vStroke.gone()
        }
    }
}