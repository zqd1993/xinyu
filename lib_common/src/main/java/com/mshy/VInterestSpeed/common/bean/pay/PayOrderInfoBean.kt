package com.mshy.VInterestSpeed.common.bean.pay

data class PayOrderInfoBean(
    val version: Int,
    val mer_no: String,
    val mer_order_no: String,
    val create_time: String,
    val expire_time: String,
    val order_amt: String,
    val notify_url: String,
    val return_url: String,
    val create_ip: String,
    val product_code: String,
    val store_id: String,
    val goods_name: String,
    val clear_cycle: String,
    val pay_extra: String,
    val accsplit_flag: String,
    val jump_scheme: String,
    val meta_option: String,
    val sign_type: String,
    val sign: String,
    val order_status: Int,
    val url: String
)
