package com.mshy.VInterestSpeed.common.utils

import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.uikit.common.http.CommonCallBack

/**
 * author: Tany
 * date: 2022/6/14
 * description:
 */
class CallUtil {
    companion object {
        fun call(vquVideoRequestBean: com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean, isFromNotification: Boolean) {
            val callTime = vquVideoRequestBean.data.callTime
            if (!TextUtils.isEmpty(callTime)) {
                val currentTimeMillis = com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getTime() / 1000
                // long currentTimeMillis = System.currentTimeMillis() / 1000;
                val time = callTime.toLong()
                val timeValue = currentTimeMillis - time
                var bv = UserManager.isVideo
                if (timeValue <= 180) {
                    var roomId = vquVideoRequestBean.data.roomid
                    if (!TextUtils.isEmpty(roomId)) {
                        Handler().postDelayed({
                            GlobalServiceManage.getRetrofit()
                                .create(GlobalApiService::class.java)
                                .vquGetCallInfoFromRoomId(roomId)
                                .enqueue(object :
                                    CommonCallBack<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>() {
                                    override fun onSuccess(data: com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean) {
                                        if (11 == vquVideoRequestBean.id) {
                                            ARouter.getInstance()
                                                .build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                                .withString(
                                                    RouteKey.SOCKET_URL,
                                                    data.socket_url ?: ""
                                                )
                                                .withBoolean("isCaller", false)
                                                .withBoolean(
                                                    "isMatch",
                                                    vquVideoRequestBean.data.isMatch
                                                )
                                                .withParcelable("CallBean", data)
                                                .navigation()
                                        } else {
                                            Log.d("aaaaa", "onSuccess: "+vquVideoRequestBean.data.isMatch)
                                            ARouter.getInstance()
                                                .build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                                                .withString(
                                                    RouteKey.SOCKET_URL,
                                                    data.socket_url ?: ""
                                                )
                                                .withBoolean("isCaller", false)
                                                .withBoolean(
                                                    "isMatch",
                                                    vquVideoRequestBean.data.isMatch
                                                )
                                                .withParcelable("CallBean", data)
                                                .navigation()
                                        }
                                    }

                                    override fun onFailed(t: BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>) {
                                        super.onFailed(t)
                                        if (isFromNotification) {
                                            //跳转到首页
                                            ARouter.getInstance()
                                                .build(RouteUrl.Main.CommonVquMainAcitvity)
                                                .withInt(RouteKey.POS, 0).navigation()
                                        }
                                    }
                                })
                        }, 100)

                    }
                }

            }
        }
    }
}