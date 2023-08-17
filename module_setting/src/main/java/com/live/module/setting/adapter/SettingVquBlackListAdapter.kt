package com.live.module.setting.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.setting.R
import com.live.module.setting.bean.BlackListBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage

/**
 * author: Tany
 * date: 2022/4/2
 * description:
 */
class SettingVquBlackListAdapter :
    BaseQuickAdapter<BlackListBean, BaseViewHolder>(R.layout.setting_tanta_item_blacklist) {
    init {
        addChildClickViewIds(R.id.tv_remove)
    }

    override fun convert(holder: BaseViewHolder, item: BlackListBean) {
        holder.getView<ImageView>(R.id.iv_head)
            .vquLoadCircleImage(NetBaseUrlConstant.IMAGE_URL + item.avatar,
                R.mipmap.ic_common_head_circle_def)
        holder.getView<TextView>(R.id.tv_name).text = item.nickname
        holder.getView<TextView>(R.id.tv_title).text = item.sign
        com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper.addGender(
            context,
            holder.getView<LinearLayout>(R.id.cl_root),
            item.gender,
            item.age.toString()
        )
    }
}