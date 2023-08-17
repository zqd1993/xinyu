package com.live.module.info.vm

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
import com.live.module.info.bean.InfoUserTagBean
import com.live.module.info.bean.InfoVquSelectDataBean
import com.live.module.info.bean.InfoVquUserBean
import com.live.module.info.repo.InfoEditActivityRepo
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import com.mshy.VInterestSpeed.common.bean.VquInfoAddressBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class InfoEditViewModel @Inject constructor(private val mVquRepoEdit: InfoEditActivityRepo) :
    BaseViewModel() {
    val vquUserInfoData = MutableLiveData<BaseResponse<InfoVquUserBean>>()
    val vquGetSelectData = MutableLiveData<BaseResponse<InfoVquSelectDataBean>>()
    val vquGetLabelData = MutableLiveData<BaseResponse<InfoUserTagBean>>()
    val vquGetCityData = MutableLiveData<BaseResponse<MutableList<VquInfoAddressBean>>>()
    val vquSaveUserData = MutableLiveData<BaseResponse<Any>>()
    val vquCompareFacesAvatarData = MutableLiveData<BaseResponse<String>>()
    val vquVideoUrlData = MutableLiveData<String>()
    val vquUpLoadErrData = MutableLiveData<String>()
    open val vquUrlData = MutableLiveData<String>()
    open val vquAvatarUrlData = MutableLiveData<String>()
    val vquAuthData = MutableLiveData<BaseResponse<CommonAuthBean>>()
    fun vquGetUserInfo() {
        launchIO {
            val params = hashMapOf<String, Any>()
            var userId = UserManager.userInfo?.userId
            params["user_id"] = userId ?: ""
            mVquRepoEdit.vquGetUserInfo(params)
                .catch { }
                .collect {
                    vquUserInfoData.postValue(it)
                }
        }
    }

    fun vquGetSelectData() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepoEdit.vquGetSelectData(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquGetSelectData.postValue(it)
                }
        }
    }

    fun vquGetUserTag() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepoEdit.vquGetUserTag(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquGetLabelData.postValue(it)
                }
        }
    }

    fun vquGetProvinceList() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepoEdit.vquGetProvinceList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquGetCityData.postValue(it)
                }
        }
    }

    fun vquCompareFacesAvatar(avatar: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["avatar"] = avatar
            mVquRepoEdit.vquCompareFacesAvatar(params)
                .catch {}
                .collect {
                    vquCompareFacesAvatarData.postValue(it)
                }
        }
    }

    fun vquSaveUserInfo(
        birthday: String,
        height: String,
        weight: String,
        cityId: String,
        occupation: String,
        annual_income: String,
        education: String,
        is_marriage: String,
        label: String,
        avatar: String,
        startAvatar: String,
        nickname: String,
        startName: String,
        albums: String,
        startAlbums: String,
        signature: String,
        startSignature: String,
        voice: String,
        startVoice: String,
        voice_time: Int,
        video: String,
        startVideo: String,
    ) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["user_id"] = UserManager.userInfo!!.userId
            params["age"] = birthday
            params["height"] = height
            params["weight"] = weight
            params["cityId"] = cityId
            params["occupation"] = occupation
            params["annual_income"] = annual_income
            params["education"] = education
            params["is_marriage"] = is_marriage
            params["label"] = label
            if (!avatar.isNullOrEmpty() && avatar != startAvatar) {
                params["avatar"] = avatar
            }
            if (nickname != startName) {
                params["nickname"] = nickname
            }
            if (albums != startAlbums) {
                params["albums"] = albums
            }
            if (signature != startSignature) {
                params["signature"] = signature
            }
            if (voice != startVoice) {
                params["voice"] = voice
                params["voice_time"] = voice_time
            }
//            if (video != startVideo) {
//                params["video"] = video
//            }
            mVquRepoEdit.vquSaveUserInfo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSaveUserData.postValue(it)
                }
        }
    }

    open fun vquUploadImg(fileList: MutableList<LocalMedia?>, type: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type

            mVquRepoEdit.vquGetSts(params)
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
                            if (PictureMimeType.isHasHttp(localMedia?.path)) {
                                var url =
                                    localMedia?.path?.replace(NetBaseUrlConstant.IMAGE_URL, "")
                                urls?.add(url!!)
                            } else {
                                val realPath = if (localMedia?.compressPath.isNullOrEmpty()) {
                                    localMedia?.realPath
                                } else {
                                    localMedia?.compressPath
                                }
                                val put = PutObjectRequest(
                                    it.data.bucketName,
                                    it.data.dir.toString() + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDayJpg(),
                                    realPath
                                )

                                oss.asyncPutObject(put,
                                    object :
                                        OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                                        override fun onSuccess(
                                            request: PutObjectRequest?,
                                            result: PutObjectResult?,
                                        ) {
                                            urls?.add(request?.objectKey!!)
                                            if (urls?.size == fileList.size) {
                                                vquUrlData.postValue(
                                                    urls.toList().joinToString(",")
                                                )
                                            }
                                        }

                                        override fun onFailure(
                                            request: PutObjectRequest?,
                                            clientException: ClientException?,
                                            serviceException: ServiceException?,
                                        ) {
                                            vquUpLoadErrData.postValue("上传失败")
                                        }

                                    })
                            }

                        }
                    }
                }
        }
    }

    open fun vquUploadAvatar(localMedia: LocalMedia?) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = "avatar"

            mVquRepoEdit.vquGetSts(params)
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
                        val realPath = localMedia?.path
//                            FilePathUtils.getFileAbsolutePath(
//                                BaseApplication.context,
//                                Uri.parse(localMedia?.path)
//                            )
                        val put = PutObjectRequest(
                            it.data.bucketName,
                            it.data.dir.toString() + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDayJpg(),
                            localMedia?.cutPath
                        )

                        oss.asyncPutObject(put,
                            object :
                                OSSCompletedCallback<PutObjectRequest, PutObjectResult> {
                                override fun onSuccess(
                                    request: PutObjectRequest?,
                                    result: PutObjectResult?,
                                ) {
                                    vquAvatarUrlData.postValue(
                                        request?.objectKey!!
                                    )
                                }

                                override fun onFailure(
                                    request: PutObjectRequest?,
                                    clientException: ClientException?,
                                    serviceException: ServiceException?,
                                ) {
                                    vquUpLoadErrData.postValue("上传失败")
                                }

                            })

                    }
                }
        }
    }

    open fun vquUploadVideo(videoPath: String, type: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type

            mVquRepoEdit.vquGetSts(params)
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
                            it.data!!.dir + com.mshy.VInterestSpeed.common.utils.TimeZoneUtils.getCurrentYearMonthDay("mp4"),
                            Uri.parse("file://$videoPath")
                        )
                        oss!!.asyncPutObject(
                            put,
                            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult?> {
                                override fun onSuccess(
                                    request: PutObjectRequest,
                                    result: PutObjectResult?,
                                ) {
                                    vquVideoUrlData.postValue(request.objectKey)
                                }

                                override fun onFailure(
                                    request: PutObjectRequest,
                                    clientExcepion: ClientException,
                                    serviceException: ServiceException,
                                ) {
                                    vquUpLoadErrData.postValue("上传失败")
                                }
                            })
                    }
                }
        }
    }

    fun vquIsAuth() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepoEdit.vquIsAuth(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquAuthData.postValue(it)
                }
        }
    }
}