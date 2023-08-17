package com.live.module.message.repo

import com.live.module.message.net.MessageVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean
import javax.inject.Inject

/**
 * FileName: com.live.module.message.repo
 * Date: 2022/5/5 16:36
 * Description:
 * History:
 */
class MessageVquCommonWordRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: MessageVquApiService

    suspend fun vquGetCommonWord() = request<BaseResponse<List<NIMCommonWordBean>>> {
        mApi.vquSuspendGetCommonWord().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquDelCommonWord(id: Int) =
        request<BaseResponse<Any>> {
            mApi.vquDeleteCommonWord(id).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }

        }


    suspend fun vquAddCommonWord(word: String) =
        request<BaseResponse<Any>> {
            mApi.vquAddCommonWord(word).run {
                responseCodeExceptionHandler(code, message) {
                    emit(this)
                }
            }
        }
}