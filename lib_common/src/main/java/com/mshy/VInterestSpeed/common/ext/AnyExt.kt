package com.mshy.VInterestSpeed.common.ext

import android.view.View
import com.apkfuns.logutils.LogUtils

/**
 * author: Tany
 * date: 2022/4/9
 * description:
 */

fun Any.logI() {
    LogUtils.i(this)
}

fun Any.logE() {
    LogUtils.e(this)
}

fun Any.logE(msg: String) {
    LogUtils.e(msg, this)
}

fun View.setVisible(isVisible: Boolean) {//设置可见或者隐藏
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}