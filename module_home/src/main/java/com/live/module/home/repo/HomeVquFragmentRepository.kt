package com.quyunshuo.module.home.fragment

import com.live.module.home.bean.HomeNewDataBean
import com.live.module.home.bean.HomeVquBeckonBean
import com.live.module.home.bean.HomeVquChannelAnchorBean
import com.live.module.home.net.HomeVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.VquUserHomeBean
import com.mshy.VInterestSpeed.common.helper.responseCodeExceptionHandler
import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean
import javax.inject.Inject

/**
 * @author DBoy 2021/7/6 <p>
 * - 文件描述 :
 */
class HomeVquFragmentRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var vquHomeService: HomeVquApiService

    suspend fun vquGetAdvert(vquType:String = "2") = request<BaseResponse<CommonVquAdBean>> {
        vquHomeService.vquGetAdvert(vquType).run {
            responseCodeExceptionHandler(code, message, {
                emit(this)
            })
        }
    }

    suspend fun vquGetRecommendAnchors(vquPage:String = "1") = request<BaseResponse<HomeVquChannelAnchorBean>> {
        vquHomeService.vquGetRecommendAnchors(vquPage,50).run {
            responseCodeExceptionHandler(code, message, {
                emit(this)
            })
        }
    }
    suspend fun vquGetNewRecommendAnchors(vquPage:String = "1") = request<BaseResponse<HomeNewDataBean>> {
        vquHomeService.vquGetNewRecommendAnchors(vquPage,50).run {
            responseCodeExceptionHandler(code, message, {
                emit(this)
            })
        }
    }

    suspend fun vquGetOnLineAnchors(vquPage:String = "1") = request<BaseResponse<HomeNewDataBean>> {
        vquHomeService.vquGetOnLineAnchors(vquPage,50).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }
    suspend fun vquGetOnlineAnchors(vquPage:String = "1") = request<BaseResponse<HomeVquChannelAnchorBean>> {
        vquHomeService.vquGetOnlineAnchors(vquPage).run {
            responseCodeExceptionHandler(code, message, {
                emit(this)
            })
        }
    }

    suspend fun vquSendBeckon(vquUserIds:String = "",isContinue:Int=0,isTip:Int=0) = request<BaseResponse<HomeVquBeckonBean>> {
        vquHomeService.vquSendBeckon(vquUserIds,isContinue,isTip).run {
            emit(this)
        }
    }


    suspend fun vquGetUserInfo(params: HashMap<String, Any>) = request<BaseResponse<VquUserHomeBean>> {
        vquHomeService.vquGetUserInfo(params).run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquCloseInfoFinishTip() = request<BaseResponse<Any>> {
        vquHomeService.vquCloseInfoFinishTip().run {
            responseCodeExceptionHandler(code, message) {
                emit(this)
            }
        }
    }

    suspend fun vquAddMatching(vquType:String) = request<BaseResponse<Any>> {
        vquHomeService.vquAddMatching(vquType).run {
            try {
                emit(this)
            }catch (ex:Exception){
                ex.printStackTrace()
            }
        }
    }

}