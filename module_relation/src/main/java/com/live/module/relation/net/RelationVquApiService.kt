package com.live.module.relation.net

import com.live.module.relation.bean.MyProtectionListBean
import com.mshy.VInterestSpeed.common.bean.VquFollowBean
import com.mshy.VInterestSpeed.common.bean.VquRelationListBean
import com.live.module.relation.bean.VquRelationPraiseListBean
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
interface RelationVquApiService {
    @POST("user/follow_list")
    @FormUrlEncoded
    suspend fun vquFollowList(@Field("page") page: Int): BaseResponse<VquRelationListBean>

    @POST("user/fans_list")
    @FormUrlEncoded
    suspend fun vquFansList(@Field("page") page: Int): BaseResponse<VquRelationListBean>

    @POST("user/visitor_list")
    @FormUrlEncoded
    suspend fun vquVisitorList(@Field("page") page: Int): BaseResponse<VquRelationListBean>

    @POST("user/viewer_list")
    @FormUrlEncoded
    suspend fun vquViewerList(@Field("page") page: Int): BaseResponse<VquRelationListBean>

    @POST("user/follow")
    @FormUrlEncoded
    suspend fun vquFollow(@Field("follow_uid") followId: String): BaseResponse<VquFollowBean>

    @POST("vod.trends/like_lists")
    @FormUrlEncoded
    suspend  fun vquLikeList(@Field("page") page: Int): BaseResponse<VquRelationPraiseListBean>

    /**
     * 心动 or 搭讪
     *
     * @return
     */
    @POST("beckon/send")
    @FormUrlEncoded
    suspend fun vquSendBeckon(@Field("user_ids") vquUserIds: String): BaseResponse<Any>

    /**
     * 守护我的
     */
    @POST("guardian/guardian_my_list")
    @FormUrlEncoded
    suspend fun vquGuardianMyList(@Field("page") page: Int): BaseResponse<MutableList<MyProtectionListBean>>

    /**
     * 我的守护
     */
    @POST("guardian/my_guardian_list")
    @FormUrlEncoded
    suspend fun vquMyGuardianList(@Field("page") page: Int): BaseResponse<MutableList<MyProtectionListBean>>
}