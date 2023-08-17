package com.mshy.VInterestSpeed.uikit.attchment
import com.alibaba.fastjson.JSONObject
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment


/**
 * FileName: com.live.module.message.attachment
 * Date: 2022/4/11 10:10
 * Description:
 * History:
 */
abstract class MessageVquBaseAttachment constructor(val type: Int) : MsgAttachment {

    // 解析附件内容。
    fun fromJson(data: JSONObject) {
        parseData(data)
    }

    // 实现 MsgAttachment 的接口，封装公用字段，然后调用子类的封装函数。
    override fun toJson(send: Boolean): String {
        return MessageVquCustomParser.packData(type, packData())
    }

    // 子类的解析和封装接口。
    protected abstract fun parseData(data: JSONObject)

    protected abstract fun packData(): JSONObject


}
