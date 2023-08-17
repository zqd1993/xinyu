package com.live.module.info.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.info.R
import com.live.module.info.bean.DynamicBean
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.utils.UiUtils

/**
 * author: Tany
 * date: 2022/4/26
 * description:
 */
class InfoVquSmallDynamicAdapter(data: MutableList<DynamicBean>) :
    BaseQuickAdapter<DynamicBean, BaseViewHolder>(R.layout.info_tanta_dynamic_small_item, data) {
    override fun convert(holder: BaseViewHolder, item: DynamicBean) {
        var img = holder.getView<ImageView>(R.id.iv_img)
        var ivPlay = holder.getView<ImageView>(R.id.iv_play)

        if (item.type == 1) {
            ivPlay.visible()
            img.vquLoadRoundImage(item.url, UiUtils.dip2px(context, 8f))
        } else {
            ivPlay.gone()
            img.vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.url,
                UiUtils.dip2px(context, 8f))
        }
    }

}