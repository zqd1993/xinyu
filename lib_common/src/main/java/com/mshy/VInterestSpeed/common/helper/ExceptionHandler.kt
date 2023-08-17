package com.mshy.VInterestSpeed.common.helper

import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.ExitAgoraEvent
import com.mshy.VInterestSpeed.common.ext.logI
import com.mshy.VInterestSpeed.common.ext.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

/**
 * 响应code异常统一处理
 *
 * 该方法主要做两件事:
 *
 * - 1.做统一的code码处理
 * - 2.未进行统一处理的code码会被转换为自定义异常[ResponseException]抛出
 *
 * 使用方式为：进行统一处理的异常进行抛出[ResponseEmptyException]，未进行处理的code抛出[ResponseException]，成功状态下执行[successBlock]
 *
 * @param code Int code码
 * @param msg String? 错误信息
 * @param successBlock suspend () -> Unit 没有异常的情况下执行的方法体 可以在此处进行数据的发射
 * @throws ResponseException 未进行处理的异常会进行抛出，让ViewModel去做进一步处理
 */
@Throws(ResponseException::class)
suspend fun responseCodeExceptionHandler(
    code: Int,
    msg: String?,
    successBlock: suspend () -> Unit,
) {
    // 进行异常的处理
    when (code) {
        ResponseCodeEnum.SUCCESS.getCode() -> {
            successBlock.invoke()
        }
        ResponseCodeEnum.ERROR.getCode() -> {
            toast(msg ?: "", 1000)
            msg?.logI()
            throw ResponseEmptyException()
        }
        ResponseCodeEnum.NOT_LOGIN.getCode() -> {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    EventBus.getDefault().post("logout")
                    EventBus.getDefault().post(ExitAgoraEvent())
                }
            }

        }
        else -> {
            msg?.toast()
//            "网络异常，请稍后再试".toast()
        }
    }
}


@Throws(ResponseException::class)
suspend fun responseCodeExceptionHandler(
    code: Int,
    msg: String?,
    isErrorHandler: Boolean = true,
    successBlock: suspend () -> Unit,
) {
    // 进行异常的处理
    when (code) {
        ResponseCodeEnum.SUCCESS.getCode() -> {
            successBlock.invoke()
        }
        ResponseCodeEnum.ERROR.getCode() -> {
            if (isErrorHandler) {
                toast(msg ?: "", 1000)
                msg?.logI()
                throw ResponseEmptyException()
            } else {
                throw ResponseException(ResponseCodeEnum.ERROR, msg ?: "")
            }
        }
        else -> {
            msg?.toast()
//            "网络异常，请稍后再试".toast()
        }
    }
}

