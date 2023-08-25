package com.live.module.vip.bean

import com.mshy.VInterestSpeed.common.bean.pay.RechargeRoute
import java.io.Serializable

/**
 * @desc
 */
class VipPayInfoBean : Serializable {
    var nameLabel: String = ""
    var vip_id: String = ""
    var vip_goods_id: String = ""
    var pay_type: String = ""
    var type: String = ""
    var money: String = ""
    var rechargeRoute: RechargeRoute? = null

}