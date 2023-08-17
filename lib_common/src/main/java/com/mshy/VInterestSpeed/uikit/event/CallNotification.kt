package com.mshy.VInterestSpeed.uikit.event

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/5/10 15:30
 *
 */
 data class CallNotification(var isCaller: Boolean, var CallBean : com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean, var socket_url :String)

//data class CallNotification(var room_id: Int)