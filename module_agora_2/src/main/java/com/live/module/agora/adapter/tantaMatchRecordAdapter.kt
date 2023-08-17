package com.live.module_agora.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.agora.R
import com.live.module.agora.bean.AgoraVquMatchRecordBean

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/4/25 16:35
 *
 */
class tantaMatchRecordAdapter :
    BaseQuickAdapter<AgoraVquMatchRecordBean.LogBean, BaseViewHolder>(R.layout.item_match_record) {

    override fun convert(helper: BaseViewHolder, recordBean: AgoraVquMatchRecordBean.LogBean) {
        helper.setText(R.id.vqu_tv_main_call, recordBean?.from_username)
        helper.setText(R.id.vqu_tv_vice_call, recordBean?.to_username)
    }

    override fun getItem(position: Int): AgoraVquMatchRecordBean.LogBean {
        val newPosition = position % data.size
        return data.get(newPosition)
    }

    override fun getItemViewType(position: Int): Int {
        var count = headerLayoutCount + data.size
        //刚开始进入包含该类的activity时,count为0。就会出现0%0的情况，这会抛出异常，所以我们要在下面做一下判断
        if (count <= 0) {
            count = 1
        }
        var newPosition = position % count;
        return super.getItemViewType(newPosition);
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

}
