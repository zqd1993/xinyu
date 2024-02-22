package com.live.module.setting.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.setting.R
import com.live.module.setting.bean.SettingBean
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible

/**
 * author: Tany
 * date: 2022/4/2
 * description:
 */
class SettingVquAdapter(data: MutableList<SettingBean>) :
    BaseQuickAdapter<SettingBean, BaseViewHolder>(R.layout.setting_tanta_item_setting, data) {
    init {
        addChildClickViewIds(R.id.iv_disturb)
    }
    override fun convert(holder: BaseViewHolder, item: SettingBean) {
        var tvTitle = holder.getView<TextView>(R.id.tv_title)
        var tvTip = holder.getView<TextView>(R.id.tv_tip)
        var ivDisturb = holder.getView<ImageView>(R.id.iv_disturb)
        var ivArrow = holder.getView<ImageView>(R.id.iv_arrow)
        var tvValue = holder.getView<TextView>(R.id.tv_value)
        if(holder.layoutPosition == data.size - 1){
            tvTitle.setTextColor(context.resources.getColor(R.color.color_FF8859))
        }
        tvTitle.text = item.title
        tvTip.text = item.tip
        if (item.isShowCheckBox) {
            ivDisturb.visible()
        } else {
            ivDisturb.gone()
        }
        ivDisturb.isSelected=item.isSelect
        if (item.isShowArrow) {
            ivArrow.visible()
        } else {
            ivArrow.gone()
        }
        tvValue.text = item.right
    }
}