package com.mshy.VInterestSpeed.common.ui.view.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.sp2px
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.City
import com.mshy.VInterestSpeed.common.utils.ResUtils


/**
 * author: Lau
 * date: 2022/4/12
 * description:
 */
class SectionItemDecoration(val context: Context, val data: List<City>) :
    RecyclerView.ItemDecoration() {

    private var mSectionHeight = context.dp2px(21f).toFloat()

    private val mBgPaint: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ResUtils.getColor(R.color.color_FFFFFF)
        }
    }
    private val mTextPaint: TextPaint by lazy {
        TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = context.sp2px(16f).toFloat()
            color = ResUtils.getColor(R.color.color_A3AABE)
        }
    }

    private val mBounds: Rect by lazy {
        Rect()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        Log.d("TAG", "onDraw: -2")
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {

            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition

            if (data.isNotEmpty() && position < data.size && position >= 0) {
                if (position == 0) {
                    drawSection(c, left, right, child, params, position)
                } else {
                    if (null != data[position].getSection()
                        && !data[position].getSection()
                            .equals(data[position - 1].getSection())
                    ) {
                        drawSection(c, left, right, child, params, position)
                    }
                }
            }
        }
    }

    private fun drawSection(
        c: Canvas, left: Int, right: Int, child: View,
        params: RecyclerView.LayoutParams, position: Int
    ) {
        c.drawRect(
            left.toFloat(),
            child.top - params.topMargin - mSectionHeight,
            right.toFloat(), (
                    child.top - params.topMargin).toFloat(), mBgPaint
        )
        mTextPaint.getTextBounds(
            data[position].getSection(),
            0,
            data[position].getSection()!!.length,
            mBounds
        )
        c.drawText(
            data[position].getSection()!!,
            child.paddingLeft.toFloat()+ context.dp2px(14f),
            child.top - params.topMargin - (mSectionHeight / 2 - mBounds.height() / 2),
            mTextPaint
        )
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val pos = (parent.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (pos < 0) return
        if (data.isEmpty()) return
        val section = data[pos].getSection()
        val child = parent.findViewHolderForLayoutPosition(pos)!!.itemView

        var flag = false
        if (pos + 1 < data.size) {
            if (null != section && section != data[pos + 1].getSection()) {
                if (child.height + child.top < mSectionHeight) {
                    c.save()
                    flag = true
                    c.translate(0f, child.height + child.top - mSectionHeight)
                }
            }
        }
        c.drawRect(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat(),
            (parent.right - parent.paddingRight).toFloat(),
            parent.paddingTop + mSectionHeight, mBgPaint
        )
        mTextPaint.getTextBounds(section, 0, section!!.length, mBounds)
        c.drawText(
            section,
            child.paddingLeft.toFloat() + context.dp2px(14f),
            parent.paddingTop + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2),
            mTextPaint
        )
        if (flag) c.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (data.isNotEmpty() && position <= data.size - 1 && position > -1) {
            if (position == 0) {
                outRect[0, mSectionHeight.toInt(), 0] = 0
            } else {
                if (null != data[position].getSection()
                    && data[position].getSection() != data[position - 1].getSection()
                ) {
                    outRect[0, mSectionHeight.toInt(), 0] = 0
                }
            }
        }
    }

}