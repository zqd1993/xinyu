package com.live.module.bill.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.live.vquonline.base.ktx.dp2px

/**
 * author: Lau
 * date: 2022/4/20
 * description:
 */
class BillVquDetailItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter
        if (adapter is BillVquDetailAdapter) {
            val currentPosition = parent.getChildAdapterPosition(view)
            val item = adapter.getItemOrNull(currentPosition) ?: return

            if (item.isHeader) {
                outRect.bottom = context.dp2px(14f)
                outRect.top = context.dp2px(20f)
            } else {
                outRect.bottom = context.dp2px(5f)
                outRect.top = context.dp2px(0f)
            }
        }

    }
}