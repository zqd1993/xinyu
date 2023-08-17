package com.live.module.dynamic.repo

import com.live.module.dynamic.bean.*
import com.live.module.dynamic.net.DynamicVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import com.mshy.VInterestSpeed.common.bean.DynamicVquReportBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 *
 *
 */
class DynamicVquPublishActivityRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mVquDynamicApi: DynamicVquApiService


    suspend fun vquDynamicList(params: HashMap<String, Any>) =
        request<BaseResponse<DynamicVquBean>> {
            mVquDynamicApi.vquDynamicList(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquUserDynamicList(params: HashMap<String, Any>) =
        request<BaseResponse<DynamicVquBean>> {
            mVquDynamicApi.vquUserDynamicList(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquDynamicLike(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquDynamicLike(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquPublishVideo(params: HashMap<String, Any>) =
        request<BaseResponse<DynamicPostBean>> {
            mVquDynamicApi.vquPublishVideo(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquPublishImg(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquPublishImg(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetSts(params: HashMap<String, Any>) = request<BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>> {
        mVquDynamicApi.vquGetSts(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquReportCate(params: HashMap<String, Any>) =
        request<BaseResponse<MutableList<DynamicVquReportBean>>> {
            mVquDynamicApi.vquReportCate(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquReport(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquReport(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquDeleteDynamic(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquDeleteDynamic(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquDeleteDynamicTextOrImg(params: HashMap<String, Any>) =
        request<BaseResponse<Any>> {
            mVquDynamicApi.vquDeleteDynamicTextOrImg(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquIsAuth(params: HashMap<String, Any>) = request<BaseResponse<CommonAuthBean>> {
        mVquDynamicApi.vquIsAuth(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquSendBeckon(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquSendBeckon(params).run {
            emit(this)
        }
    }

    suspend fun vquDynamicLikeList(params: HashMap<String, Any>) =
        request<BaseResponse<DynamicLikesBean>> {
            mVquDynamicApi.vquDynamicLikeList(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquDynamicLikeCount(params: HashMap<String, Any>) = request<BaseResponse<Int>> {
        mVquDynamicApi.vquDynamicLikeCount(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquGetCommentList(params: HashMap<String, Any>) =
        request<BaseResponse<DynamicTantaCommentsBean>> {
            mVquDynamicApi.vquGetCommentList(params).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquDynamicInfo(params: HashMap<String, Any>) =
        request<BaseResponse<DynamicVquDetailBean>> {
            mVquDynamicApi.vquDynamicInfo(params).run {
                emit(this)
            }
        }

    suspend fun vquComment(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquComment(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquDeleteComment(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquDeleteComment(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquReportComment(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mVquDynamicApi.vquReportComment(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}