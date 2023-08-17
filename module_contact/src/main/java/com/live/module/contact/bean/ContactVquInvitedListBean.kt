package com.live.module.contact.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/6/22
 * description:
 */
data class ContactVquInvitedListBean(
    @SerializedName("total")
    val total:Int,
    @SerializedName("page")
    val page:Int,
    @SerializedName("total_page")
    val totalPage:Int,
    @SerializedName("list")
    val list: MutableList<ContactVquInvitedData>?
)