package com.live.module.relation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.relation.R
import com.live.module.relation.bean.VquRelationPraiseBean
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.view.CommonVquAvatarFrameView

/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
class RelationVquPraiseAdapter :
    BaseQuickAdapter<VquRelationPraiseBean, BaseViewHolder>(
        R.layout.relation_vqu_item_praise_list
    ) {

    init {
        addChildClickViewIds(R.id.cl_relation_vqu_item_praise_list_dynamic_parent)
    }

    override fun convert(
        holder: BaseViewHolder,
        item: VquRelationPraiseBean
    ) {
        val sivAvatar =
            holder.getView<CommonVquAvatarFrameView>(R.id.siv_relation_item_relation_list_avatar)

        sivAvatar.loadAvatar(item.avatar, true)
        sivAvatar.loadAvatarFrame(item.avatarFrame, true)


        holder.setText(R.id.tv_relation_item_relation_list_name, item.nickname)

        val clRoot = holder.getView<ViewGroup>(R.id.cl_relation_item_root)

        com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper.addGender(
            context,
            clRoot,
            item.gender,
            item.age.toString()
        )

        holder.setText(
            R.id.tv_relation_item_relation_list_desc, item.date
        )

        val ivCover = holder.getView<ImageView>(R.id.iv_relation_item_praise_list_image)
        val ivPlay = holder.getView<ImageView>(R.id.iv_relation_item_praise_list_play)
        val tvContent = holder.getView<TextView>(R.id.tv_relation_item_relation_list_content)

        if (item.type == 0 || item.type == 2) {
            ivCover.visibility = View.VISIBLE
            ivPlay.visibility = View.VISIBLE
            tvContent.visibility = View.GONE
            ivCover.vquLoadRoundImage(item.coverUrl, context.dp2px(3f))
        } else if (item.type == 1) {
            ivPlay.visibility = View.GONE
            if (item.coverUrl.isNullOrEmpty()) {
                ivCover.visibility = View.GONE
                tvContent.visibility = View.VISIBLE
                tvContent.text = item.content
            } else {
                ivCover.visibility = View.VISIBLE
                tvContent.visibility = View.GONE
                ivCover.vquLoadRoundImage(item.coverUrl, context.dp2px(3f))
            }
        }


    }
}