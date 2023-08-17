package com.live.module.setting.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.setting.bean.BlackListBean
import com.live.module.setting.bean.FateMatchBean
import com.live.module.setting.bean.SettingVquBindBean
import com.live.module.setting.bean.ShowLocationBean
import com.live.module.setting.repo.SettingVquActivityRepo
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.qiyukf.unicorn.api.Unicorn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class SettingVquViewModel @Inject constructor(private val mVquRepo: SettingVquActivityRepo) :
    BaseViewModel() {

    val vquSetVideoData = MutableLiveData<BaseResponse<Any>>()
    val vquSendCodeData = MutableLiveData<BaseResponse<Any>>()
    val vquBindData = MutableLiveData<BaseResponse<Any>>()
    val vquUserInfoData = MutableLiveData<BaseResponse<VquUserHomeBean>>()
    val vquBindInfoData = MutableLiveData<BaseResponse<SettingVquBindBean>>()
    val vquFateMatchData = MutableLiveData<BaseResponse<FateMatchBean>>()
    val vquSetFateMatchData = MutableLiveData<BaseResponse<Any>>()
    val vquSetPwdData = MutableLiveData<BaseResponse<Any>>()
    val vquCloseModeData = MutableLiveData<BaseResponse<Any>>()
    val vquBlackListData = MutableLiveData<BaseResponse<VquListBean<BlackListBean>>>()
    val vquDoBlackData = MutableLiveData<BaseResponse<Any>>()
    val vquCloseAccountData = MutableLiveData<BaseResponse<Any>>()
    val vquVersionData = MutableLiveData<BaseResponse<SettingVquVersionBean>>()
    val vquGetShowLocation = MutableLiveData<BaseResponse<ShowLocationBean>>()
    val vquSetShowLocation = MutableLiveData<BaseResponse<ShowLocationBean>>()
    val vquJumpAble = MutableLiveData<String?>()

    val vquAuthData = MutableLiveData<BaseResponse<CommonAuthBean>>()
    val vquLikeStatusData = MutableLiveData<BaseResponse<String>>()
    val vquSetLikeStatusData = MutableLiveData<BaseResponse<Any>>()
    fun vquSetVideo(status: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["status"] = status

            mVquRepo.vquSetVideoStatus(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSetVideoData.postValue(it)
                }
        }
    }

    fun vquSetUserVideo(status: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["is_msg_refuse"] = status

            mVquRepo.vquSetUserVideoStatus(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSetVideoData.postValue(it)
                }
        }
    }

    fun vquGetUserInfo() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquGetUserInfo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquUserInfoData.postValue(it)
                }
        }
    }

    fun vquGetBindInfo() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquGetBindInfo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquBindInfoData.postValue(it)
                }
        }
    }

    fun vquSendCode(mobile: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["mobile"] = mobile
            params["type"] = "bind"
            mVquRepo.vquSendCode(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSendCodeData.postValue(it)
                }
        }
    }

    fun vquSendCloseCode(mobile: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["mobile"] = mobile
            params["type"] = "findadpwd"
            mVquRepo.vquSendCode(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSendCodeData.postValue(it)
                }
        }
    }

    fun vquBindMobile(mobile: String?, code: String?) {
        if (mobile.isNullOrEmpty()) {
            toast("请输入手机号码")
            return
        }

        if (code.isNullOrEmpty()) {
            toast("请输入验证码")
            return
        }


        launchIO {
            val params = hashMapOf<String, Any>()
            params["mobile"] = mobile
            params["phone_code"] = code
            mVquRepo.vquBindMobile(params)
                .catch {
                    toast(it.message ?: "")
//                    vquBindData.postValue(BaseResponse(Any(), 0, ""))
                }
                .collect {
                    vquBindData.postValue(it)
                }
        }
    }

    fun vquCloseModeByCode(mobile: String, code: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["mobile"] = mobile
            params["phone_code"] = code
            mVquRepo.vquCloseModeByCode(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquBindData.postValue(it)
                }
        }
    }

    fun vquGetFateMatch() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquGetFateMatch(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquFateMatchData.postValue(it)
                }
        }
    }

    fun vquShowLocation() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquShowLocation(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquGetShowLocation.postValue(it)
                }
        }
    }

    fun vquSetShowLocation(show: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params.put("show_location", show)
            mVquRepo.vquShowLocation(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSetShowLocation.postValue(it)
                }
        }
    }

    fun vquSetFateMatch(match: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["fate_match"] = match
            mVquRepo.vquSetFateMatch(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSetFateMatchData.postValue(it)
                }
        }
    }

    fun vquSetTeenMode(pwd: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["password"] = pwd
            mVquRepo.vquSetTeenMode(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSetPwdData.postValue(it)
                }
        }
    }

    fun vquGetBlackList(page: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["page"] = page
            mVquRepo.vquGetBlackList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquBlackListData.postValue(it)
                }
        }
    }

    fun vquDoBlack(uid: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["black_uid"] = uid
            mVquRepo.vquDoBlack(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquDoBlackData.postValue(it)
                }
        }
    }

    fun vquCloseAccount() {
        var userId = UserManager.userInfo?.userId
        launchIO {
            val params = hashMapOf<String, Any>()
            params["user_id"] = userId ?: ""
            mVquRepo.vquCloseAccount(params)
                .catch {
                    toast(it.message ?: "")

                }
                .collect {
                    vquCloseAccountData.postValue(it)
                }
        }
    }

    fun vquCloseTeenMode(pwd: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["password"] = pwd
            params["is_open"] = 0
            mVquRepo.vquCloseTeenMode(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquCloseModeData.postValue(it)
                }
        }
    }

    fun vquCheckUpdate() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquCheckUpdate(params)
                .catch { }
                .collect {
                    vquVersionData.postValue(it)
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

    fun vquLoginOut() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquLoginOut(params)
                .catch { }
                .collect {
                    Unicorn.logout()
                }
        }
    }

    fun vquSystemIndex() {
        launchIO {
            mVquRepo.vquSystemIndex()
                .catch {
                    vquJumpAble.postValue(null)
                }
                .collect {
                    vquJumpAble.postValue(it.data.oneKeyLoginKey)
                }
        }
    }

    fun getUserLikeStatus() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.getUserLikeStatus(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquLikeStatusData.postValue(it)
                }
        }
    }

    fun setUserLikeStatus(status: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["status"] = status
            mVquRepo.setUserLikeStatus(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSetLikeStatusData.postValue(it)
                }
        }
    }

}