package com.mshy.VInterestSpeed.uikit.common.http

import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.ResponseCodeEnum
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * FileName: com.mshy.VInterestSpeed.uikit.common.http
 * Date: 2022/4/28 16:29
 * Description:
 * History:
 */
abstract class CommonCallBack<T> : Callback<BaseResponse<T>> {

    override fun onResponse(call: Call<BaseResponse<T>>, response: Response<BaseResponse<T>>) {
        if (response.isSuccessful) {
            response.body()?.let {
                when (it.code) {
                    ResponseCodeEnum.SUCCESS.getCode() -> {
                        if (it.data != null) {
                            onSuccess(it.data)
                        } else {
                            if (showSuccessToast()) {
                                toast(it.message)
                            }
                        }
                    }
                    ResponseCodeEnum.ERROR.getCode() -> {
                        onFailed(it)
                    }
                    ResponseCodeEnum.NOT_ENOUGH_MONEY.getCode() -> {
                        onEnoughCoin(it.message)
                    }
                    ResponseCodeEnum.NOT_ENOUGH_COIN.getCode() -> {
                        onEnoughCoin(it.message)
                    }
                    else -> {
                        toast(it.message)
                    }
                }
            }
        } else {
            toast(response.message())
        }
    }

    override fun onFailure(call: Call<BaseResponse<T>>, t: Throwable) {
    }

    open fun onFailed(t: BaseResponse<T>) {
        toast(t.message)
    }

    open fun showSuccessToast(): Boolean {
        return true
    }

    //金币不足
    open fun onEnoughCoin(msg: String) {
       EventBus.getDefault().post("NotEnough")
    }

    abstract fun onSuccess(data: T)
}