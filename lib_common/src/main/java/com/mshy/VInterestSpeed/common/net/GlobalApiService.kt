package com.mshy.VInterestSpeed.common.net

import com.alibaba.fastjson.JSONObject
import com.google.gson.internal.LinkedTreeMap
import com.live.module.login.bean.LoginTantaNicknameBean
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.uikit.bean.ChatIntimateBean
import com.mshy.VInterestSpeed.uikit.bean.UpdateOnlineBean
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * @Description: 全局服务
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:01
 *
 */
interface GlobalApiService {

    /**
     * 获取首页全局配置数据
     *
     * @return
     */
    @POST("system/index")
    @FormUrlEncoded
    suspend fun getSystemIndex(
        @Field("bindData") indData: String,
        @Field("channelCode") channelCode: String,
    ): BaseResponse<SystemBean>

    /**
     * 获取首页全局配置数据
     *
     * @return
     */
    @POST("app/config")
    suspend fun vquGetGlobalConfig(): BaseResponse<CommonVquGlobalConfigBean>

    /**
     *  1v1视频
     *
     * @return
     */
    @POST("chat/get_room")
    @FormUrlEncoded
    suspend fun vquGetCallInfoFromRoom(@Field("room_id") room_id: String): BaseResponse<VideoVquCallBean>

    /**
     *  1v1视频
     *
     * @return
     */
    @POST("chat/get_room")
    @FormUrlEncoded
    fun vquGetCallInfoFromRoomId(@Field("room_id") room_id: String): Call<BaseResponse<VideoVquCallBean>>

    /**
     * 上传错误日志
     */
    @POST("system/report")
    @FormUrlEncoded
    fun vquSystemReport(@Field("log") log: String): Call<BaseResponse<Any>>
    /**
     * 上传错误日志
     */
    @POST("v2/antian/index")
    @FormUrlEncoded
    fun vquRiskControl(@Field("token") token: String): Call<BaseResponse<Any>>
    /**
     *  速配测试
     *
     * @return
     */
    @POST("matching.matching/test")
    @FormUrlEncoded
    fun matchingTest(): Call<BaseResponse<Any>>


    @POST("dress/getAllBadge")
    suspend fun vquGetBadgeConfig(): BaseResponse<JSONObject>

    /**
     * 获取广告
     *
     * @return
     */
    @POST("user/getAdvert")
    @FormUrlEncoded
    suspend fun vquGetAdvert(@Field("position") position: String): BaseResponse<CommonVquAdBean>


    /**
     * 获取青少年模式
     */
    @POST("user/get_adolescent_status")
    suspend fun vquGetTeenMode(): BaseResponse<CommonVquStatusBean>


    @POST("index/banner")
    @FormUrlEncoded
    suspend fun vquGetMainBanner(@Field("cate_id") cateId: String): BaseResponse<CommonVquMainBannerBean>

    @POST("index/getWhoLookMeInfo")
    suspend fun getWhoLookMeInfo(): BaseResponse<MessageVisitorBean>


    companion object {

        val USER_PRIVACY_AGREEMENT_URL: String
            get() = "${NetBaseUrlConstant.AGREEMENT_BASE_URL}/index/about/privacy.html"

        val USER_SERVICE_AGREEMENT_URL: String
            get() = "${NetBaseUrlConstant.AGREEMENT_BASE_URL}/index/about/agreement.html"
    }


    @POST("app/citys")
    suspend fun citys(): BaseResponse<LinkedTreeMap<String, String>>


    @POST("wallet/index")
    suspend fun walletIndex(): BaseResponse<TantaWalletBean>



    @POST("wallet/recharge")
    @FormUrlEncoded
    suspend fun recharge(
        @Field("channel") payType: String,
        @Field("goods_id") id: String,
        @Field("polling") polling: Int,
    ): BaseResponse<TantaPayBean>

    /**
     * 获取sts
     */
    @POST("sts/index")
    suspend fun vquGetSts(@Body map: HashMap<String, Any>): BaseResponse<StsInfoBean>

    /**
     * 获取举报原因
     */
    @POST("user/report_cate")
    suspend fun vquReportCate(@Body map: HashMap<String, Any>): BaseResponse<MutableList<DynamicVquReportBean>>

    /**
     * 获取举报原因
     */
    @POST("user/report")
    suspend fun vquReport(@Body map: HashMap<String, Any>): BaseResponse<Any>

    /**
     * 获取举报原因
     */
    @POST("voice/list")
    suspend fun vquGetVoiceTextList(@Body map: HashMap<String, Any>): BaseResponse<InfoVoiceListBean>

    @POST("wallet/goods_list")
    @FormUrlEncoded
    suspend fun vquGoodsList(@Field("type") type: Int): BaseResponse<CommonVquRechargeData>

    @POST("task/share_task")
    @FormUrlEncoded
    fun vquShareTask(@Field("type") type: Int): Call<BaseResponse<Any>>

    /**
     * 领取红包
     */
    @POST("user/receiveTaskReward")
    @FormUrlEncoded
    fun vquReceiveTaskReward(@Field("task_id") id: String): Call<BaseResponse<CommonVquRedPacketBean>>

    /**
     * 领取红包(每日任务)
     */
    @POST("EveryDayActivity/userSend")
    @FormUrlEncoded
    fun vquReceiveTaskRewardDay(@Field("task_id") id: String): Call<BaseResponse<CommonVquRedPacketBean>>

    /**
     * 亲密度权益列表
     */
    @POST("chat_intimate/getIntimateV2")
    @FormUrlEncoded
    fun vquGetIntimateList(@Field("user_id") id: String): Call<BaseResponse<CommonIntimateListBean>>

    /**
     * 检测更新
     */
    @POST("app/system")
    @FormUrlEncoded
    fun vquUpdateVersion(@Field("update") update: String): Call<BaseResponse<SettingVquVersionBean>>

    /**
     * 获取拨打电话的价格
     */
    @POST("anchor/getPrice")
    @FormUrlEncoded
    fun vquGetPrice(
        @Field("anchor_id") id: String,
        @Field("is_new") isNew: Int,
    ): Call<BaseResponse<CommonCallPriceBean>>

    /**
     * 拨打语音或者视频
     * @param type 0视频 1语音
     */
    @POST("chat/call_new")
    @FormUrlEncoded
    fun vquGetCallInfo(
        @Field("to_uid") id: String,
        @Field("type") type: Int,
    ): Call<BaseResponse<VideoVquCallBean>>

    @POST("user/online_update")
    @FormUrlEncoded
    fun vquOnlineUpdate(@Field("task_id") id: String): Call<BaseResponse<UpdateOnlineBean>>

    /**
     * 查询亲密列表
     */
    @POST("chat_intimate/getIntimateList")
    suspend fun vquQueryIntimateList(): BaseResponse<ChatIntimateBean>

    /**
     * 查询是否认证
     */
    @POST("user/isAuth")
    suspend fun vquIsAuth(): BaseResponse<CommonVquAuthBean>

    /**
     * 在线状态
     */
    @POST("user/online_update")
    suspend fun getOnlineUpdate(): BaseResponse<UpdateOnlineBean>

    /**
     * 发送验证码
     */
    @POST("passport/send_code")
    @FormUrlEncoded
    suspend fun tantaSendCode(@FieldMap params: MutableMap<String, Any>): BaseResponse<Any>

    @POST("index/closeInfoFinishTip")
    suspend fun vquCloseInfoFinishTip(): BaseResponse<Any>
    /**
     * 发送验证码
     *
     * @return
     */
    @POST("passport/send_code")
    suspend fun tantaSendCode(@Body map: HashMap<String, Any>): BaseResponse<SystemBean>

    /**
     * 获取首页全局配置数据
     *
     * @return
     */
    @POST("system/index")
    suspend fun getSystemIndex(): BaseResponse<SystemBean>

    @POST("passport/oneKeyLogin")
    @FormUrlEncoded
    suspend fun onKeyLogin(@FieldMap params: MutableMap<String, Any>): BaseResponse<TantaLoginBean>


    @POST("passport/codeLogin")
    @FormUrlEncoded
    suspend fun tantaCodeLogin(@FieldMap params: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    @POST("user/nickname")
    suspend fun tantaNickname(): BaseResponse<LoginTantaNicknameBean>

    @POST("user/profile_reg")
    @FormUrlEncoded
    suspend fun tantaProfileReg(@FieldMap params: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    @POST("passport/third")
    @FormUrlEncoded
    suspend fun tantaPassportThird(@FieldMap netParams: MutableMap<String, Any>): BaseResponse<TantaLoginBean>

    /**
     * 首充
     */
    @POST("IndexActivity/index")
    suspend fun getFirstRechargeInfo(): BaseResponse<IndexActivityBean>


    //
    @POST("IndexActivity/sendGift")
    fun vquIndexActivitySendGift(): Call<BaseResponse<Any>>

    @POST("matching.matching/handelMatchCall")
    @FormUrlEncoded
    fun tantaReceiveCall(
        @Field("match_id") match_id: String,
        @Field("type") type: String,
    ): Call<BaseResponse<VideoVquCallBean>>

    @GET("pay/config")
    suspend fun getPayConfig():BaseResponse<MutableList<BillPaymentData>>

    @POST("wallet/recharge")
    @FormUrlEncoded
    suspend fun recharge(
        @Field("channel") channel: String,
        @Field("goods_id") goodsId: Int,
        @Field("polling") polling: Int,
        @Field("scheme") scheme: String
    ): BaseResponse<PayOrderInfoBean>

    @POST("vipOrder/payNobleOrder")
    @FormUrlEncoded
    suspend fun payNobleOrder(
        @Field("pay_type") channel: String,
        @Field("vip_goods_id") goodsId: String,
        @Field("polling") polling: Int,
        @Field("scheme") scheme: String,
        @Field("type") type: String,
        @Field("id") id: String
    ): BaseResponse<PayOrderInfoBean>

    @POST("user/check_event")
    @FormUrlEncoded
    fun checkEvent(@FieldMap params: MutableMap<String, Any>): Call<BaseResponse<Any>>

    @POST("batch.user/notify")
    suspend fun getNotifyMsg(): BaseResponse<NotifyMsgBean>

}