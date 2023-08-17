package com.live.module.agora.repo

import com.live.module.agora.bean.AgoraVquMatchRecordBean
import com.live.module.agora.net.AgoraVquApiService

import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:32
 *
 */
class AgoraVquMatchActivityRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var vquAgoraVideoApi: AgoraVquApiService

    @Inject
    lateinit var vquAdvertVideoApi: GlobalApiService


    suspend fun vquAddMatching(vquType:String) = request<BaseResponse<AgoraVquMatchRecordBean>> {
        vquAgoraVideoApi.vquAddMatching(vquType).run {
             try {
                emit(this)
             }catch (ex:Exception){
                 ex.printStackTrace()
             }
        }
    }

    suspend fun vquCancelMatching(type: String) = request<BaseResponse<Any>> {
        vquAgoraVideoApi.vquCancelMatching(type).run {
            try {
                emit(this)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }



    suspend fun vquReceiveCall(type :String,match_id:String) = request<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>> {
        vquAgoraVideoApi.vquReceiveCall(match_id, type).run {
            try {
                emit(this)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }
}