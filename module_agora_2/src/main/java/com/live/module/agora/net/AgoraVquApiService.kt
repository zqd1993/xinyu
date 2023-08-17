package com.live.module.agora.net


import com.live.module.agora.bean.AgoraVquChatShootBean
import com.live.module.agora.bean.AgoraVquMatchRecordBean
import com.live.module.agora.bean.AgoraVquReduceBean
import com.live.module.agora.bean.VquVideoFeeBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Home模块的接口
 *
 * ""
 * @since 6/4/21 5:51 PM
 */
interface AgoraVquApiService {
    /**
     * 获取广告
     *
     * @return
     */
    @POST("user/getAdvert")
    @FormUrlEncoded
    suspend fun vquGetAdvert(@Field("position") position: String): BaseResponse<com.mshy.VInterestSpeed.common.bean.CommonVquAdBean>


    /**
     * 截取主叫者的图片
     *
     * @return
     */
    @POST("chat/snapshoot_v2")
    @FormUrlEncoded
    suspend fun vquPostChatShootFrom(
        @Field("room_id") roomId: String,
        @Field("from") from: String,
        @Field("uid") id: String
    ): BaseResponse<AgoraVquChatShootBean>

    /**
     * 获取减免
     *
     * @return
     */
    @POST("chat/getReduce")
    @FormUrlEncoded
    suspend fun vquGetReduce(@Field("room_id") roomId: String): BaseResponse<AgoraVquReduceBean>

    /**
     * 视频结束
     *
     * @return
     */
    @POST("chat/video_charging")
    @FormUrlEncoded
    suspend fun vquGetVideoFee(@Field("room_id") roomId: String): BaseResponse<VquVideoFeeBean>

    /**
     * 提交评价
     *
     * @return
     */
    @POST("chat/video_score")
    @FormUrlEncoded
    suspend fun vquEvaluateVideo(
        @Field("room_id") roomId: String,
        @Field("score") score: String,
        @Field("to_uid") to_uid: String
    ): BaseResponse<Any>


    /**
     * 音视频速配
     *
     * @return
     */
    @POST("matching.matching/addMatching")
    @FormUrlEncoded
    suspend fun vquAddMatching(@Field("type") type: String): BaseResponse<AgoraVquMatchRecordBean>

    /**
     * 音视频速配
     *
     * @return
     */
    @POST("matching.matching/cancelMatching")
    @FormUrlEncoded
    suspend fun vquCancelMatching(@Field("type") type: String): BaseResponse<Any>

    /**
     * 音视频速配
     *
     * @return
     */
    @POST("matching.matching/cancelMatching")
    suspend fun vquMatchRecord(): BaseResponse<Any>

    /**
     * 音视频速配
     *
     * @return
     */
    @POST("matching.matching/handelMatchCall")
    @FormUrlEncoded
    suspend fun vquReceiveCall(
        @Field("match_id") match_id: String,
        @Field("type") type: String
    ): BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>

    /**
     * 监控
     *
     * @return
     */
    @POST("chat/videoStream")
    @FormUrlEncoded
    suspend fun vquCheckVideo(
        @Field("swAppId") swAppId: String,
        @Field("channel") channel: String,
        @Field("room_id") room_id: String
    ): BaseResponse<Any>


    /**
     * 截取主叫者的图片
     *
     * @return
     */
    @POST("chat/snapshoot_v2")
    @FormUrlEncoded
    suspend fun vquPostChatShootFromCall(
        @Field("room_id") roomId: String,
        @Field("from") from: String,
        @Field("uid") uid: String
    ): BaseResponse<AgoraVquChatShootBean>
}