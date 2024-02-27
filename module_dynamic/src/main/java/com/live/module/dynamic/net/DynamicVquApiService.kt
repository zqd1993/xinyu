package com.live.module.dynamic.net


import com.live.module.dynamic.bean.*
import com.mshy.VInterestSpeed.common.bean.DynamicVquReportBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonAuthBean
import retrofit2.http.*

/**
 * 动态模块
 *
 * @author tany
 * @since 2021/3/31 10:55
 */
interface DynamicVquApiService {

    /**
     * 获取动态列表
     *
     * @return
     */
    @POST("dynamic/lists")
    suspend fun vquDynamicList(@Body map:HashMap<String,Any>): BaseResponse<DynamicVquBean>
    /**
     * 获取动态列表
     *
     * @return
     */
    @POST("user/dynamic_list")
    suspend fun vquUserDynamicList(@Body map:HashMap<String,Any>): BaseResponse<DynamicVquBean>
    /**
     * 动态点赞
     *
     * @return
     */
    @POST("dynamic/like")
    suspend fun vquDynamicLike(@Body map:HashMap<String,Any>): BaseResponse<Any>

    /**
     * 发布视频
     */
    @POST("dynamic/publish")
    suspend fun vquPublishVideo(@Body map:HashMap<String,Any>): BaseResponse<DynamicPostBean>
    /**
     * 发布视频
     */
    @POST("dynamic/publish_image")
    suspend fun vquPublishImg(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 获取sts
     */
    @POST("sts/index")
    suspend fun vquGetSts(@Body map:HashMap<String,Any>): BaseResponse<com.mshy.VInterestSpeed.common.bean.StsInfoBean>
    /**
     * 获取举报原因
     */
    @POST("user/report_cate")
    suspend fun vquReportCate(@Body map:HashMap<String,Any>): BaseResponse<MutableList<DynamicVquReportBean>>
    /**
     * 获取举报原因
     */
    @POST("user/report")
    suspend fun vquReport(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 删除动态
     */
    @POST("dynamic/delete")
    suspend fun vquDeleteDynamic(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 删除动态
     */
    @POST("dynamic/delete_lower")
    suspend fun vquDeleteDynamicTextOrImg(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 是否认证
     */
    @POST("user/isAuth")
    suspend fun vquIsAuth(@Body map:HashMap<String,Any>): BaseResponse<CommonAuthBean>
    /**
     * 心动/搭讪
     */
    @POST("beckon/send")
    suspend fun vquSendBeckon(@Body map:HashMap<String,Any>): BaseResponse<Any>

    /**
     * 动态消息点赞数
     *
     * @return
     */
    @POST("dynamic/user_like_count")
    suspend fun vquDynamicLikeCount(@Body map:HashMap<String,Any>): BaseResponse<Int>
    /**
     * 动态详情
     *
     * @return
     */
    @POST("dynamic/detail")
    suspend fun vquDynamicInfo(@Body map:HashMap<String,Any>): BaseResponse<DynamicVquDetailBean>
    /**
     * 动态详情评论列表
     *
     * @return
     */
    @POST("dynamicComment/lists")
    suspend fun vquGetCommentList(@Body map:HashMap<String,Any>): BaseResponse<DynamicTantaCommentsBean>
    /**
     * 动态详情评论列表
     *
     * @return
     */
    @POST("dynamicComment/comment")
    suspend fun vquComment(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 删除评论
     *
     * @return
     */
    @POST("dynamicComment/delete")
    suspend fun vquDeleteComment(@Body map:HashMap<String,Any>): BaseResponse<Any>
    /**
     * 举报评论
     *
     * @return
     */
    @POST("user/report")
    suspend fun vquReportComment(@Body map:HashMap<String,Any>): BaseResponse<Any>
}
