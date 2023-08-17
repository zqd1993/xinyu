package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/6/23
 * description:
 */
data class VquInfoAddressBean(
    @SerializedName("child")
    var child: List<VquInfoAddressBean>?,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("parent_id")
    var parentId: Int
)
