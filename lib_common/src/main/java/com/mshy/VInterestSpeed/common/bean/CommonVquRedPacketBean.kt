package com.mshy.VInterestSpeed.common.bean

/**
 * author: Tany
 * date: 2022/5/5
 * description:
 */
data class CommonVquRedPacketBean(
    var title: String,//任务标题
    var price: String,//任务奖励的钱或金币
    var company: String,//单位。。金币/元
    var task_id: String,
    var type:Int
)