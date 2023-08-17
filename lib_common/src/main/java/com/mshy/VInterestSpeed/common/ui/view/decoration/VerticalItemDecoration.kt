package com.mshy.VInterestSpeed.common.ui.view.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.live.vquonline.base.ktx.dp2px

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
class VerticalItemDecoration(
    val context: Context,
    private val spaceDp: Float,
    private val firstTopSpaceDp: Float = 0f,
    private val lastBottomSpaceDp: Float = 0f
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter
        if (adapter is BaseQuickAdapter<*, *>) {
            when (parent.getChildAdapterPosition(view)) {
                0 -> {
                    if (firstTopSpaceDp > 0) {
                        outRect.top = context.dp2px(firstTopSpaceDp)
                    } else {
                        outRect.top = 0
                    }
                    outRect.bottom = context.dp2px(spaceDp / 2f)
                }
                adapter.data.size - 1 -> {
                    if (lastBottomSpaceDp > 0) {
                        outRect.bottom = context.dp2px(lastBottomSpaceDp)
                    } else {
                        outRect.bottom = 0
                    }
                    outRect.top = context.dp2px(spaceDp / 2f)
                }
                else -> {
                    outRect.top = context.dp2px(spaceDp / 2f)
                    outRect.bottom = context.dp2px(spaceDp / 2f)
                }
            }

        }
    }
}