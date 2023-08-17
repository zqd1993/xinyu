package com.mshy.VInterestSpeed.common.helper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable
import java.util.*

/**
 * Author: Reisen
 * Date: 2022/2/22 15:25
 * Description: 路由帮助类
 * History:
 */
internal object ARouterHelper {
    /**
     * 是否初始化
     */
    private var isHasInit = false

    internal lateinit var aRouter: ARouter

    /**
     * 初始化
     */
    fun init(application: Application, isDebug: Boolean) {
        if (isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
        isHasInit = true
        aRouter = ARouter.getInstance()
    }

    fun inject(any: Any) {
        aRouter.inject(any)
    }

}

/**
 * 为跳转添加flag值
 */
const val ROUTER_CONFIG_FLAG = "ARouter_Flag"

/**
 * 为跳转添加绿色通道
 */
const val ROUTER_CONFIG_GREEN_CHANNEL = "ARouter_Green_Channel"

/**
 * 为跳转添加ActivityOptionsCompat
 */
const val ROUTER_CONFIG_ACTIVITY_OPTIONS_COMPAT = "ActivityOptionsCompat"

fun getARouterFragment(path: String): Fragment {
    return ARouterHelper.aRouter.build(path).navigation() as Fragment
}

fun startARouterActivity(path: String) {
    ARouterHelper.aRouter.build(path).navigation()
}

fun startARouterActivity(path: String, key: String, bundle: Bundle?) {
    ARouterHelper.aRouter.build(path).withBundle(key, bundle).navigation()
}

fun startARouterActivityForResult(path: String, context: Activity, requestCode: Int) {
    startARouterActivityForResult(path, null, null, context, requestCode)
}

fun startARouterActivityForResult(
    path: String,
    key: String?,
    bundle: Bundle?,
    context: Activity?,
    requestCode: Int
) {
    ARouterHelper.aRouter.build(path).withBundle(key, bundle).navigation(context, requestCode)
}

/**
 * 带参数的跳转（参数 "key","value" 形式
 *
 * @param path   路径
 * @param params 参数数组（可配置特殊参数
 */
fun startARouterActivityForResult(
    path: String,
    context: Activity,
    requestCode: Int,
    vararg params: Any
) {
    val postcard: Postcard = ARouterHelper.aRouter.build(path)
    if (params.size % 2 == 0) {
        var i = 0
        while (i < params.size) {
            if (params[i] is String) {
                val key = params[i] as String
                val value = params[i + 1]
                parseAdd(key, value, postcard)
            } else {
                throw IllegalArgumentException("传递key类型错误，必须为String类型")
            }
            i += 2
        }
        postcard.navigation(context, requestCode)
    } else {
        throw IllegalArgumentException("传递参数错误，缺少value")
    }
}

/**
 * 带参数的跳转（参数 "key","value" 形式
 *
 * @param path   路径
 * @param params 参数数组（可配置特殊参数
 */
fun startARouterActivity(path: String?, vararg params: Any) {
    val postcard: Postcard = ARouterHelper.aRouter.build(path)
    if (params.size % 2 == 0) {
        var i = 0
        while (i < params.size) {
            if (params[i] is String) {
                val key = params[i] as String
                val value = params[i + 1]
                parseAdd(key, value, postcard)
            } else {
                throw IllegalArgumentException("传递key类型错误，必须为String类型")
            }
            i += 2
        }
        postcard.navigation()
    } else {
        throw IllegalArgumentException("传递参数错误，缺少value")
    }
}


/**
 * 备注：List数据类型  需要用getIntent的方式获取  不能使用@Autowired注解
 * 使用@Autowired字段 修饰符不能使用private 和 protected
 * 解析参数并添加至Postcard
 */
private fun parseAdd(key: String, value: Any, postcard: Postcard) {
    if (checkHasConfigParams(key, value, postcard)) {
        return
    }
    when (value) {
        is String -> {
            postcard.withString(key, value)
        }
        is Int -> {
            postcard.withInt(key, value)
        }
        is Float -> {
            postcard.withFloat(key, value)
        }
        is Double -> {
            postcard.withDouble(key, value)
        }
        is Long -> {
            postcard.withLong(key, value)
        }
        is List<*> -> {
            postcard.withParcelableArrayList(key, value as ArrayList<out Parcelable?>)
        }
        is Parcelable -> {
            postcard.withParcelable(key, value)
        }
        is Serializable -> {
            postcard.withSerializable(key, value)
        }
        is Bundle -> {
            postcard.with(value)
        }
    }
}

/**
 * 检查是否含有ARouter的配置参数
 *
 * @param key      键
 * @param postcard 注入对象
 * @return 是否含有ARouter的配置参数
 * @value value 值
 */
private fun checkHasConfigParams(key: String, value: Any, postcard: Postcard): Boolean {
    return when (key) {
        ROUTER_CONFIG_FLAG -> {
            postcard.withFlags(value as Int)
            true
        }
        ROUTER_CONFIG_GREEN_CHANNEL -> {
            val isGreen = value as Boolean
            if (isGreen) {
                postcard.greenChannel()
            }
            true
        }
        ROUTER_CONFIG_ACTIVITY_OPTIONS_COMPAT -> {
            postcard.withOptionsCompat(value as ActivityOptionsCompat)
            true
        }
        else -> {
            false
        }
    }
}