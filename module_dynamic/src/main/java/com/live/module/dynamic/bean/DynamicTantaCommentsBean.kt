package com.live.module.dynamic.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/7/30
 * description:
 */
data class DynamicTantaCommentsBean(
    @SerializedName("list")
    var list: MutableList<DynamicTantaCommentBean>,
    @SerializedName("total")
    var total: Int
)
