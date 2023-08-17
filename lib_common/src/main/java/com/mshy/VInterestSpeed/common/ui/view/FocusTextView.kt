package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import android.util.AttributeSet

/**
 * author: Lau
 * date: 2022/7/25
 * description:
 */
class FocusTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

}