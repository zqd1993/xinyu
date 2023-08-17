package com.live.module.home.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.home.R
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper
import com.mshy.VInterestSpeed.common.ui.view.CommonVquAvatarFrameView
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager

/**
 * author: Lau
 * date: 2022/7/4
 * description:
 */
class HomeSearchAdapter() :
    BaseQuickAdapter<VquRelationBean, BaseViewHolder>(R.layout.home_tanta_item_search_list) {

    init {
        addChildClickViewIds(R.id.stv_relation_vqu_item_relation_list_like)
    }

    private val chatDrawable = ResUtils.getDrawable(R.mipmap.resources_tanta_main_home_chat)
        .apply {
            setBounds(
                0, 0, minimumWidth,
                minimumHeight
            )
        }
    private val likeDrawable = ResUtils.getDrawable(R.mipmap.resources_tanta_home_like).apply {
        setBounds(
            0, 0, minimumWidth,
            minimumHeight
        )
    }

    override fun convert(holder: BaseViewHolder, item: VquRelationBean) {
        val vquAvatarView =
            holder.getView<CommonVquAvatarFrameView>(R.id.siv_relation_item_relation_list_avatar)

        vquAvatarView.loadAvatar(item.avatar)
        vquAvatarView.loadAvatarFrame(item.avatarFrame)
        vquAvatarView.showAnchorView(item.isLive == 1)

        holder.setText(R.id.tv_relation_item_relation_list_name, item.nickname)

        val clRoot = holder.getView<ViewGroup>(R.id.cl_relation_item_root)

        CommonVquAddRankAndGradeViewHelper.addGender(
            context,
            clRoot,
            item.gender,
            item.age.toString()
        )


        val desc = StringBuilder()

        //年龄
        if (!item.age.isNullOrEmpty()) {
            if (desc.isNotEmpty()) {
                desc.append(" | ")
            }
            desc.append(item.age + "岁")
        }

        //身高
        if (!item.height.isNullOrEmpty()) {
            if (desc.isNotEmpty()) {
                desc.append(" | ")
            }
            desc.append(item.height)
        }

        //职业
        if (!item.occupation.isNullOrEmpty()) {
            if (desc.isNotEmpty()) {
                desc.append(" | ")
            }
            desc.append(item.occupation)
        }

        holder.setText(R.id.tv_relation_item_relation_list_desc, desc.toString())

        val stvLike = holder.getView<TextView>(R.id.stv_relation_vqu_item_relation_list_like)

        stvLike.visibility = View.VISIBLE

        stvLike.text = item.isBeckonText

        if (item.isBeckon == true) {
            stvLike.isSelected = true
            stvLike.setText(R.string.dynamic_chat)
//            stvLike.setStartColor(
//                ResUtils.getColor(R.color.color_14BFB6FF),
//                ResUtils.getColor(R.color.color_267C69FE)
//            )
//            stvLike.setTextColor(ResUtils.getColor(R.color.color_7C69FE))
//
//            stvLike.setCompoundDrawables(null, chatDrawable, null, null)

        } else {

            if (UserManager.userInfo?.gender == 1) {
                stvLike.setText(R.string.common_vqu_beckoning)
            } else {
                stvLike.setText(R.string.common_vqu_accost)
            }

            stvLike.isSelected = false

//            stvLike.setStartColor(
//                ResUtils.getColor(R.color.color_14FBB3AC),
//                ResUtils.getColor(R.color.color_14FF3826)
//            )
//            stvLike.setTextColor(ResUtils.getColor(R.color.color_FF3826))
//
//            stvLike.setCompoundDrawables(null, likeDrawable, null, null)
        }
    }
}