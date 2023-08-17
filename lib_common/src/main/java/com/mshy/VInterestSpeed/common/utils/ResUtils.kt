package com.mshy.VInterestSpeed.common.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.live.vquonline.base.BaseApplication

object ResUtils {
    fun getString(@StringRes id: Int): String {
        return BaseApplication.context.resources.getString(id)
    }

    fun getColor(@ColorRes id: Int): Int {
        return BaseApplication.context.resources.getColor(id)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable {
        return BaseApplication.context.resources.getDrawable(id)
    }
}