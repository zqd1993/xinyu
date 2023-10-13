package com.mshy.VInterestSpeed.common.ui.vm

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ishumei.smantifraud.SmAntiFraud
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquAuthBean
import com.mshy.VInterestSpeed.common.bean.CommonVquStatusBean
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ui.repo.SplashActivityRepo
import com.mshy.VInterestSpeed.common.utils.LoginUtils
import com.mshy.VInterestSpeed.uikit.bean.UpdateOnlineBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:30
 *
 */
@HiltViewModel
class CommonVquMainViewModel @Inject constructor(private val mRepo: SplashActivityRepo) :
    BaseViewModel() {

    val vquGetTeenModeData = MutableLiveData<BaseResponse<CommonVquStatusBean>>()
    val vquOnlineUpdateData = MutableLiveData<BaseResponse<UpdateOnlineBean>>()

    val videoVquCallBean =
        MutableLiveData<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>()

    val isAuthData = MutableLiveData<CommonVquAuthBean>()

    /**
     * 获取 banner 数据
     */
    fun getSystemIndex() {
        launchIO {
//            mRepo.getSystemIndex(bindData, channelCode)
//                .catch { toast(it.message ?: "") }
//                .collect {
//                    //  ToastUtils.showToast(it.data.appurl,1000)
//                    //  systemBean.postValue(it)
//                }

        }

    }

    /**
     * 1v1视频
     */
    fun vquGetCallInfoFromRoom(room_id: Int) {
        launchIO {
            mRepo.vquGetCallInfoFromRoom(room_id)
                .catch { toast(it.message ?: "") }
                .collect {
                    if (it.data != null) {
                        videoVquCallBean.postValue(it.data!!)
                    }
                }

        }

    }


    /**
     * 获取 banner 数据
     */
    fun vquGetGlobalConfig() {
        launchIO {
            mRepo.vquGetGlobalConfig()
                .catch { toast(it.message ?: "") }
                .collect {
                    //  ToastUtils.showToast(it.data.appurl,1000)
                    com.mshy.VInterestSpeed.common.helper.CommonVquWebUrlHelper.getInstance()
                        .saveUrl(it.data.webUrl)
                    val serviceId = StringBuilder()
                    for (i in 0 until it.data.config.serviceId.size) {
                        serviceId.append(it.data.config.serviceId[i])
                            .append(",")
                    }
                    if (serviceId.toString().endsWith(",")) {
                        SpUtils.putString(
                            SpKey.SERVICE_ID,
                            serviceId.substring(0, serviceId.length - 1)
                        )
                    }
                    val woman_hang_up_tips: Int = it.data.config.woman_hang_up_tips
                    val is_audio_video_mini_open: Boolean = it.data.config.is_audio_video_mini_open
                    com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("curSecondInMinute000=$woman_hang_up_tips")
                    SpUtils.putInt(
                        SpKey.woman_hang_up_tips, woman_hang_up_tips
                    )
                    SpUtils.putBoolean(
                        SpKey.is_audio_video_mini, is_audio_video_mini_open
                    )
                    var is_audio_video_mini = SpUtils.getBoolean(SpKey.is_audio_video_mini, false)

                    com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("is_audio_video_mini=$is_audio_video_mini")
                }

        }
    }

    fun vquGetBadgeConfig() {
        launchIO {
            mRepo.vquGetBadgeConfig()
                .catch { }
                .collect {
                    SpUtils.putString(SpKey.badgeConfig, it.data.toJSONString())
                }
        }
    }

    fun vquGetTeenMode() {
        launchIO {
            mRepo.vquGetTeenMode()
                .catch { }
                .collect {
                    vquGetTeenModeData.postValue(it)
                }

        }

    }


    fun isAuth() {
        launchIO {
            mRepo.isAuth()
                .catch { }
                .collect {
                    isAuthData.postValue(it.data!!)
                }
        }
    }

    fun getOnlineUpdate() {
        launchIO {
            mRepo.getOnlineUpdate()
                .catch { }
                .collect {
                    vquOnlineUpdateData.postValue(it)
                }
        }
    }

    fun getOnlineUpdateNotResponse() {
        launchIO {
            mRepo.getOnlineUpdate()
                .catch { }
                .collect {
                }
        }
    }

    fun vquQueryCities() {
        launchIO {
            mRepo.queryCities()
                .catch {
//                    jumpAble.postValue(true)
                }
                .collect {
//                    jumpAble.postValue(true)
                }
        }
    }

    fun vquCloseInfoFinishTip() {
        launchIO {
            mRepo.vquCloseInfoFinishTip()
                .catch { toast(it.message ?: "") }
                .collect {

                }
        }
    }

    fun initSm() {
        val deviceId = SpUtils.getString(SpKey.DEVICE_ID, "")
        val smEventId = "login"
        val smType = "phoneMessage"
        if(deviceId!!.isEmpty()) {
            LoginUtils.initSm(smType, smEventId)
        }
    }
}