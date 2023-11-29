package com.live.module.anchor.vm

import android.net.Uri
import android.util.Log
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
import com.live.module.anchor.bean.AnchorVquUploadBean
import com.live.module.anchor.net.AnchorVquApiService
import com.live.module.anchor.repo.AnchorVquNewHelloTemplateRepository
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/26
 * description:
 */
@HiltViewModel
class AnchorVquNewHelloTemplateViewModel @Inject constructor(private val repo: AnchorVquNewHelloTemplateRepository) :
    BaseViewModel() {


    private val GREET_IMG = "greet/img"
    private val GREET_VIDEO = "greet/video"
    private val GREET_MP3 = "greet/mp3"


    val saveSuccess = MutableLiveData<Boolean>()


    private var mVquUploadMap = mutableMapOf<String, String>()


    fun saveToSerice(
        title: String,
        length: Int,
        imagePath: String?,
        videoPath: String?,
        voicePath: String?,
    ) {
        mVquUploadMap.clear()

        if (imagePath.isNullOrEmpty()  && voicePath.isNullOrEmpty()) {

            vuqGreetAdd(title, length, mVquUploadMap)

        } else {
            changeStateView(loading = true)

            checkPathAndValue(imagePath, videoPath, voicePath) {
                if (it) {
                    vuqGreetAdd(title, length, mVquUploadMap)
                } else {
                    changeStateView(hide = true)
                }
            }
        }
    }


    private var currentIndex = 0

    private val waitUploadList = mutableListOf<AnchorVquUploadBean>()

    private fun checkPathAndValue(
        imagePath: String?,
        videoPath: String?,
        voicePath: String?,
        listener: (Boolean) -> Unit
    ) {
        waitUploadList.clear()

        if (!imagePath.isNullOrEmpty()) {
            val split = imagePath.split(".")
            val fileType = if (split.size >= 2) {
                split[split.size - 1]
            } else {
                "jpg"
            }
            waitUploadList.add(AnchorVquUploadBean(GREET_IMG, fileType, imagePath))
        }

//        if (!videoPath.isNullOrEmpty()) {
//            val split = videoPath.split(".")
//            val fileType = if (split.size >= 2) {
//                split[split.size - 1]
//            } else {
//                "mp4"
//            }
//            waitUploadList.add(AnchorVquUploadBean(GREET_VIDEO, fileType, videoPath))
//        }

        if (!voicePath.isNullOrEmpty()) {
            val split = voicePath.split(".")
            val fileType = if (split.size >= 2) {
                split[split.size - 1]
            } else {
                "mp3"
            }
            waitUploadList.add(AnchorVquUploadBean(GREET_MP3, fileType, voicePath))
        }

        currentIndex = 0
//        count = waitUploadList.size

        startUpload(waitUploadList[currentIndex], listener)


//
//        launchIO {
//            val uploadPaths = mutableMapOf<String, String>()
//            changeStateView(loading = true)
//
//            /**
//             * 上传图片
//             * 判断图片链接是否为空，如果不为空，则表示有图片
//             * 则上传图片
//             */
//            if (!imagePath.isNullOrEmpty()) {
//                val split = imagePath.split(".")
//                val fileType = if (split.size >= 2) {
//                    split[split.size - 1]
//                } else {
//                    "jpg"
//                }
//
//                startUpload(imagePath, GREET_IMG, fileType, listener = { objectKey, isSuccess ->
//                    imgFinish = true
//                    imgSuccess = isSuccess
//
//                    if (isSuccess) {
//                        Log.d("AnchorUploadFile", "uploadFile img:  $objectKey")
//                        uploadPaths["file"] = objectKey
//                    }
//                })
//            }
//
//            /**
//             * 上传视频
//             * 判断视频链接是否为空，如果不为空，则表示有视频
//             * 则上传视频
//             */
//            if (!videoPath.isNullOrEmpty()) {
//                val split = videoPath.split(".")
//                val fileType = if (split.size >= 2) {
//                    split[split.size - 1]
//                } else {
//                    "mp4"
//                }
//                startUpload(
//                    videoPath,
//                    GREET_VIDEO,
//                    fileType,
//                    listener = { objectKey, isSuccess ->
//                        Log.d("AnchorUploadFile", "uploadFile video:  $objectKey")
//                        videoFinish = true
//                        videoSuccess = isSuccess
//                        if (isSuccess) {
//                            uploadPaths["video_file"] = objectKey
//                        }
//                    })
//            }
//
//            /**
//             * 上传音频
//             * 判断音频链接是否为空，如果不为空，则表示有音频
//             * 则上传音频
//             */
//            if (!voicePath.isNullOrEmpty()) {
//                val split = voicePath.split(".")
//                val fileType = if (split.size >= 2) {
//                    split[split.size - 1]
//                } else {
//                    "mp3"
//                }
//                startUpload(voicePath, GREET_MP3, fileType, listener = { objectKey, isSuccess ->
//                    Log.d("AnchorUploadFile", "uploadFile voice:  $objectKey")
//                    voiceFinish = true
//                    voiceSuccess = isSuccess
//                    if (isSuccess) {
//                        uploadPaths["voice_file"] = objectKey
//                    }
//                })
//            }
//        }
    }

    private fun startUpload(bean: AnchorVquUploadBean, listener: (Boolean) -> Unit) {
        launchIO {
            repo.getStsIndex(bean.type)
                .catch {
                    changeStateView(hide = true)
                    toast(it.message ?: "")
                }
                .collect {
                    uploadToOSS(it.data, bean, listener)
                }
        }
    }

    /**
     * 具体参数，请查看[AnchorVquApiService.vquGreetAdd]
     */
    fun vuqGreetAdd(title: String, length: Int, fileParams: MutableMap<String, String>?) {
        val params = mutableMapOf<String, Any>()
        params["title"] = title
        params["length"] = length
        params["is_multi"] = 1

        if (!fileParams.isNullOrEmpty()) {
            params.putAll(fileParams)
        }
        launchIO {
            repo.vquGreetAdd(params)
                .catch {
                    Log.d("AnchorUploadFile", "uploadFile catch")
                    it.printStackTrace()
                    changeStateView(hide = true)
                    toast(it.message ?: "")
                }
                .onStart {
                    Log.d("AnchorUploadFile", "uploadFile onStart")
                    changeStateView(loading = true)
                }
                .onCompletion {
                    Log.d("AnchorUploadFile", "uploadFile onCompletion")
                    changeStateView(hide = true)
                }
                .collect {
                    Log.d("AnchorUploadFile", "uploadFile success")
                    toast("新建模板成功")
                    saveSuccess.postValue(true)
                }
        }
    }

    private fun uploadToOSS(
        stsInfoBean: com.mshy.VInterestSpeed.common.bean.StsInfoBean,
        bean: AnchorVquUploadBean,
        listener: (Boolean) -> Unit
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

        val put =
            if (bean.type == GREET_MP3) {
                PutObjectRequest(
                    result.bucketName,
                    result.dir + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDay(bean.fileType),
                    Uri.parse("file://${bean.path}")
                )
            } else {
                PutObjectRequest(
                    result.bucketName,
                    result.dir + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDay(bean.fileType),
                    bean.path
                )
            }

        // 构造上传请求
        oss.asyncPutObject(
            put,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult?> {
                override fun onSuccess(
                    request: PutObjectRequest,
                    result: PutObjectResult?
                ) {

                    when (bean.type) {
                        GREET_IMG -> mVquUploadMap["file"] = request.objectKey
//                        GREET_VIDEO -> mVquUploadMap["video_file"] = request.objectKey
                        GREET_MP3 -> mVquUploadMap["voice_file"] = request.objectKey
                    }

                    currentIndex++
                    if (currentIndex >= waitUploadList.size) {
                        listener.invoke(true)
                    } else {
                        startUpload(waitUploadList[currentIndex], listener)
                    }
                }

                override fun onFailure(
                    request: PutObjectRequest,
                    clientExcepion: ClientException,
                    serviceException: ServiceException
                ) {
                    changeStateView(hide = true)
                    toast("上传文件失败")
                    listener.invoke(false)
                }
            })
    }
}