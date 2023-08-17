package com.live.module.anchor.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
data class AnchorVquSkillSettingBean(
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("service_count")
    val serviceCount: Int,
    @SerializedName("service_time")
    val serviceTime: Int,
    @SerializedName("skill_id")
    val skillId: String,
    @SerializedName("voice_price")
    val voicePrice: Int
)