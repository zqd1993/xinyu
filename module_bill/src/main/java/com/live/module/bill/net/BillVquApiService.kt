package com.live.module.bill.net

import com.live.module.bill.bean.*
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquRechargeData
import com.mshy.VInterestSpeed.common.bean.WarningBean
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
interface BillVquApiService {

    @POST("wallet/goods_list")
    @FormUrlEncoded
    suspend fun vquGoodsList(@Field("type") type: Int): BaseResponse<CommonVquRechargeData>

    /**
     * 设备  1安卓  2ios  3h5
     */
    @POST("pay/pay_type")
    @FormUrlEncoded
    suspend fun getWechatPayType(@Field("device") device: Int): BaseResponse<BillTantaWechatPayTypeData>

    @POST("wallet/withdraw")
    suspend fun withdraw(): BaseResponse<BillVquEarningData>

    @POST("wallet/bind_alipay")
    @FormUrlEncoded
    suspend fun vquBindAlipay(
        @Field("card_name") name: String,
        @Field("card_account") account: String
    ): BaseResponse<String>

    @POST("wallet/get_withdraw_price")
    @FormUrlEncoded
    suspend fun vquGetWithdrawPrice(@Field("money") money: String): BaseResponse<BillVquWithdrawPriceBean>

    @POST("wallet/user_withdraw")
    @FormUrlEncoded
    suspend fun vquUserWithDraw(@Field("money") money: String): BaseResponse<String>

    @POST("wallet/changeCoin")
    @FormUrlEncoded
    suspend fun vquChangeCoin(@Field("moneys") money: Int): BaseResponse<String>


    @POST("wallet/record")
    @FormUrlEncoded
    suspend fun vquWalletRecord(@FieldMap params: MutableMap<String, Any>): BaseResponse<BillVquDetailBean>

    @POST("wallet/account_info")
    @FormUrlEncoded
    suspend fun vquAccountInfo(
        @FieldMap params: MutableMap<String, Any>
    ): BaseResponse<BillVquAccountInfo>

    @POST("wallet/account_save")
    @FormUrlEncoded
    suspend fun vquAccountSave(@FieldMap params: MutableMap<String, Any>): BaseResponse<Any>

    @POST("wallet/alipay")
    suspend fun vquWalletAlipay(): BaseResponse<BillVquAccountInfo>

    @POST("wallet/user_withdraw_new")
    @FormUrlEncoded
    suspend fun vquUserWithDrawNew(@Field("money") money: String): BaseResponse<Any>

    @POST("wallet/account_list")
    suspend fun vquAccountList(): BaseResponse<BillVquAccountList>

    @POST("wallet/account_use")
    @FormUrlEncoded
    suspend fun vquAccountUse(@Field("id") id: String): BaseResponse<Any>


    @POST("wallet/cashout_record")
    @FormUrlEncoded
    suspend fun vquCashoutRecord(
        @Field("page") page: Int,
        @Field("date") date: String
    ): BaseResponse<BillVquWithdrawalListBean>

    @POST("v2/index/user/rechargeWarning")
    suspend fun vquRechargeWarning():BaseResponse<WarningBean>
}