package com.mshy.VInterestSpeed.uikit.attchment

import com.alibaba.fastjson.JSONObject

/**
 * FileName: com.live.module.message.attachment
 * Date: 2022/4/19 10:59
 * Description:
 * History:
 */
class MessageVquFateMatchAttachment(type: Int) : MessageVquBaseAttachment(type) {

    override fun parseData(data: JSONObject) {
    }

    override fun packData(): JSONObject {
        return JSONObject()
    }
}