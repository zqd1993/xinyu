package com.mshy.VInterestSpeed.common.utils

import android.content.Context
import com.live.vquonline.base.utils.GsonUtil
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.tencent.mmkv.MMKV

/**
 * MMKV使用封装
 *
 * 用户专用的MMKV
 * @since 8/28/20
 */
object UserSpUtils {

    /**
     * 初始化
     */
    fun initMMKV(context: Context): String? = MMKV.initialize(context)

    /**
     * 保存数据（简化）
     * 根据value类型自动匹配需要执行的方法
     */
    fun put(key: String, value: Any) =
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Double -> putDouble(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            else -> false
        }

    fun putString(key: String, value: String): Boolean? =
        MMKV.mmkvWithID("user")?.encode(key, value)

    fun getString(key: String, defValue: String? = null): String? =
        MMKV.mmkvWithID("user")?.decodeString(key, defValue)

    fun putInt(key: String, value: Int): Boolean? = MMKV.mmkvWithID("user")?.encode(key, value)

    fun getInt(key: String, defValue: Int = 0): Int? =
        MMKV.mmkvWithID("user")?.decodeInt(key, defValue)

    fun putLong(key: String, value: Long): Boolean? = MMKV.mmkvWithID("user")?.encode(key, value)

    fun getLong(key: String, defValue: Long = 0L): Long? =
        MMKV.mmkvWithID("user")?.decodeLong(key, defValue)

    fun putDouble(key: String, value: Double): Boolean? =
        MMKV.mmkvWithID("user")?.encode(key, value)

    fun getDouble(key: String, defValue: Double = 0.0): Double? =
        MMKV.mmkvWithID("user")?.decodeDouble(key, defValue)

    fun putFloat(key: String, value: Float): Boolean? = MMKV.mmkvWithID("user")?.encode(key, value)

    fun getFloat(key: String, defValue: Float = 0f): Float? =
        MMKV.mmkvWithID("user")?.decodeFloat(key, defValue)

    fun putBoolean(key: String, value: Boolean): Boolean? =
        MMKV.mmkvWithID("user")?.encode(key, value)

    fun getBoolean(key: String, defValue: Boolean = false): Boolean? =
        MMKV.mmkvWithID("user")?.decodeBool(key, defValue)

    fun contains(key: String): Boolean? = MMKV.mmkvWithID("user")?.contains(key)

    fun putBean(key: String, value: Any): Boolean? {
        val json = GsonUtil.GsonString(value)
        return MMKV.mmkvWithID("user")?.encode(key, json)
    }


    fun <T> getBean(key: String, cls: Class<T>): T? {
        val json = getString(key)
        if (!json.isNullOrEmpty()) {
            return GsonUtil.GsonToBean(json, cls)
        }
        return null
    }

    fun putUserBean(value: Any): Boolean? {
        if(value == null){
            return false
        }
        val json = GsonUtil.GsonString(value)
        return MMKV.mmkvWithID("user")?.encode(SpKey.userInfo, json)
    }

    fun getUserBean(): VquUserInfo? {
        val json = getString(SpKey.userInfo)
        if (!json.isNullOrEmpty()) {
            return GsonUtil.GsonToBean(json, VquUserInfo::class.java)
        }
        return null
    }


    fun clear() {
        MMKV.mmkvWithID("user")?.clearAll()
    }
}