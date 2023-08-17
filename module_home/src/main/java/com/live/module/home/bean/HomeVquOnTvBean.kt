package com.live.vquonline.view.main.bean

data class HomeVquOnTvBean(
    var create_time: Int,
    var from_avatar: String,
    var from_nickname: String,
    var from_obscure: Int,
    var from_uid: Int,
    var gift_amount: Int,
    var gift_count: Int,
    var gift_id: Int,
    var gift_img: String,
    var gift_name: String,
    var id: Int,
    var lock_expired_time: Int,
    var lock_time: Int,
    var to_avatar: String,
    var to_nickname: String,
    var to_obscure: Int,
    var to_uid: Int
)