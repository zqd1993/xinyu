package com.live.module.message.repo

import com.live.module.message.net.MessageVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquMainBannerBean
import com.mshy.VInterestSpeed.common.bean.MessageVisitorBean
import com.mshy.VInterestSpeed.common.bean.NotifyMsgBean
import com.mshy.VInterestSpeed.common.bean.VquRelationListBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.uikit.bean.ChatIntimateBean
import javax.inject.Inject

/**
 * FileName: com.live.module.message.repo
 * Date: 2022/4/14 15:10
 * Description:
 * History:
 */
class MessageVquRecentFragmentRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: GlobalApiService

    @Inject
    lateinit var mApiMsg: MessageVquApiService


    /**
     * 获取banner数据
     */
    suspend fun vquGetBanner() = request<BaseResponse<CommonVquMainBannerBean>> {
        mApi.vquGetMainBanner("5").run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquDelBeckons(uids:String) = request<BaseResponse<Any>> {
        mApiMsg.vquDelBeckons(uids).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun getWhoLookMeInfo() = request<BaseResponse<MessageVisitorBean>>(requestBlock = {
        mApi.getWhoLookMeInfo().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    })

    suspend fun vquQueryIntimateList() =
        request<BaseResponse<ChatIntimateBean>> {
            mApi.vquQueryIntimateList().run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }

    suspend fun vquSaveRemarkName(userId: String, remarkName: String) = request<BaseResponse<Any>> {
        mApiMsg.saveRemark(userId, remarkName).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquVisitorList(page: Int) = request<BaseResponse<VquRelationListBean>> {
        mApiMsg.vquVisitorList(page).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun getNotifyMsg() = request<BaseResponse<NotifyMsgBean>>(requestBlock = {
        mApi.getNotifyMsg().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    })
}