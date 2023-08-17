package com.live.module.info.net


import com.live.module.info.bean.*
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import com.mshy.VInterestSpeed.common.bean.StsInfoBean
import com.mshy.VInterestSpeed.common.bean.VquInfoAddressBean
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import retrofit2.http.*

/**
 * 动态模块
 *
 * @author tany
 * @since 2021/3/31 10:55
 */
interface InfoVquApiService {

    /**
     *编辑资料用户信息
     * @return
     */
    @POST("user/getUserInfo")
    suspend fun vquGetUserInfo(@Body map: HashMap<String, Any>): BaseResponse<InfoVquUserBean>
    /**
     *个人主页
     * @return
     */
    @POST("user/getUserIndex")
    suspend fun vquGetUserInfoIndex(@Body map: HashMap<String, Any>): BaseResponse<InfoVquInfoBean>

    /**
     * 获取升高体重这些选项
     */
    @POST("user/getSelectData")
    suspend fun vquGetSelectData(@Body map: HashMap<String, Any>): BaseResponse<InfoVquSelectDataBean>
    /**
     * 获取用户标签
     */
    @POST("user/getUserTag")
    suspend fun vquGetUserTag(@Body map: HashMap<String, Any>): BaseResponse<InfoUserTagBean>
    /**
     * 获取城市列表
     */
    @POST("app/provinceCitys")
    suspend fun vquGetProvinceList(@Body map: HashMap<String, Any>): BaseResponse<MutableList<VquInfoAddressBean>>
    /**
     * 获取sts
     */
    @POST("sts/index")
    suspend fun vquGetSts(@Body map:HashMap<String,Any>): BaseResponse<StsInfoBean>
    /**
     * 编辑用户信息
     */
    @POST("user/improve_data")
    suspend fun vquSaveUserInfo(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 编辑用户信息
     */
    @POST("user/compareFacesAvatar")
    suspend fun vquCompareFacesAvatar(@Body map:HashMap<String,Any>): BaseResponse<String>
    /**
     * 关注/取消关注
     */
    @POST("user/follow")
    suspend fun vquFollow(@Body map:HashMap<String,Any>): BaseResponse<InfoVquFollowResultBean>
    /**
     * 拉黑/取消拉黑
     */
    @POST("user/black")
    suspend fun vquBlack(@Body map:HashMap<String,Any>): BaseResponse<InfoVquFollowResultBean>
    /**
     * 心动/搭讪
     */
    @POST("beckon/send")
    suspend fun vquSendBeckon(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 礼物墙
     */
    @POST("user/gift_list")
    suspend fun vquGetGiftList(@Body map:HashMap<String,Any>): BaseResponse<InfoVquGiftBean>

    /**
     *  1v1视频
     *
     * @return
     */
    @POST("chat/call")
    suspend fun vquGetCallInfo(@Body map:HashMap<String,Any>): BaseResponse<VideoVquCallBean>


    /**
     * 是否认证
     */
    @POST("user/isAuth")
    suspend fun vquIsAuth(@Body map:HashMap<String,Any>): BaseResponse<CommonAuthBean>

    @POST("user/user_remark")
    @FormUrlEncoded
    suspend fun saveRemark(
        @Field("user_id") userId: String,
        @Field("remark") remarkName: String
    ): BaseResponse<Any>

}
