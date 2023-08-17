package com.live.module.anchor.bean

import android.os.CountDownTimer
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/25
 * description:
 */
data class AnchorVquHelloListBean(
    @SerializedName("count")
    val count: Int,
    @SerializedName("list")
    val list: MutableList<AnchorVquHelloTemplateBean>
)

data class AnchorVquHelloTemplateBean(
    @SerializedName("create_time")
    val createTime: Int,
    @SerializedName("file")
    val `file`: String?,
    @SerializedName("height")
    val height: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_default")
    val isDefault: Int,
    @SerializedName("len")
    val len: Int,
    @SerializedName("name")
    var name: String?,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("video_file")
    val videoFile: String?,
    @SerializedName("voice_file")
    val voiceFile: String?,
    @SerializedName("width")
    val width: Int,
    var playType: Int,
    var countDownTimer: CountDownTimer?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnchorVquHelloTemplateBean

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}