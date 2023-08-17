package com.mshy.VInterestSpeed.common.ui.vm

import android.net.Uri
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
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.DynamicVquReportBean
import com.mshy.VInterestSpeed.common.bean.InfoVoiceListBean
import com.mshy.VInterestSpeed.common.ui.repo.UploadRepository
import com.luck.picture.lib.entity.LocalMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.io.File
import javax.inject.Inject


@HiltViewModel
open class UploadViewModel @Inject constructor(private val mVquRepo: UploadRepository) :
    BaseViewModel() {
    open val vquUrlData = MutableLiveData<String>()
    open val vquVoiceUrlData = MutableLiveData<String>()
    open val vquUpLoadErrData = MutableLiveData<Any>()
    val vquReportCateData = MutableLiveData<BaseResponse<MutableList<DynamicVquReportBean>>>()
    val vquReportData = MutableLiveData<BaseResponse<Any>>()
    val vquVoiceTextListData = MutableLiveData<BaseResponse<InfoVoiceListBean>>()

    open fun vquUploadImg(fileList: MutableList<LocalMedia?>, type: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type

            mVquRepo.vquGetSts(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    var result = it.data
                    val endpoint = "http://oss-cn-hangzhou.aliyuncs.com"
                    //该配置类如果不设置，会有默认配置，具体可看该类
                    //该配置类如果不设置，会有默认配置，具体可看该类
                    val conf = ClientConfiguration()
                    conf.setConnectionTimeout(15 * 1000) // 连接超时，默认15秒

                    conf.setSocketTimeout(15 * 1000) // socket超时，默认15秒

                    conf.setMaxConcurrentRequest(5) // 最大并发请求数，默认5个

                    conf.setMaxErrorRetry(2) // 失败后最大重试次数，默认2次


                    val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
                        result.getAccessKeyId(),
                        result.getAccessKeySecret(),
                        result.getSecurityToken()
                    )
                    var oss = OSSClient(BaseApplication.context, endpoint, credentialProvider, conf)
                    var urls: MutableList<String>? = mutableListOf<String>()
                    async {
                        fileList.map { localMedia ->
                            val realPath =localMedia?.realPath
//                                FilePathUtils.getFileAbsolutePath(
//                                    BaseApplication.context,
//                                    Uri.parse(localMedia?.path)
//                                )
                            val put = PutObjectRequest(
                                it.data.bucketName,
                                it.data.dir.toString() + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDayJpg(),
                                realPath
                            )

                            oss.asyncPutObject(put,
                                object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                                    override fun onSuccess(
                                        request: PutObjectRequest?,
                                        result: PutObjectResult?
                                    ) {
                                        urls?.add(request?.objectKey!!)
                                        if (urls?.size == fileList.size) {
                                            vquUrlData.postValue(urls.toList().joinToString(","))
                                        }
                                    }

                                    override fun onFailure(
                                        request: PutObjectRequest?,
                                        clientException: ClientException?,
                                        serviceException: ServiceException?
                                    ) {
                                        vquUpLoadErrData.postValue("上传失败")
                                    }

                                })
                        }
                    }
                }
        }
    }

    open fun vquUploadVoice(audioFile: File, type: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type

            mVquRepo.vquGetSts(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    var result = it.data
                    val endpoint = "http://oss-cn-hangzhou.aliyuncs.com"
                    //该配置类如果不设置，会有默认配置，具体可看该类
                    //该配置类如果不设置，会有默认配置，具体可看该类
                    val conf = ClientConfiguration()
                    conf.setConnectionTimeout(15 * 1000) // 连接超时，默认15秒

                    conf.setSocketTimeout(15 * 1000) // socket超时，默认15秒

                    conf.setMaxConcurrentRequest(5) // 最大并发请求数，默认5个

                    conf.setMaxErrorRetry(2) // 失败后最大重试次数，默认2次


                    val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
                        result.getAccessKeyId(),
                        result.getAccessKeySecret(),
                        result.getSecurityToken()
                    )
                    var oss = OSSClient(BaseApplication.context, endpoint, credentialProvider, conf)
                    async {
                        // 构造上传请求
                        val put = PutObjectRequest(
                            it.data.bucketName,
                            it.data!!.dir + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDay("mp3"),
                            Uri.parse("file://" + audioFile?.absolutePath)
                        )
                        oss!!.asyncPutObject(
                            put,
                            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult?> {
                                override fun onSuccess(
                                    request: PutObjectRequest,
                                    result: PutObjectResult?
                                ) {
                                    vquVoiceUrlData.postValue(request.objectKey)
                                }

                                override fun onFailure(
                                    request: PutObjectRequest,
                                    clientExcepion: ClientException,
                                    serviceException: ServiceException
                                ) {
                                    vquUpLoadErrData.postValue("上传失败")
                                }
                            })
                    }
                }
        }
    }

    /**
     * --------------------------------举报相关------------------------------------------------------------------------
     */
    fun vquReportCate() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquReportCate(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquReportCateData.postValue(it)
                }
        }
    }

    fun vquReport(type: Int, cateId: Int, uid: String, content: String?, images: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type
            params["cate_id"] = cateId
            params["report_uid"] = uid
            params["content"] = content ?: ""
            if (!images.isNullOrEmpty()) {
                params["images"] = images
            }
            mVquRepo.vquReport(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquReportData.postValue(it)
                }
        }
    }
    /**
     * --------------------------------声音展示相关------------------------------------------------------------------------
     */
    fun vquGetVoiceTextList() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquGetVoiceTextList(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquVoiceTextListData.postValue(it)
                }
        }
    }

}