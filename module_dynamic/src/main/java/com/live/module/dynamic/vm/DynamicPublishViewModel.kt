package com.live.module.dynamic.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.dynamic.bean.*
import com.mshy.VInterestSpeed.common.bean.DynamicVquReportBean
import com.live.module.dynamic.repo.DynamicVquPublishActivityRepo
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import com.mshy.VInterestSpeed.common.bean.StsInfoBean
import com.mshy.VInterestSpeed.common.constant.LIMIT
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.TantaCitySelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class DynamicPublishViewModel @Inject constructor(private val mVquRepo: DynamicVquPublishActivityRepo) :
    BaseViewModel() {
    val dynamicLsit = MutableLiveData<BaseResponse<DynamicVquBean>>()
    val nyDynamicLsit = MutableLiveData<BaseResponse<DynamicVquBean>>()
    val dynamicLikeLsit = MutableLiveData<BaseResponse<DynamicLikesBean>>()
    val dynamicLikeCount = MutableLiveData<BaseResponse<Int>>()
    val dynamicDetail = MutableLiveData<BaseResponse<DynamicVquDetailBean>>()
    val dynamicDetailCommentListData = MutableLiveData<BaseResponse<DynamicTantaCommentsBean>>()
    val dynamicCommentData = MutableLiveData<BaseResponse<Any>>()
    val dynamicDelCommentData = MutableLiveData<BaseResponse<Any>>()
    val dynamicReportCommentData = MutableLiveData<BaseResponse<Any>>()
    val dynamicLike = MutableLiveData<BaseResponse<Any>>()
    val vquUploadVideoData = MutableLiveData<BaseResponse<DynamicPostBean>>()
    val vquUploadImageData = MutableLiveData<BaseResponse<DynamicPostBean>>()
    val vquUploadImageUrlData = MutableLiveData<BaseResponse<Any>>()
    val vquStsData = MutableLiveData<BaseResponse<StsInfoBean>>()
    val vquReportCateData = MutableLiveData<BaseResponse<MutableList<DynamicVquReportBean>>>()
    val vquDelDynamicData = MutableLiveData<BaseResponse<Any>>()
    val vquDelTextOrImgData = MutableLiveData<BaseResponse<Any>>()
    val vquAuthData = MutableLiveData<BaseResponse<CommonAuthBean>>()
    val vquChatAuthData = MutableLiveData<BaseResponse<CommonAuthBean>>()
    val vquChatAuthDetailData = MutableLiveData<BaseResponse<CommonAuthBean>>()
    val vquSendData = MutableLiveData<BaseResponse<Any>>()
    val vquSendBeckoningData = MutableLiveData<BaseResponse<Any>>()
    fun vquDynamicList(type: Int, page: Int) {
        launchIO {
            var user = UserManager.userInfo
            val params = hashMapOf<String, Any>()
            params["type"] = type
            params["page"] = page
            params["limit"] = LIMIT
            params["city"] = TantaCitySelector.getCityId(user?.city ?: "") ?: "110100"
            params["gender"] = user!!.gender

            mVquRepo.vquDynamicList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicLsit.postValue(it)
                }
        }
    }

    fun vquUserDynamicList(userId: Int, page: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["page"] = page
            params["limit"] = LIMIT
            params["user_id"] = userId

            mVquRepo.vquUserDynamicList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    nyDynamicLsit.postValue(it)
                }
        }
    }

    fun vquDynamicLike(type: Int, id: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type
            params["dynamic_id"] = id

            mVquRepo.vquDynamicLike(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicLike.postValue(it)
                }
        }
    }

    fun vquPublishVideo(content: String, type: Int, videoFileName: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["content"] = content
            params["type"] = type
            params["videoFileName"] = videoFileName

            mVquRepo.vquPublishVideo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquUploadVideoData.postValue(it)
                }
        }
    }

    fun vquPublishImg(content: String, type: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["content"] = content

            params["type"] = type

            mVquRepo.vquPublishVideo(params)
                .catch {
                    toast(it.message ?: "")
                    changeStateView(hide = true)
                }
                .collect {
                    vquUploadImageData.postValue(it)
                }
        }
    }

    fun vquPublishImgUrl(dynamicId: String, img: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["dynamic_id"] = dynamicId
            params["images"] = img

            mVquRepo.vquPublishImg(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquUploadImageUrlData.postValue(it)
                }
        }
    }

    fun vquGetSts() {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = "dynamic"

            mVquRepo.vquGetSts(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquStsData.postValue(it)
                }
        }
    }

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

    fun vquDeleteDynamic(id: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["dynamic_id"] = id
            mVquRepo.vquDeleteDynamic(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquDelDynamicData.postValue(it)
                }
        }
    }

    fun vquDeleteDynamicTextOrImg(type: Int, id: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["type"] = type
            params["info_id"] = id
            mVquRepo.vquDeleteDynamicTextOrImg(params)
                .catch { toast(it.message ?: "") }
                .onStart { changeStateView(loading = true) }
                .onCompletion {
                    changeStateView(hide = true)
                }
                .collect {
                    vquDelTextOrImgData.postValue(it)
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

    fun vquAuth() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquIsAuth(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquChatAuthData.postValue(it)
                }
        }
    }
    fun vquAuthByDetail() {
        launchIO {
            val params = hashMapOf<String, Any>()
            mVquRepo.vquIsAuth(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquChatAuthDetailData.postValue(it)
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
    fun vquSendBeckonByDetail(userId: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["user_ids"] = userId
            mVquRepo.vquSendBeckon(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSendBeckoningData.postValue(it)
                }
        }
    }

    fun vquDynamicLikeList(page: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["page"] = page
            params["limit"] = LIMIT

            mVquRepo.vquDynamicLikeList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicLikeLsit.postValue(it)
                }
        }
    }

    fun vquDynamicLikeCount() {
        launchIO {
            val params = hashMapOf<String, Any>()

            mVquRepo.vquDynamicLikeCount(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicLikeCount.postValue(it)
                }
        }
    }

    fun vquDynamicInfo(dynamicId: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params.put("dynamic_id", dynamicId)
            mVquRepo.vquDynamicInfo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicDetail.postValue(it)
                }
        }
    }

    fun vquGetCommentList(dynamicId: String, page: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params.put("dynamic_id", dynamicId)
            params.put("page", page)
            params.put("limit", LIMIT)
            mVquRepo.vquGetCommentList(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicDetailCommentListData.postValue(it)
                }
        }
    }

    fun vquComment(dynamicId: String, content: String, commentId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params.put("dynamic_id", dynamicId)
            if (commentId > 0) {
                params.put("comment_id", commentId)
            }
            params.put("content", content)
            mVquRepo.vquComment(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicCommentData.postValue(it)
                }
        }
    }

    fun vquDeleteComment(dynamicId: String, commentId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params.put("dynamic_id", dynamicId)
            params.put("comment_id", commentId)
            mVquRepo.vquDeleteComment(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicDelCommentData.postValue(it)
                }
        }
    }
    fun vquReportComment(commentId: Int) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params.put("report_uid", commentId)
            params.put("cate_id", 14)
            params.put("type", 6)
            mVquRepo.vquReportComment(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    dynamicReportCommentData.postValue(it)
                }
        }
    }
}