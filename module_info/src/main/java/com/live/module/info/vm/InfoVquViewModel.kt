package com.live.module.info.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.info.bean.*
import com.live.module.info.repo.InfoVquActivityRepo
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import com.mshy.VInterestSpeed.common.bean.ProtectionOptionBean
import com.mshy.VInterestSpeed.common.bean.ProtectionStatusBean
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class InfoVquViewModel @Inject constructor(private val mVquRepo: InfoVquActivityRepo) :
    BaseViewModel() {
    val vquUserInfoData = MutableLiveData<BaseResponse<InfoVquInfoBean>>()
    val vquFollowData = MutableLiveData<BaseResponse<InfoVquFollowResultBean>>()
    val vquBlackData = MutableLiveData<BaseResponse<InfoVquFollowResultBean>>()
    val vquSendData = MutableLiveData<BaseResponse<Any>>()
    val vquGiftData = MutableLiveData<BaseResponse<InfoVquGiftBean>>()
    val vquCallData = MutableLiveData<BaseResponse<VideoVquCallBean>>()
    val vquAuthData = MutableLiveData<BaseResponse<CommonAuthBean>>()
    val vquSaveRemarkNameData = MutableLiveData<BaseResponse<Any>>()
    val vquProtectionStatusBean = MutableLiveData<BaseResponse<ProtectionStatusBean>>()
    val vquProtectionOptionBeanList = MutableLiveData<BaseResponse<MutableList<ProtectionOptionBean>>>()
    fun vquGetUserInfo(userId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["user_id"] = userId
            mVquRepo.vquGetUserInfo(params)
                .catch {  }
                .collect {
                    //3001
                    vquUserInfoData.postValue(it)
                }
        }
    }

    fun vquFollow(userId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["follow_uid"] = userId
            mVquRepo.vquFollow(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquFollowData.postValue(it)
                }
        }
    }

    fun vquBlack(userId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["black_uid"] = userId
            mVquRepo.vquBlack(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquBlackData.postValue(it)
                }
        }
    }

    fun vquSendBeckon(userId: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["user_ids"] = userId
            mVquRepo.vquSendBeckon(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSendData.postValue(it)
                }
        }
    }

    fun vquGetGiftList(userId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["user_id"] = userId
            mVquRepo.vquGetGiftList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquGiftData.postValue(it)
                }
        }
    }

    /**
     * 1v1视频
     */
    fun vquGetCallInfo(toUserId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["to_uid"] = toUserId
            mVquRepo.vquGetCallInfo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquCallData.postValue(it)
                }

        }

    }

    fun vquIsAuth() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquIsAuth(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquAuthData.postValue(it)
                }
        }
    }
    fun vquSaveRemarkName(userId: String, remarkName: String) {
        launchIO {
            mVquRepo.vquSaveRemarkName(userId, remarkName)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSaveRemarkNameData.postValue(it)
                }
        }
    }

    fun vquGuardianStatus(userId: Int) {
        launchIO {
            mVquRepo.vquGuardianStatus(userId)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquProtectionStatusBean.postValue(it)
                }
        }
    }

    fun vquGetGuardianOptions() {
        launchIO {
            mVquRepo.vquGetGuardianOptions()
                .catch { toast(it.message ?: "") }
                .collect {
                    vquProtectionOptionBeanList.postValue(it)
                }
        }
    }
}