package com.live.module.message.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.message.R
import com.live.module.message.bean.CallRecordData
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage

/**
 * FileName: com.live.module.message.adapter
 * Date: 2022/5/21 14:54
 * Description:
 * History:
 */
class MessageVquCallRecordAdapter(data: MutableList<CallRecordData.ListBean>) :
    BaseQuickAdapter<CallRecordData.ListBean, BaseViewHolder>(
        R.layout.message_tanta_call_record_item, data
    ) {


    override fun convert(holder: BaseViewHolder, item: CallRecordData.ListBean) {

        holder.setText(R.id.tv_nickname, item.nickname)
            .setText(
                R.id.tv_message, if (item.is_match == 1) {
                    "速配通话时长：${item.time}"
                } else {
                    "通话时长：${item.time}"
                }
            )
//            .setText(R.id.tv_intimate_value, item.intimate)
//            .setGone(R.id.tv_intimate_value, item.intimate.isEmpty())
            .setText(R.id.tv_date_time, item.date)
        val imageHead = holder.getView<ImageView>(R.id.img_head)

        imageHead.vquLoadCircleImage(
            NetBaseUrlConstant.IMAGE_URL + item.avatar, R.mipmap.ic_common_head_circle_def
        )
    }
}