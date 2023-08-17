package com.live.module.message.repo

import com.live.module.message.bean.CallRecordData
import com.live.module.message.net.MessageVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import javax.inject.Inject

/**
 * FileName: com.live.module.message.repo
 * Date: 2022/5/20 16:32
 * Description:
 * History:
 */
class MessageVquCallRecordRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mApi: MessageVquApiService

    suspend fun vquGetCallRecords(page: Int) = request<BaseResponse<CallRecordData>> {
        mApi.vquGetCallRecords(page).run {
            responseCodeExceptionHandler(code,message){
                emit(this)
            }
        }
    }

    suspend fun vquDeleteCallRecords(id:Int) = request<BaseResponse<Any>> {
        mApi.vquDeleteCallRecords(id,0).run {
            responseCodeExceptionHandler(code,message){
                emit(this)
            }
        }
    }

}