package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/15
 * description:
 */
data class TantaPayBean(
    @SerializedName("order")
    val order: Order,
    @SerializedName("payinfo")
    val payinfo: String,
    @SerializedName("wechatpayinfo")
    val wechatpayinfo: WechatPayInfo,
    @SerializedName("applet")
    val applet: Applet
)

data class Applet(
    @SerializedName("wechatOriginalId")
    val wechatOriginalId: String,
    @SerializedName("wechatAppletPath")
    val wechatAppletPath: String
)

data class WechatPayInfo(
    @SerializedName("appid")
    val appid: String,
    @SerializedName("mch_id")
    val mchId: String,
    @SerializedName("nonce_str")
    val nonceStr: String,
    @SerializedName("paySign")
    val paySign: String,
    @SerializedName("prepay_id")
    val prepayId: String,
    @SerializedName("result_code")
    val resultCode: String,
    @SerializedName("return_code")
    val returnCode: String,
    @SerializedName("return_msg")
    val returnMsg: String,
    @SerializedName("sign")
    val sign: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("trade_type")
    val tradeType: String
)

data class Order(
    @SerializedName("appid")
    val appid: String,
    @SerializedName("noncestr")
    val noncestr: String,
    @SerializedName("package")
    val packageX: String,
    @SerializedName("partnerid")
    val partnerid: String,
    @SerializedName("prepayid")
    val prepayid: String,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("channel_id")
    val channelId: String,
    @SerializedName("device")
    val device: String,
    @SerializedName("goods_id")
    val goodsId: Int,
    @SerializedName("order_no")
    val orderNo: String,
    @SerializedName("platform_name")
    val platformName: String,
    @SerializedName("platform_type")
    val platformType: Int,
    @SerializedName("recharge_money")
    val rechargeMoney: Double,
    @SerializedName("recharge_type")
    val rechargeType: Int,
    @SerializedName("type")
    val type: Int
)