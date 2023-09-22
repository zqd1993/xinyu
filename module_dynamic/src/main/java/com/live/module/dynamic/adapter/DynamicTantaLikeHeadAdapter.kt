package com.live.module.dynamic.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.dynamic.R
import com.live.module.dynamic.bean.LikeBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage

/**
 * author: Tany
 * date: 2022/6/11
 * description:
 */
class DynamicTantaLikeHeadAdapter(data: MutableList<LikeBean>) :
    BaseQuickAdapter<LikeBean, BaseViewHolder>(R.layout.dynamic_tanta_item_like_head, data) {
    override fun convert(holder: BaseViewHolder, item: LikeBean) {
        holder.getView<ImageView>(R.id.iv_img)
            .vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.avatar,
                50,
                R.mipmap.ic_common_head_def)
    }
}