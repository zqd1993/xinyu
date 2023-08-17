package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton


/**
 * author: Lau
 * date: 2022/4/8
 * description:
 */
class CenterIconRadioButton constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : AppCompatRadioButton(
    context,
    attrs,
    defStyleAttr
) {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)


    override fun onDraw(canvas: Canvas?) {

        val drawables = compoundDrawables
        if (drawables != null) {
            val drawableLeft = drawables[0]
            if (drawableLeft != null) {
                val textWidth = paint.measureText(text.toString())
                val drawablePadding = compoundDrawablePadding
                val drawableWidth: Int = drawableLeft.intrinsicWidth
                val bodyWidth = textWidth + drawableWidth + drawablePadding
                canvas!!.translate((width - bodyWidth) / 2, 0f)
            }
        }

        super.onDraw(canvas)
    }

}