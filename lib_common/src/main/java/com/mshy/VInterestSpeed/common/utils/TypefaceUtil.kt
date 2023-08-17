package com.mshy.VInterestSpeed.common.utils

import android.content.res.AssetManager
import android.graphics.Typeface
import android.widget.TextView
import com.live.vquonline.base.BaseApplication

/**
 * author: Tany
 * date: 2022/6/14
 * description:
 */
class TypefaceUtil {
    companion object{
        fun setFont1(tv:TextView){//DINCondensedBold字体
            val assetsManager: AssetManager = BaseApplication.application.assets //得到AssetManager
            val tf: Typeface =
                Typeface.createFromAsset(assetsManager, "fonts/DINCondensedBold.ttf") //根据路径得到Typeface
            tv.typeface = tf
        }
        fun setFont2(tv:TextView){//DINAlternateBold
            val assetsManager: AssetManager = BaseApplication.application.assets //得到AssetManager
            val tf: Typeface =
                Typeface.createFromAsset(assetsManager, "fonts/DINAlternateBold.ttf") //根据路径得到Typeface
            tv.typeface = tf
        }
    }
}