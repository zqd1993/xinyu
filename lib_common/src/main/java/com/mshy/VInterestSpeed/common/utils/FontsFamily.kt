package com.mshy.VInterestSpeed.common.utils

import android.graphics.Typeface
import com.live.vquonline.base.BaseApplication

/**
 * author: Lau
 * date: 2022/11/4
 * description:
 */
object FontsFamily {
    val tfDDinExpBold: Typeface by lazy {
        Typeface.createFromAsset(BaseApplication.application.assets, "fonts/D-DINExp-Bold.otf")
    }

}