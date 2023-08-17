package com.live.module.message.repo

import com.live.module.message.bean.ChatSettingBean
import com.live.module.message.net.MessageVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * FileName: com.live.module.message.repo
 * Date: 2022/4/14 15:10
 * Description:
 * History:
 */
class MessageVquChatSettingRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: MessageVquApiService

    suspend fun vquGetChatSetting(userId: String) = request<BaseResponse<ChatSettingBean>> {
        mApi.getChatInfo(userId).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquSaveRemarkName(userId: String, remarkName: String) = request<BaseResponse<Any>> {
        mApi.saveRemark(userId, remarkName).run {
            responseCodeExceptionHandler(code,message) {
                emit(this)
            }
        }
    }

    suspend fun vquDoBlack(params: HashMap<String, Any>) = request<BaseResponse<Any>> {
        mApi.vquDoBlack(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
}