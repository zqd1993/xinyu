package com.mshy.VInterestSpeed.common.bean

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.parcelize.Parcelize
import java.util.regex.Pattern

/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
@Parcelize
data class City(
    var name: String,
    var province: String,
    var pinyin: String,
    var code: String
) : Parcelable {

    /***
     * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
     * @return
     */
    fun getSection(): String? {
        return if (TextUtils.isEmpty(pinyin)) {
            "#"
        } else {
            val c = pinyin.substring(0, 1)
            val p = Pattern.compile("[a-zA-Z]")
            val m = p.matcher(c)
            if (m.matches()) {
                c.uppercase()
            } else if (TextUtils.equals(c, "定") || TextUtils.equals(c, "热")) pinyin else "#"
        }
    }

}