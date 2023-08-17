package com.live.vquonline.base.utils

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * MMKV使用封装
 *
 * ""
 * @since 8/28/20
 */
object SpUtils {


    /**
     * 初始化
     */
    @JvmStatic
    fun initMMKV(context: Context): String? = MMKV.initialize(context)

    /**
     * 保存数据（简化）
     * 根据value类型自动匹配需要执行的方法
     */
    @JvmStatic
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

    @JvmStatic
    fun putString(key: String, value: String): Boolean? = MMKV.defaultMMKV()?.encode(key, value)

    @JvmStatic
    fun getString(key: String, defValue: String? = null): String? =
        MMKV.defaultMMKV()?.decodeString(key, defValue)

    @JvmStatic
    fun putInt(key: String, value: Int): Boolean? = MMKV.defaultMMKV()?.encode(key, value)

    @JvmStatic
    fun getInt(key: String, defValue: Int = 0): Int? = MMKV.defaultMMKV()?.decodeInt(key, defValue)

    @JvmStatic
    fun putLong(key: String, value: Long): Boolean? = MMKV.defaultMMKV()?.encode(key, value)

    @JvmStatic
    fun getLong(key: String, defValue: Long = 0L): Long? =
        MMKV.defaultMMKV()?.decodeLong(key, defValue)

    @JvmStatic
    fun putDouble(key: String, value: Double): Boolean? = MMKV.defaultMMKV()?.encode(key, value)

    @JvmStatic
    fun getDouble(key: String, defValue: Double = 0.0): Double? =
        MMKV.defaultMMKV()?.decodeDouble(key, defValue)

    @JvmStatic
    fun putFloat(key: String, value: Float): Boolean? = MMKV.defaultMMKV()?.encode(key, value)

    @JvmStatic
    fun getFloat(key: String, defValue: Float = 0f): Float? =
        MMKV.defaultMMKV()?.decodeFloat(key, defValue)

    @JvmStatic
    fun putBoolean(key: String, value: Boolean): Boolean? = MMKV.defaultMMKV()?.encode(key, value)

    @JvmStatic
    fun getBoolean(key: String, defValue: Boolean = false): Boolean? =
        MMKV.defaultMMKV()?.decodeBool(key, defValue)

    @JvmStatic
    fun contains(key: String): Boolean? = MMKV.defaultMMKV()?.contains(key)

    @JvmStatic
    fun putBean(key: String, value: Any): Boolean? {
        val json = GsonUtil.GsonString(value)
        return MMKV.defaultMMKV()?.encode(key, json)
    }

    @JvmStatic
    fun <T> getBean(key: String, cls: Class<T>): T? {
        val json = getString(key)
        if (!json.isNullOrEmpty()) {
            return GsonUtil.GsonToBean(json, cls)
        }
        return null
    }
}