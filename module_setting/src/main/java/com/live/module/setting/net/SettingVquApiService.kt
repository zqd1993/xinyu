package com.live.module.setting.net


import com.live.module.setting.bean.BlackListBean
import com.live.module.setting.bean.FateMatchBean
import com.live.module.setting.bean.SettingVquBindBean
import com.live.module.setting.bean.ShowLocationBean
import com.mshy.VInterestSpeed.common.bean.*
import retrofit2.http.*

/**
 * 动态模块
 *
 * @author tany
 * @since 2021/3/31 10:55
 */
interface SettingVquApiService {

    /**
     * 女神开启勿扰
     *
     * @return
     */
    @POST("anchor/set_video_status")
    suspend fun vquSetVideoStatus(@Body map: HashMap<String, Any>): BaseResponse<Any>

    /**
     * 普通用户
     *
     * @return
     */
    @POST("user/isMsgRefuse")
    suspend fun vquSetUserVideoStatus(@Body map: HashMap<String, Any>): BaseResponse<Any>

    @POST("user/home")
    suspend fun vquGetUserInfo(@Body map: HashMap<String, Any>): BaseResponse<VquUserHomeBean>

    /**
     * 获取绑定信息
     */
    @POST("user/account_binds")
    suspend fun vquGetBindInfo(@Body map: HashMap<String, Any>): BaseResponse<SettingVquBindBean>
    /**
     * 发送验证码
     */
    @POST("passport/send_code")
    suspend fun vquSendCode(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 绑定手机号
     */
    @POST("user/bind_mobile")
    suspend fun vquBindMobile(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 确认手机号关闭青少年模式
     */
    @POST("user/verify_mobile_code")
    suspend fun vquCloseModeByCode(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 获取缘分牵线信息
     */
    @POST("user/getFateMatch")
    suspend fun vquGetFateMatch(@Body map: HashMap<String, Any>): BaseResponse<FateMatchBean>
    /**
     * 获取是否隐藏所在地
     */
    @POST("user/showLocation")
    suspend fun vquShowLocation(@Body map: HashMap<String, Any>): BaseResponse<ShowLocationBean>
    /**
     * 设置缘分牵线
     */
    @POST("user/setFateMatch")
    suspend fun vquSetFateMatch(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 设置青少年模式密码
     */
    @POST("user/set_adolescent")
    suspend fun vquSetTeenMode(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 黑名单
     */
    @POST("user/black_list")
    suspend fun vquGetBlackList(@Body map: HashMap<String, Any>): BaseResponse<VquListBean<BlackListBean>>

    /**
     * 移除黑名单/或者拉黑
     */
    @POST("user/black")
    suspend fun vquDoBlack(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 移除黑名单/或者拉黑
     */
    @POST("passport/close_account")
    suspend fun vquCloseAccount(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 移除黑名单/或者拉黑
     */
    @POST("user/switch_adolescent")
    suspend fun vquCloseTeenMode(@Body map: HashMap<String, Any>): BaseResponse<Any>
    /**
     * 获取版本信息
     */
    @POST("app/system")
    suspend fun vquCheckUpdate(@Body map: HashMap<String, Any>): BaseResponse<SettingVquVersionBean>
    /**
     * 是否认证
     */
    @POST("user/isAuth")
    suspend fun vquIsAuth(@Body map:HashMap<String,Any>): BaseResponse<CommonAuthBean>
    /**
     * 退出登录
     */
    @POST("user/loginOut")
    suspend fun vquLoginOut(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 获取点赞开关
     */
    @POST("user/getUserLikeStatus")
    suspend fun getUserLikeStatus(@Body map:HashMap<String,Any>): BaseResponse<String>
    /**
     * 设置点赞开关
     */
    @POST("user/setUserLikeStatus")
    suspend fun setUserLikeStatus(@Body map:HashMap<String,Any>): BaseResponse<Any>
}
