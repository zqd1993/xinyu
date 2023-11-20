package com.live.module.relation.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.relation.R
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.ext.setVip
import com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper
import com.mshy.VInterestSpeed.common.ui.view.CommonVquAvatarFrameView
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView
import com.mshy.VInterestSpeed.common.ui.view.WaveView
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager


/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
class RelationVquAdapter :
    BaseMultiItemQuickAdapter<VquRelationBean, BaseViewHolder>(mutableListOf()) {

    private var deal = 0

    fun getDeal(): Int {
        return deal
    }

    fun setDeal(deal: Int) {
        this.deal = deal
    }

    var type = 0

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

    init {
        addChildClickViewIds(R.id.stv_relation_vqu_item_relation_list_focus)
        addChildClickViewIds(R.id.stv_relation_vqu_item_relation_list_like)
        //Item类型，访客和其他类型
        addItemType(RouteKey.RelationType.TRACK, R.layout.relation_vqu_item_relation_list)
        addItemType(RouteKey.RelationType.VISTOR, R.layout.relation_vqu_item_relation_visitor)
    }


    override fun convert(holder: BaseViewHolder, item: VquRelationBean) {
        val vquAvatarView =
            holder.getView<CommonVquAvatarFrameView>(R.id.siv_relation_avatar)

        vquAvatarView.loadAvatar(item.avatar)
        vquAvatarView.loadAvatarFrame(item.avatarFrame)
        vquAvatarView.showAnchorView(item.isLive == 1)

        val ivLive = holder.getView<ImageView>(R.id.iv_relation_vqu_item_relation_list_live)
        val wvWave = holder.getView<WaveView>(R.id.wv_relation_vqu_item_relation_list_wave)

        if (item.liveStatus == 1) {
            ivLive.visibility = View.VISIBLE
            wvWave.visibility = View.VISIBLE
            wvWave.setDuration(5000)
            wvWave.setMaxRadius(context.dp2px(30f).toFloat())
            wvWave.setStyle(Paint.Style.FILL)
            wvWave.setColor(Color.parseColor("#ff0000"))
            wvWave.setInterpolator(LinearOutSlowInInterpolator())
            if (deal == 1) {
                wvWave.stop()
            } else {
                wvWave.start()
            }
        } else {
            wvWave.visibility = View.GONE
            ivLive.visibility = View.GONE
        }

        holder.setText(R.id.tv_nick_name, item.nickname)



        holder.getView<TextView>(R.id.tv_nick_name).setVip(item.vip > 0)
        //类型
        when (holder.itemViewType) {
            RouteKey.RelationType.TRACK -> {

                val desc = StringBuilder()

                //城市
                //        if (!item.city.isNullOrEmpty()) {
                //            desc.append(item.city)
                //        }

                if (item.gender == 1) {
                    desc.append("女")
                } else {
                    desc.append("男")
                }


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
                //
                //        //体重
                //        if (!item.weight.isNullOrEmpty()) {
                //            if (desc.isNotEmpty()) {
                //                desc.append(" | ")
                //            }
                //            desc.append(item.weight + "G")
                //        }

                holder.setText(R.id.tv_relation_desc, desc.toString())
            }

            RouteKey.RelationType.VISTOR -> {

                //随机文案
                holder.setText(R.id.tv_relation_desc, item.visitorText)
                //访问时间
                holder.setText(R.id.tv_relation_time, item.addTime + "看过你")

                val clRoot = holder.getView<ViewGroup>(R.id.cl_relation_item_root)

                CommonVquAddRankAndGradeViewHelper.addGender(
                    context,
                    clRoot,
                    item.gender,
                    item.age
                )
            }

        }

        if (type != RouteKey.RelationType.TRACK) {
            holder.setGone(R.id.v_relation_item_relation_list_dot, item.isWatch == 1)
        } else {
            holder.setGone(R.id.v_relation_item_relation_list_dot, true)
        }


        val stvLike = holder.getView<TextView>(R.id.stv_relation_vqu_item_relation_list_like)
        val stvFocus = holder.getView<ShapeTextView>(R.id.stv_relation_vqu_item_relation_list_focus)

        if (type == RouteKey.RelationType.TRACK || type == RouteKey.RelationType.VISTOR) {
            stvLike.visibility = View.VISIBLE
            stvFocus.visibility = View.GONE

            stvLike.text = item.isBeckonText

            if (item.isBeckon == true) {
                stvLike.setText(R.string.dynamic_chat)
//                stvLike.setStartColor(
//                    ResUtils.getColor(R.color.color_14BFB6FF),
//                    ResUtils.getColor(R.color.color_267C69FE)
//                )
//                stvLike.setTextColor(ResUtils.getColor(R.color.color_7C69FE))
//
//                stvLike.setCompoundDrawables(null, chatDrawable, null, null)
                stvLike.setBackgroundResource(R.mipmap.resources_tanta_main_home_chat)
                stvLike.isSelected = true
            } else {

                if (UserManager.userInfo?.gender == 1) {
                    stvLike.setText(R.string.common_vqu_beckoning)
                } else {
                    stvLike.setText(R.string.common_vqu_accost)
                }
                stvLike.isSelected = false

//                stvLike.setStartColor(
//                    ResUtils.getColor(R.color.color_14FBB3AC),
//                    ResUtils.getColor(R.color.color_14FF3826)
//                )
//                stvLike.setTextColor(ResUtils.getColor(R.color.color_FF3826))
//
//                stvLike.setCompoundDrawables(null, likeDrawable, null, null)
                stvLike.setBackgroundResource(R.mipmap.resources_tanta_home_like)
            }

        } else {
            stvFocus.visibility = View.VISIBLE
            stvLike.visibility = View.GONE

            if (item.isFollow == 1) {
//                stvFocus.solidColor(
//                    ResUtils.getColor(R.color.color_D3D1D7),
//                    ResUtils.getColor(R.color.color_D3D1D7)
//                )
                stvFocus.solidColor = ResUtils.getColor(R.color.color_cccccc)
                if (item.isFans == 1) {
                    stvFocus.setText(R.string.mine_vqu_focused_each_other)
                } else {
                    stvFocus.setText(R.string.mine_vqu_focused)
                }
            } else {
                stvFocus.solidColor = ResUtils.getColor(R.color.color_B4A3FD)
//                stvFocus.setStartColor(
//                    ResUtils.getColor(R.color.color_BFB6FF), ResUtils.getColor(R.color.color_7C69FE)
//                )
                stvFocus.setText(R.string.mine_vqu_focus)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }
}