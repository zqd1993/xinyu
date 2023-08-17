package com.live.module.info.bean

import java.io.Serializable

data class InfoUserTagBean(
    var tag_list: ArrayList<Tag>,
    var user_tag: ArrayList<Tag>
)

data class Tag(
    var color: String,
    var id: Int,
    var name: String,
    var scene: Int,
    var status: Int,
    var system_status: Int,
    var type: Int,
    var isSelect:Boolean=false
) :Serializable