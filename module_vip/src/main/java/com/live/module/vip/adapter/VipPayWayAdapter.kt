package com.live.module.vip.adapter

import android.widget.CheckBox
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.vip.R
import com.live.module.vip.bean.VipPayWayBean
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadImage

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
class VipPayWayAdapter :
    BaseQuickAdapter<VipPayWayBean, BaseViewHolder>(R.layout.vip_item_dialog_pay_way) {
    var selectIndex: Int = -1
    override fun convert(holder: BaseViewHolder, item: VipPayWayBean) {
        var adapterPosition = holder.adapterPosition
        holder.getView<ImageView>(R.id.iv_type)
            .vquLoadImage(item.icon)

        holder.setText(R.id.tv_type, item.name)
        var checkBox = holder.getView<CheckBox>(R.id.cb_type)
        com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("selectIndex=$selectIndex...adapterPosition=$adapterPosition")
        checkBox.isChecked = adapterPosition == selectIndex
        holder.getView<com.mshy.VInterestSpeed.common.ui.view.ShapeRelativeLayout>(R.id.ll_vqu_pay_pay).setViewClickListener(1) {
            if (selectIndex == adapterPosition) {
                return@setViewClickListener
            }
            selectIndex = adapterPosition
            notifyDataSetChanged()
        }
    }
}