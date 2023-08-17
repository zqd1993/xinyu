package com.live.module.bill.bean

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * author: Lau
 * date: 2022/4/20
 * description:
 */
class BillVquDetailSectionBean : SectionEntity {
    var date: String? = null
    var expend: Int = 0
    var income: String? = null

    var accountType: Int = 0
    var changeValue: String? = null
    var changeValueNew: String? = null
    var createTime: String? = null
    var fromUsername: String? = null
    var icon: String? = null
    var id: String? = null
    var platformName: String? = null
    var platformType: Int = 0
    var rechargeMoney: String? = null
    var systemStr: String? = null
    var type: Int = 0
    var tracUserId: String? = null

    override val isHeader: Boolean
        get() = !date.isNullOrEmpty()


    fun setHeaderBean(bean: BillVquDetailNewListBean) {
        date = bean.date
        expend = bean.expend
        income = bean.income
    }

    fun setChildBean(bean: BillVquItemData) {
        accountType = bean.accountType
        changeValue = bean.changeValue
        changeValueNew = bean.changeValueNew
        createTime = bean.createTime
        fromUsername = bean.fromUsername
        icon = bean.icon
        id = bean.id
        platformName = bean.platformName
        platformType = bean.platformType
        rechargeMoney = bean.rechargeMoney
        systemStr = bean.systemStr
        type = bean.type
        tracUserId = bean.tracUserId
    }
}