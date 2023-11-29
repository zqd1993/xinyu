package com.live.module.auth.vm

import android.app.Activity
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
import com.alipay.face.api.ZIMFacadeBuilder
import com.live.module.auth.bean.DescribeVerifyTokenBean
import com.live.module.auth.repo.AuthVquAvatarRepository
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/5/19
 * description:
 */
@HiltViewModel
class AuthVquAvatarViewModel @Inject constructor(private val repo: AuthVquAvatarRepository) :
    BaseViewModel() {

    val authResult = MutableLiveData<Boolean>()

    val verifyData = MutableLiveData<DescribeVerifyTokenBean?>()

    val verifyResultData = MutableLiveData<Int>()

    private var mVquAvatar: String = ""

    fun uploadTask(vquAvatar: String?, type: String) {
        mVquAvatar = ""
        if (vquAvatar.isNullOrEmpty()) {
            toast("请选择真人头像")
            return
        }

        changeStateView(loading = true)

        launchIO {

            repo.vquGetStsIndex(type)
                .catch {
                    changeStateView(hide = true)
                    it.printStackTrace()
                    toast(it.message ?: "")
                }
                .collect {
                    startUpload(vquAvatar, it.data)
                }
        }
    }

    private fun startUpload(vquAvatar: String, stsInfoBean: com.mshy.VInterestSpeed.common.bean.StsInfoBean) {
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
            vquAvatar
        )

        oss.asyncPutObject(put, object : OSSCompletedCallback<PutObjectRequest?, PutObjectResult?> {
            override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult?) {
                val objectKey = request?.objectKey
                if (!objectKey.isNullOrEmpty()) {
                    vquDescribeVerifyToken(objectKey)
                } else {
                    changeStateView(hide = true)
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

    private fun compareFace(avatar: String) {
        launchIO {
            repo.compareFace(avatar)
                .catch {
                    changeStateView(hide = true)
                    authResult.postValue(false)
                }
                .collect {
                    changeStateView(hide = true)
                    authResult.postValue(true)
                    UserManager.userInfo?.isRpAuth = 1
                }

        }
    }

    fun vquDescribeVerifyToken(objectKey: String) {
        mVquAvatar = objectKey

        if (mVquAvatar.isEmpty()) {
            toast("请选择真人头像")
            return
        }

        changeStateView(loading = true)
        launchIO {
            repo.vquRPAuth()
                .catch {
                    changeStateView(hide = true)
                    toast(it.message ?: "")
                }
                .collect {
                    changeStateView(hide = true)
                    verifyData.postValue(it.data)
                }
        }
    }

    fun rpVerify(
        activity: Activity,
        bean: DescribeVerifyTokenBean
    ) {
        ZIMFacadeBuilder.create(activity)
            .verify(bean.verifyToken, true, null) { response ->
                when (response?.code) {
                    1000 -> {   //认证通过
                        changeStateView(loading = true)
                        vquDescribeVerifyResult(bean.bizId, bean.verifyToken)
                    }
                    1003 -> { //认证中断
                        toast("未完成认证")
                    }
                    else -> {   //认证失败
                        toast("认证不通过")
                    }
                }
                true
            }
    }

    private fun vquDescribeVerifyResult(bizId: String, token: String) {
        changeStateView(loading = true)
        launchIO {
            repo.vquDescribeVerifyResult(bizId, token)
                .catch {
                    changeStateView(hide = true)
                    it.printStackTrace()
                    toast(it.message ?: "")
                }
                .collect {
                    if (it.data.auth == 1) {
                        compareFace(mVquAvatar)
                    } else {
                        changeStateView(hide = true)
                        authResult.postValue(false)
                    }
                }
        }
    }
}