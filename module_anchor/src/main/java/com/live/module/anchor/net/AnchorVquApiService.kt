package com.live.module.anchor.net

import com.live.module.anchor.bean.AnchorVquHelloListBean
import com.live.module.anchor.bean.AnchorTantaPriceBean
import com.live.module.anchor.bean.AnchorTantaSettingBean
import com.live.module.anchor.bean.AnchorVquSkillSettingBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
interface AnchorVquApiService {
    @POST("anchor/skill_setting")
    suspend fun vquSkillSetting(): BaseResponse<MutableList<AnchorVquSkillSettingBean>>

    /**
     * 获取主播收费设置参数
     *
     * params请求参数：
     * type 主播收费设置列表（为空获取全部）：video，chat，voice
     */
    @POST("anchor/get_anchor_setting")
    @FormUrlEncoded
    suspend fun vquGetAnchorSetting(@FieldMap params: MutableMap<String, String>): BaseResponse<AnchorTantaSettingBean>

    @POST("AnchorMan/get_anchor_setting")
    @FormUrlEncoded
    suspend fun vquManGetAnchorSetting(@FieldMap params: MutableMap<String, String>): BaseResponse<AnchorTantaSettingBean>

    @POST("anchor/getPrice")
    @FormUrlEncoded
    suspend fun vquGetPrice(@FieldMap params: MutableMap<String, String>): BaseResponse<AnchorTantaPriceBean>

    @POST("AnchorMan/getPrice")
    @FormUrlEncoded
    suspend fun vquManGetPrice(@FieldMap params: MutableMap<String, String>): BaseResponse<AnchorTantaPriceBean>

    /**
     *  设置技能价格
     *
     *  params请求参数：
     *  skill_id 技能ID
     *  type 2视频，1语音，5文字
     *  price_id 选择id
     */
    @POST("anchor/set_price")
    @FormUrlEncoded
    suspend fun vquSetPrice(@FieldMap params: MutableMap<String, Any>): BaseResponse<String>

    @POST("AnchorMan/set_price")
    @FormUrlEncoded
    suspend fun vquManSetPrice(@FieldMap params: MutableMap<String, Any>): BaseResponse<String>

    @POST("anchor/set_video_status")
    @FormUrlEncoded
    suspend fun vquSetVideoStatus(@Field("status") status: Int): BaseResponse<Any>

    @POST("greet/lists_new")
    suspend fun vquListNew(): BaseResponse<AnchorVquHelloListBean>

    @POST("greet/set_name")
    @FormUrlEncoded
    suspend fun vquSetName(@FieldMap params: MutableMap<String, Any>): BaseResponse<Any>

    @POST("greet/delete_new")
    @FormUrlEncoded
    suspend fun vquDeleteNew(@Field("id") id: String): BaseResponse<Any>

    @POST("greet/set_default")
    @FormUrlEncoded
    suspend fun vquSetDefault(@Field("id") id: String): BaseResponse<Any>

    @POST("sts/index")
    @FormUrlEncoded
    suspend fun vquGetStsIndex(@Field("type") type: String): BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>

    /**
     * 添加打招呼语v1.8
     *
     * params参数：
     * type 此处不传
     * title 文字
     * file 图片链接
     * length 语音时长
     * is_multi 默认1
     * voice_file 语音链接
     * video_file 视频链接
     */
    @POST("greet/add")
    @FormUrlEncoded
    suspend fun vquGreetAdd(@FieldMap params: MutableMap<String, Any>): BaseResponse<Any>
}