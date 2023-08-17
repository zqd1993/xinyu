package com.mshy.VInterestSpeed.common.bean

/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
data class CommonVquMenuBean(
    var icon: Int = 0,
    var title: String,
    var desc: String = "",
    var type: Int = 0,
    var descIcon: Int = 0,
    var showRedPoint: Int = 0
) {

    var isSelected = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommonVquMenuBean

        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        return type
    }
}