package com.live.module.anchor.bean

import com.contrarywind.interfaces.IPickerViewData
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
data class AnchorTantaSettingBean(
    @SerializedName("chat")
    val chat: MutableList<Selection>,
    @SerializedName("star_grade")
    val starGrade: Int,
    @SerializedName("video")
    val video: MutableList<Selection>,
    @SerializedName("voice")
    val voice: MutableList<Selection>
) {
    data class Selection(
        @SerializedName("coins")
        val coins: String,
        @SerializedName("content")
        val content: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("star_grade")
        val starGrade: Int,
        @SerializedName("status")
        val status: Int
    ) : IPickerViewData {
        override fun getPickerViewText(): String {
            var text = "$coins$content"
//            if (starGrade > 1) {
//                text += "（魅力值达到${starGrade}级可选"
//            }
            return text
        }
    }

}

