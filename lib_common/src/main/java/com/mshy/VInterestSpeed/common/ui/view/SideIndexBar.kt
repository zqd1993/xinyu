package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.live.vquonline.base.ktx.sp2px
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.utils.ResUtils

/**
 * author: Lau
 * date: 2022/4/12
 * description:
 */
class SideIndexBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(
    context,
    attrs,
    defStyleAttr
) {


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)


    private val mIndexItems = arrayListOf(
        "定位", "A", "B", "C", "D", "E", "F",
        "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V",
        "W", "X", "Y", "Z", "#"
    )

    private var mTextSize = 0f
    private var mTextColor = 0
    private var mTextTouchedColor = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mItemHeight = 0
    private var mTopMargin = 0
    private var mCurrentIndex = -1

    private lateinit var mPaint: Paint
    private lateinit var mTouchedPaint: Paint

    private var mOnIndexChangedListener: OnIndexTouchedChangedListener? = null

    private var mOverlayTextView: TextView? = null

    init {
        init(getContext())
    }

    private fun init(context: Context) {
        mTextSize = context.sp2px(10f).toFloat()
        mTextColor = ResUtils.getColor(R.color.color_A3AABE)
        mTextTouchedColor = ResUtils.getColor(R.color.color_273145)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textSize = mTextSize
        mPaint.color = mTextColor

        mTouchedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTouchedPaint.textSize = mTextSize
        mTouchedPaint.color = mTextTouchedColor
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var index: String
        for (i in mIndexItems.indices) {
            index = mIndexItems[i]
            val fm = mPaint.fontMetrics
            canvas!!.drawText(
                index,
                (mWidth - mPaint.measureText(index)) / 2,
                mItemHeight / 2 + (fm.bottom - fm.top) / 2 - fm.bottom + mItemHeight * i + mTopMargin,
                if (i == mCurrentIndex) mTouchedPaint else mPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = h.coerceAtLeast(oldh)
        mItemHeight = mHeight / mIndexItems.size
        mTopMargin = (mHeight - mItemHeight * mIndexItems.size) / 2
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        performClick()
        when (event!!.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val y = event.y
                val indexSize = mIndexItems.size
                var touchedIndex = (y / mItemHeight).toInt()
                if (touchedIndex < 0) {
                    touchedIndex = 0
                } else if (touchedIndex >= indexSize) {
                    touchedIndex = indexSize - 1
                }
                if (mOnIndexChangedListener != null && touchedIndex >= 0 && touchedIndex < indexSize) {
                    if (touchedIndex != mCurrentIndex) {
                        mCurrentIndex = touchedIndex
                        mOverlayTextView?.visibility = VISIBLE
                        mOverlayTextView?.text = mIndexItems[touchedIndex]
                        mOnIndexChangedListener?.onIndexChanged(
                            mIndexItems[touchedIndex],
                            touchedIndex
                        )
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mCurrentIndex = -1
                mOverlayTextView?.visibility = GONE
                invalidate()
            }
        }
        return true
    }

    fun setOnIndexChangedListener(listener: OnIndexTouchedChangedListener?): SideIndexBar? {
        mOnIndexChangedListener = listener
        return this
    }

    fun setOverlayTextView(overlay: TextView?): SideIndexBar? {
        mOverlayTextView = overlay
        return this
    }


    interface OnIndexTouchedChangedListener {
        fun onIndexChanged(index: String?, position: Int)
    }

}