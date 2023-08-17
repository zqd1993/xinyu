package com.mshy.VInterestSpeed.common.ext

import android.widget.Toast
import com.live.vquonline.base.utils.ToastUtils

/**
 * author: Tany
 * date: 2022/4/9
 * description:
 */
fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.showToast(this, duration)
}

/**
 * 需要写日志到本地文件才用这个log
 */
fun String.xLogI(tag: String) {
    if (this.isNullOrEmpty()) {
        return
    }
}