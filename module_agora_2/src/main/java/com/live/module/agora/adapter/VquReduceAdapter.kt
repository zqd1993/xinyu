package com.live.module_agora.adapter

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.agora.R
import com.live.module.agora.bean.AgoraVquReduceBean

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/4/25 16:35
 *
 */
class VquReduceAdapter:  BaseQuickAdapter<AgoraVquReduceBean.TimeBean, BaseViewHolder>(R.layout.item_reduce) {


    override fun convert(helper: BaseViewHolder, timeBean: AgoraVquReduceBean.TimeBean) {
        resizeItemView(helper)
        if (timeBean.isCheck) {
            helper.getView<View>(R.id.tv_reduce).background =
                context.resources.getDrawable(R.drawable.bg_reduce_check)
            helper.setTextColor(R.id.tv_reduce, Color.parseColor("#7C69FE"))
        } else {
            helper.getView<View>(R.id.tv_reduce).background =
                context.resources.getDrawable(R.drawable.bg_reduce_uncheck)
            helper.setTextColor(R.id.tv_reduce, Color.parseColor("#273145"))
        }
        helper.setText(R.id.tv_reduce, timeBean.time)
    }

    private fun resizeItemView(helper: BaseViewHolder) {
        val layoutParams =
            helper.getView<View>(R.id.tv_reduce).layoutParams as RecyclerView.LayoutParams

        //对3取余
        val residue: Int = helper.layoutPosition % 3
        if (residue == 0) {
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = context.resources.getDimension(R.dimen.dp_size_8).toInt()
        } else if (residue == 1) { //当为每一列的第二个时
            layoutParams.leftMargin = context.resources.getDimension(R.dimen.dp_size_4).toInt()
            layoutParams.rightMargin = context.resources.getDimension(R.dimen.dp_size_4).toInt()
        } else if (residue == 2) {
            layoutParams.leftMargin = context.resources.getDimension(R.dimen.dp_size_8).toInt()
            layoutParams.rightMargin = 0
        }
        helper.getView<View>(R.id.tv_reduce).layoutParams = layoutParams
    }
}
