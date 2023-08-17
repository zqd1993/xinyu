package com.mshy.VInterestSpeed.uikit.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBroadUtils {
    /**
     * 隐藏键盘的方法
     *
     * @param context
     */
    @JvmStatic
    fun hideKeyboard(context: Activity) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.window.decorView.windowToken, 0)
    }

    @JvmStatic
    fun showSoftInput(context: Activity, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }


}