package com.mshy.VInterestSpeed.uikit.attchment

import com.alibaba.fastjson.JSONObject
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser

/**
 * FileName: com.live.module.message.parser
 * Author: Reisen
 * Date: 2022/4/9 11:07
 * Description:
 * History:
 */
class MessageVquCustomParser : MsgAttachmentParser {
    companion object {
        fun packData(type: Int, data: JSONObject): String {
            val jsonObject = JSONObject()
            jsonObject["type"] = type
            jsonObject["data"] = data
            return jsonObject.toJSONString()
        }
    }

    override fun parse(attach: String): MsgAttachment? {
        var attachment: MessageVquBaseAttachment? = null
        val jsonObject = JSONObject.parseObject(attach)
        val type = jsonObject.getIntValue("type")
        val data = jsonObject.getJSONObject("data")
        when (type) {
            11 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquSysInfoAttachment(type)
            }
            12 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.SysInfoSingleImageAttachment(type)
            }
            13 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.SysInfoDoubleImageAttachment(type)
            }
            14 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquGiftAttachment(type)
            }
            15 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquVideoAttachment(type)
            }
            73 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquAudioAttachment(type)
            }
            16 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquAttentionAttachment(type)
            }
            17 -> {
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquDynamicAttachment(type)
            }
            18 -> {
                attachment =
                    com.mshy.VInterestSpeed.uikit.attchment.MessageVquOnlineNoticeAttachment(type)
            }
            72 -> {
                attachment = MessageVquFateMatchAttachment(type)
            }
            74 -> {//邀请通话
                attachment = com.mshy.VInterestSpeed.uikit.attchment.MessageVquInviteChatAttachment(type)
            }
            else -> {
            }
        }
        attachment?.fromJson(data);

        return attachment
    }
}