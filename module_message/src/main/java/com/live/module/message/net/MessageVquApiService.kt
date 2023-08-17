package com.live.module.message.net

import com.alibaba.fastjson.JSONObject
import com.live.module.message.bean.CallRecordData
import com.live.module.message.bean.ChatSettingBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquGlobalConfigBean
import com.mshy.VInterestSpeed.common.bean.gift.GiftListBean
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import com.mshy.VInterestSpeed.uikit.bean.ChatIntimateBean
import com.mshy.VInterestSpeed.uikit.bean.IMCostBean
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * FileName: com.live.module.message.repo
 * Date: 2022/4/14 15:10
 * Description: 消息模块的接口
 * History:
 */
interface MessageVquApiService {

    @POST("chat/chat_setting")
    @FormUrlEncoded
    suspend fun getChatInfo(@Field("user_id") userId: String): BaseResponse<ChatSettingBean>

    @POST("user/user_remark")
    @FormUrlEncoded
    suspend fun saveRemark(
        @Field("user_id") userId: String,
        @Field("remark") remarkName: String
    ): BaseResponse<Any>

    @POST("user/black")
    suspend fun vquDoBlack(@Body map: HashMap<String, Any>): BaseResponse<Any>

    @POST("user/getCommonWord")
    suspend fun vquSuspendGetCommonWord(): BaseResponse<List<NIMCommonWordBean>>

    @POST("User/userCallList")
    @FormUrlEncoded
    suspend fun vquGetCallRecords(@Field("page") page: Int): BaseResponse<CallRecordData>

    @POST("user/call_delete")
    @FormUrlEncoded
    suspend fun vquDeleteCallRecords(
        @Field("id") id: Int,
        @Field("type") type: Int
    ): BaseResponse<Any>

    /*----------------------------MsgFragment的请求方法----------------------------------------*/
    /**
     * 获取首页全局配置数据
     *
     * @return
     */
    @POST("app/config")
    fun getGlobalConfig(): Call<BaseResponse<CommonVquGlobalConfigBean>>

    @POST("user/getCommonWord")
    fun vquGetCommonWord(): Call<BaseResponse<List<NIMCommonWordBean>>>

    /**
     * 查询亲密值
     */
    @POST("chat_intimate/getIntimateList")
    @FormUrlEncoded
    fun vquQueryIntimate(@Field("ids") ids: String): Call<BaseResponse<ChatIntimateBean>>

    @POST("v2/chat/index/delBeckons")
    @FormUrlEncoded
    suspend fun vquDelBeckons(@Field("uids") ids: String): BaseResponse<Any>

    @POST("chat/call_new")
    @FormUrlEncoded
    fun vquGetCallInfo(
        @Field("to_uid") id: String,
        @Field("type") type: Int
    ): Call<BaseResponse<VideoVquCallBean>>

    /**
     *  送礼
     * @return
     */
    @POST("chat/send_gift_new")
    fun vquSendGift(@Body map: HashMap<String, Any>): Call<BaseResponse<GiftListBean>>

    /**
     *  获取聊天用户资料
     * @return
     */
    @POST("user/getChatData")
    @FormUrlEncoded
    fun vquGetChatInfo(@Field("user_id") userId: String): Call<BaseResponse<JSONObject>>


    /**
     *  发送提示消息
     * @return
     */
    @POST("index/send_tip_msg")
    @FormUrlEncoded
    fun vquSendTipMsg(@Field("user_id") userId: String): Call<BaseResponse<Any>>


    /**
     *  发送消息
     * @return
     */
    @POST("v2/chat/index/sendIm")
    fun vquSendIMCost(@Body map: HashMap<String, Any>): Call<BaseResponse<IMCostBean>>

    /**
     * 删除用户常用语
     */
    @POST("user/delCommonWord")
    @FormUrlEncoded
    suspend fun vquDeleteCommonWord(@Field("id") id: Int): BaseResponse<Any>

    /**
     * 新增用户常用语
     */
    @POST("user/addCommonWord")
    @FormUrlEncoded
    suspend fun vquAddCommonWord(@Field("word") word: String): BaseResponse<Any>

}