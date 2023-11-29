package com.live.module.login.vm

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.fm.openinstall.OpenInstall
import com.fm.openinstall.listener.AppInstallAdapter
import com.fm.openinstall.model.AppData
import com.live.module.login.R
import com.live.module.login.bean.Gender
import com.live.module.login.bean.LoginTantaNicknameBean
import com.live.module.login.bean.Nickname
import com.live.module.login.repo.LoginTantaSetInfoRepository
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.ToastUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.TantaLocationBean
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.LoginUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/8
 * description:
 */
@HiltViewModel
class LoginTantaSetInfoViewModel @Inject constructor(private val repository: LoginTantaSetInfoRepository) :
    BaseViewModel() {

//    val avatars = MutableLiveData<List<String>>()
//    val nicknames = MutableLiveData<List<Nickname>>()

    val loginTantaAvatar = MutableLiveData<String>()
    val loginTantaNickname = MutableLiveData<Nickname>()

    val loginTantaNicknameData = MutableLiveData<LoginTantaNicknameBean>()

    val locationData = MutableLiveData<TantaLocationBean>()

    val jumpAble = MutableLiveData<String?>()


    val opBindData = MutableLiveData<String?>()


    private var channelCode: String? = null

    private var bindData: String? = null

    var mSetInfoIsOneKeyPass: Boolean = false


    fun tantaNickname() {
        launchIO {
            repository.tantaNickname()
                .catch {

                }
                .collect {
                    loginTantaNicknameData.postValue(it.data!!)
                }
        }
    }


    fun randomNicknameAndAvatar(
        gender: Gender?,
        needNickName: Boolean = true,
        needAvatar: Boolean = true
    ) {
        if (gender == null || gender.nickname.isNullOrEmpty() || gender.avatar.isNullOrEmpty()) {
            tantaNickname()
        } else {
            if (needNickName) {
                this.loginTantaNickname.postValue(gender.nickname[(gender.nickname.indices).random()])
            }
            if (needAvatar) {
                this.loginTantaAvatar.postValue(gender.avatar[0])
            }
        }

    }


    fun tantaProfileReg(
        nickname: String?,
        avatar: String?,
        uploadImagePath: String?,
        gender: Int?,
        key: String?,
        age: String?,
        inviteCode: String?,
        supportFragmentManager: FragmentManager
    ) {


        if (gender == null || gender == 0) {
            ToastUtils.showLong(R.string.tanta_login_gender_hint)
            return
        }

        if (DeviceManager.getInstance().channel != "308") {
            if (avatar.isNullOrEmpty() && uploadImagePath.isNullOrEmpty()) {
                ToastUtils.showLong(R.string.tanta_login_avatar_hint)
                return
            }


            if (gender == 1 && uploadImagePath.isNullOrEmpty()) {
                ToastUtils.showLong(R.string.tanta_login_avatar_hint)
                return
            }
        }

        if (nickname.isNullOrBlank()) {
            ToastUtils.showLong(R.string.tanta_login_nickname_hint)
            return
        }


        if (age.isNullOrEmpty()) {
            ToastUtils.showLong(R.string.tanta_login_age_hint)
            return
        }

        val message = MessageDialog()
        message.setContent("性别选择后，将无法修改，请确认你的性别")
        message.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {

                if (!uploadImagePath.isNullOrEmpty()) {
                    startUploadTask(uploadImagePath, nickname, gender, key, age, inviteCode)
                } else {
                    tantaProfileReg(
                        nickname,
                        avatar!!,
                        gender,
                        key,
                        age,
                        inviteCode
                    )
                }
                return false
            }
        })
        message.show(supportFragmentManager, "")


    }

    private fun startUploadTask(
        uploadImagePath: String,
        nickname: String,
        gender: Int,
        key: String?,
        age: String,
        inviteCode: String?
    ) {
        changeStateView(loading = true)
        launchIO {

            repository.tantaGetStsIndex("album")
                .catch {
                    changeStateView(hide = true)
                    it.printStackTrace()
                    toast(it.message ?: "")
                }
                .collect {
                    startUpload(
                        uploadImagePath,
                        nickname, gender, key, age, inviteCode, it.data
                    )
                }
        }
    }

    private fun startUpload(
        uploadImagePath: String,
        nickname: String,
        gender: Int,
        key: String?,
        age: String,
        inviteCode: String?,
        stsInfoBean: com.mshy.VInterestSpeed.common.bean.StsInfoBean
    ) {
        val result = stsInfoBean
        val endpoint = result.endpoint
        //该配置类如果不设置，会有默认配置，具体可看该类
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒

        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒

        conf.maxConcurrentRequest = 5 // 最大并发请求数，默认5个

        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次

        val credentialProvider: OSSCredentialProvider =
            OSSStsTokenCredentialProvider(
                result.accessKeyId,
                result.accessKeySecret,
                result.securityToken
            )
        val oss =
            OSSClient(BaseApplication.context, endpoint, credentialProvider, conf)

        val put = PutObjectRequest(
            result.bucketName,
            result.dir + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDayJpg(),
            uploadImagePath
        )

        oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest?, PutObjectResult?> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                val objectKey = request?.objectKey
                changeStateView(hide = true)
                if (!objectKey.isNullOrEmpty()) {
                    tantaProfileReg(nickname, objectKey, gender, key, age, inviteCode)
                } else {
                    toast("头像上传失败")
                }
            }

            override fun onFailure(
                request: PutObjectRequest?,
                clientException: ClientException?,
                serviceException: ServiceException?
            ) {
                toast("头像上传失败")
                changeStateView(hide = true)
            }
        })
    }


    private fun tantaProfileReg(
        nickname: String,
        avatar: String,
        gender: Int,
        key: String?,
        age: String,
        inviteCode: String?,
    ) {
        launchIO {

            changeStateView(loading = true)

            val params = mutableMapOf<String, Any>()
            params["nickname"] = nickname
            params["avatar"] = avatar
            params["gender"] = gender
            params["age"] = age

            if (!key.isNullOrEmpty()) {
                params["key"] = key
            }

            if (!inviteCode.isNullOrEmpty()) {
                params["invite_code"] = inviteCode
            }

            if (!bindData.isNullOrEmpty()) {
                params["bindData"] = bindData!!
            }
            if (!channelCode.isNullOrEmpty()) {
                params["channelCode"] = channelCode!!
            }


            repository.tantaProfileReg(params)
                .catch {
//                    ToastUtils.showLong(get)
                    changeStateView(hide = true)
                    it.printStackTrace()
                    toast(it.message ?: "")
                }
                .collect {
                    UserManager.userInfo = it.data.userinfo

                    launch(Dispatchers.Main) {
                        LoginUtils.checkLoginStatus(it.data.userinfo, true, mSetInfoIsOneKeyPass, finish = { isFinish ->
                            if (isFinish) {
                                changeStateView(hide = true)
                            }
                        })
//                        changeStateView(hide = true)
                    }
                }
        }
    }

    fun openInstall() {
        OpenInstall.getInstall(object : AppInstallAdapter() {
            override fun onInstall(appData: AppData) {
                //获取渠道数据
                channelCode = appData.getChannel()
                //获取自定义数据
                bindData = appData.getData()

//                opBindData.postValue(bindData)
            }
        })
    }

    fun tantaSystemIndex() {
        launchIO {
            repository.tantaSystemIndex()
                .catch {
                    jumpAble.postValue(null)
                }
                .collect {
                    jumpAble.postValue(it.data.oneKeyLoginKey)
                }
        }
    }

}