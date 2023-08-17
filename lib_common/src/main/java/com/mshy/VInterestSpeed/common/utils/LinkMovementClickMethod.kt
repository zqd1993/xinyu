package com.mshy.VInterestSpeed.common.utils

import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * author: Tany
 * date: 2022/8/9
 * description:
 */
class LinkMovementClickMethod : LinkMovementMethod() {
    private var lastClickTime: Long = 0
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action: Int = event.getAction()
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN
        ) {
            var x = event.getX() as Int
            var y = event.getY() as Int
            x -= widget.getTotalPaddingLeft()
            y -= widget.getTotalPaddingTop()
            x += widget.getScrollX()
            y += widget.getScrollY()
            val layout: Layout = widget.getLayout()
            val line: Int = layout.getLineForVertical(y)
            val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())
            val link: Array<ClickableSpan> = buffer.getSpans(off, off, ClickableSpan::class.java)
            if (link.size != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
                        link[0].onClick(widget)
                    }
                } else if (action == MotionEvent.ACTION_DOWN) {
                    val spanStart = buffer.getSpanStart(link[0])
                    val spanEnd = buffer.getSpanEnd(link[0])
                    if (spanStart >= 0 && spanEnd >= 0) {
                        Selection.setSelection(
                            buffer,
                            spanStart,
                            spanEnd
                        )
                    }
                    lastClickTime = System.currentTimeMillis()
                }
                return true
            } else {
                Selection.removeSelection(buffer)
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    companion object {
        private const val CLICK_DELAY = 500L
        val instance: LinkMovementClickMethod?
            get() {
                if (null == sInstance) {
                    sInstance = LinkMovementClickMethod()
                }
                return sInstance
            }
        private var sInstance: LinkMovementClickMethod? = null
    }
}