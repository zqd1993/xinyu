package com.mshy.VInterestSpeed.common.ext

import android.graphics.Color
import android.widget.TextView

/**
 * author: Tany
 * date: 2022/7/19
 * description:
 */
fun TextView.setVip(isVip: Boolean) {//设置vip红名
    if (isVip) {
        this.setTextColor(Color.parseColor("#934800"))
    } else {
        this.setTextColor(Color.parseColor("#222222"))
    }
}
