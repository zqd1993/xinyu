package com.live.module.agora.repo

import com.live.module.agora.bean.AgoraVquChatShootBean
import com.live.module.agora.bean.AgoraVquMatchRecordBean
import com.live.module.agora.net.AgoraVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/7/19
 * description:
 */
class AgoraVquVideoRepository @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var vquAgoraVideoApi: AgoraVquApiService

    @Inject
    lateinit var vquAdvertVideoApi: GlobalApiService

    suspend fun vquAddMatching(vquType: String) = request<BaseResponse<AgoraVquMatchRecordBean>> {
        vquAgoraVideoApi.vquAddMatching(vquType).run {
            try {
                emit(this)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    suspend fun vquPostChatShootFrom(vquRoom_id: String, from: String, uid: String) =
        request<BaseResponse<AgoraVquChatShootBean>> {
            vquAgoraVideoApi.vquPostChatShootFrom(vquRoom_id, from, uid).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}