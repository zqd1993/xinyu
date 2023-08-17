package com.mshy.VInterestSpeed.common.ui.vm

import androidx.lifecycle.MutableLiveData
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.SystemBean
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ui.repo.SplashActivityRepo
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.mixpush.MixPushService
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
class SplashViewModel @Inject constructor(private val mRepo: SplashActivityRepo) : BaseViewModel() {

    /**
     * 数据
     */
    var systemBean = MutableLiveData<BaseResponse<SystemBean>>()


    var jumpAble = MutableLiveData<String?>()


    /**
     * 获取 banner 数据
     */
    fun getSystemIndex(bindData: String, channelCode: String) {
        launchIO {


            mRepo.getSystemIndex(bindData, channelCode)
                .catch {
                    toast(it.message ?: "")
                    jumpAble.postValue(null)
                }
                .collect {
                    //  ToastUtils.showToast(it.data.appurl,1000)
                    systemBean.postValue(it)

                    DeviceManager.getInstance().timestamp =
                        (it.data.timestamp - System.currentTimeMillis()).toInt()
                    DeviceManager.getInstance().openGreen = it.data.auditStatus
                    SpUtils.put(SpKey.baseUrl, it.data.appurl + "/")
                    SpUtils.put(SpKey.baseImageUrl, it.data.imageServer + "/")
                    SpUtils.put(SpKey.onKeyLogin, it.data.oneKeyLoginKey)
                    SpUtils.put(SpKey.openGreen, it.data.auditStatus)
                    SpUtils.putString(SpKey.systemIndex, GsonUtil.GsonString(it.data))
                    jumpAble.postValue(it.data.oneKeyLoginKey)
                    NIMClient.toggleNotification(it.data.auditStatus==0)//审核状态关闭提醒
                    NIMClient.getService(MixPushService::class.java).enable(it.data.auditStatus==0)//审核状态关闭推送
//                    SpUtils.put(SpKey.baseUrl,it.data.appurl+"/")
                }

//            launchIO {
//                mRepo.queryCities()
//                    .catch {
//                        jumpAble.postValue(true)
//                    }
//                    .collect {
//                        jumpAble.postValue(true)
//                    }
//            }
        }

    }

    fun getInviteId() {
        val clipboardMessage = getClipboardMessage()
        UserManager.inviteCode = clipboardMessage
    }

}