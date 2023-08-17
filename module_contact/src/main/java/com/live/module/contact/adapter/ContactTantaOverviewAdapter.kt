package com.live.module.contact.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.contact.R
import com.live.module.contact.bean.ContactVquOverviewBean
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.utils.FontsFamily
import com.mshy.VInterestSpeed.common.utils.UserManager

/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
class ContactTantaOverviewAdapter :
    BaseQuickAdapter<ContactVquOverviewBean, BaseViewHolder>(R.layout.contact_vqu_item_my_contact) {

    init {
        addChildClickViewIds(R.id.tv_contact_vqu_item_my_contact_id)
        addChildClickViewIds(R.id.tv_contact_vqu_item_my_contact_more)
    }

    override fun convert(holder: BaseViewHolder, item: ContactVquOverviewBean) {
        holder.getView<TextView>(R.id.tv_contact_vqu_item_my_contact_money).typeface =
            FontsFamily.tfDDinExpBold


        holder.getView<ImageView>(R.id.iv_contact_vqu_item_my_contact_avatar)
            .vquLoadCircleImage(
                NetBaseUrlConstant.IMAGE_URL + item.avatar,
                R.mipmap.ic_common_head_def
            )
        holder.setText(R.id.tv_contact_vqu_item_my_contact_name, item.nickname)
        holder.setText(
            R.id.tv_contact_vqu_item_my_contact_id,
            context.getString(R.string.contact_vqu_id, item.usercode)
        )
        holder.setText(
            R.id.tv_contact_vqu_item_my_contact_date,
            context.getString(R.string.contact_vqu_register_date, item.createtime)
        )

        holder.setText(R.id.tv_contact_vqu_item_my_contact_money, item.totalIncome)

        val clParent = holder.getView<CardView>(R.id.cl_contact_vqu_item_my_contact_parent)
        com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper.addGender(
            context, clParent, item.gender, item.age
        )

        holder.setGone(R.id.iv_contact_vqu_item_my_contact_auth, item.isAnchor != 1)

        val isStartScout = UserManager.userInfo?.isStarScout == 1
        val scoutModel = UserManager.userInfo?.scoutModel == 1

        val tvMore = holder.getView<TextView>(R.id.tv_contact_vqu_item_my_contact_more)
        val llMoreParent1 =
            holder.getView<LinearLayout>(R.id.ll_contact_vqu_my_contact_more_parent_1)
        val llMoreParent2 =
            holder.getView<LinearLayout>(R.id.ll_contact_vqu_my_contact_more_parent_2)
        val llMoreParent3 =
            holder.getView<LinearLayout>(R.id.ll_contact_vqu_my_contact_more_parent_3)

        if (scoutModel) {
            tvMore.isSelected = item.isSelected
            llMoreParent1.visible()
            tvMore.visible()

            if (item.isSelected) {
                tvMore.text = "收起"
                llMoreParent2.visible()
                llMoreParent3.visible()
            } else {
                tvMore.text = "展开"
                llMoreParent2.gone()
                llMoreParent3.gone()
            }

//        holder.getView<TextView>(R.id.tv_contact_vqu_item_my_contact_letter_num)
            holder.setText(R.id.tv_contact_vqu_item_my_contact_letter_num, item.toUserLetterIncome)
            holder.setText(R.id.tv_contact_vqu_item_my_contact_video_num, item.toUserVideoIncome)
            holder.setText(R.id.tv_contact_vqu_item_my_contact_voice_num, item.toUserVoiceIncome)
            holder.setText(R.id.tv_contact_vqu_item_my_contact_gift_num, item.toUserGiftIncome)

            val totalM = item.onlineTime / 60   //计算出总分钟
            val h = totalM / 60 //计算出小时
            val m = (totalM % 60)   //计算出剩余分钟


            holder.setText(R.id.tv_contact_vqu_item_my_contact_online_time, "${h}h ${m}m")
            holder.setText(R.id.tv_contact_vqu_item_my_contact_heart_num, item.heartNum)
            holder.setText(R.id.tv_contact_vqu_item_my_contact_chat_num, item.chatNum)
            holder.setText(
                R.id.tv_contact_vqu_item_my_contact_recharge_num,
                item.toUserRechargeCoin
            )
            holder.setText(R.id.tv_contact_vqu_item_my_contact_last_time, item.lastLiveTime)
        } else {
            tvMore.gone()
            llMoreParent1.gone()
            llMoreParent2.gone()
            llMoreParent3.gone()
        }


    }
}