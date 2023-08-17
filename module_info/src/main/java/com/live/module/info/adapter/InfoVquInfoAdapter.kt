package com.live.module.info.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.info.R
import com.live.module.info.bean.BasicInfo
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.ToastUtils
import com.mshy.VInterestSpeed.common.ext.setViewClickListener

/**
 * author: Tany
 * date: 2022/4/27
 * description:
 */
class InfoVquInfoAdapter(data: MutableList<BasicInfo>) :
    BaseQuickAdapter<BasicInfo, BaseViewHolder>(R.layout.info_tanta_info_item, data) {
    override fun convert(holder: BaseViewHolder, item: BasicInfo) {
        holder.getView<TextView>(R.id.tv_key).text = item.title
        holder.getView<TextView>(R.id.tv_value).text = item.value
        var ivCopy = holder.getView<ImageView>(R.id.iv_copy)
        if (item.key == "usercode") {
            ivCopy.visible()
            ivCopy.setViewClickListener {
                val cm: ClipboardManager =
                    context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText(null, item.value))
                ToastUtils.showLong("复制成功，快去粘贴吧！")
            }
        } else {
            ivCopy.gone()
        }
        if("gender" == item.key){
            if("1" == item.value){
                holder.setText(R.id.tv_value, "女");
            }else {
                holder.setText(R.id.tv_value, "男");
            }
        }

    }
}