package com.live.module.home.net

import com.live.module.home.bean.HomeNewDataBean
import com.live.module.home.bean.HomeVquBeckonBean
import com.live.module.home.bean.HomeVquChannelAnchorBean
import com.live.module.home.bean.HomeVquUserInfo
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.VquUserHomeBean
import com.live.vquonline.view.main.bean.HomeVquOnTvBean
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Home模块的接口
 *
 * ""
 * @since 6/4/21 5:51 PM
 */
interface HomeVquApiService{
    /**
     * 获取广告
     *
     * @return
     */
    @POST("user/getAdvert")
    @FormUrlEncoded
    suspend fun vquGetAdvert(@Field("position") position: String): BaseResponse<com.mshy.VInterestSpeed.common.bean.CommonVquAdBean>

    /**
     * 获取上电视数据
     *
     * @return
     */
    @POST("user/getOntvList")
    suspend fun vquGetOntvList(): BaseResponse<ArrayList<HomeVquOnTvBean>>

    /**
     * 推荐
     *
     * @return
     */
    @POST("index/recommend_anchors")
    @FormUrlEncoded
    suspend fun vquGetRecommendAnchors(@Field("page") vqupPage: String,@Field("number")number :Int): BaseResponse<HomeVquChannelAnchorBean>


    /**
     * 推荐新接口
     *
     * @return
     */
    @POST("v2/index/index/recommend")
    @FormUrlEncoded
    suspend fun vquGetNewRecommendAnchors(@Field("page") vqupPage: String,@Field("limit")number :Int): BaseResponse<HomeNewDataBean>


    /**
     * 在线接口
     *
     * @return
     */
    @POST("v2/index/index/online")
    @FormUrlEncoded
    suspend fun vquGetOnLineAnchors(@Field("page") vqupPage: String,@Field("limit")number :Int): BaseResponse<HomeNewDataBean>

    /**
     * 在线
     *
     * @return
     */
    @POST("index/online_list")
    @FormUrlEncoded
    suspend fun vquGetOnlineAnchors(@Field("page") vquPage: String): BaseResponse<HomeVquChannelAnchorBean>

    /**
     * 心动 or 搭讪
     *
     * @return
     */
    @POST("beckon/send")
    @FormUrlEncoded
    suspend fun vquSendBeckon(@Field("user_ids") vquUserIds: String,@Field("continue")isContinue:Int,@Field("is_tips")isTips:Int): BaseResponse<HomeVquBeckonBean>

    @POST("user/home")
    suspend fun vquGetUserInfo(@Body map: HashMap<String, Any>): BaseResponse<VquUserHomeBean>

    @POST("index/closeInfoFinishTip")
    suspend fun vquCloseInfoFinishTip(): BaseResponse<Any>

    /**
     * 音视频速配
     *
     * @return
     */
    @POST("matching.matching/addMatching")
    @FormUrlEncoded
    suspend fun vquAddMatching(@Field("type") type: String): BaseResponse<Any>


    /**
     * 搜索(男搜女)
     */
    @POST("index/getUserInfoByUserCode")
    @FormUrlEncoded
    suspend fun vquGetUserInfoByUserCode(@Field("search_name") searchName: String):BaseResponse<HomeVquUserInfo>

}