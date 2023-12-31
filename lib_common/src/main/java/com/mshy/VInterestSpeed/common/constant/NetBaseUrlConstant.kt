package com.mshy.VInterestSpeed.common.constant

import com.live.vquonline.base.constant.VersionStatus
import com.mshy.VInterestSpeed.common.BuildConfig


/**
 * 接口公共地址
 *
 * ""
 * @since 4/17/21 3:27 PM
 */
object NetBaseUrlConstant {
    var BASE_URL = "http://api.zhenban.top/"//正式环境

//    const val DEBUG_BASE_URL = "http://api.zhenban.top/"//测试环境
    const val DEBUG_BASE_URL = "http://120.78.160.71:8071/"//测试环境

    var IMAGE_URL = "http://asset.zhenban.top/"

    const val IMAGE_URL_2 = "http://asset.zhenban.top/"

    val MAIN_URL =
        if (BuildConfig.VERSION_TYPE == VersionStatus.RELEASE) DEBUG_BASE_URL + "api/" else DEBUG_BASE_URL + "api/"

    var ASSET_URL = ""
        get() {
            if (field.isEmpty()) {
                throw NotImplementedError("请求改你的 MAIN_URL 的值为自己的请求地址")
            }
            return field
        }

    const val VIP_URL = "/index/about/vip_agreement_qy.html"
    const val CONTRACT_URL = "/index/about/service.html"
    const val HELP_URL_BOY = "/index/about/help_boy.html"
    const val HELP_URL_GIRL = "/index/about/help_girl.html"
    const val HELP_URL_SPECIAL = "/index/about/help_audit.html"
    const val AGREEMENT_URL = "/queniang/about/agreement_qy.html" //用户协议
    const val RULE_URL = "/index/about/invite_help.html" //规则详情

    const val USER_PRIVACY_URL = "/queniang/about/privacy_qy.html" //用户隐私协议

    const val USER_permission_URL = "/queniang/about/permission_qy.html" //权限隐私

    const val USER_thirdparty_sdk_URL = "/index/about/thirdparty_sdk_qy.html" //第三方sdk目录

    const val RECHARGE_AGREEMENT = "/index/about/recharge_agreement_qy.html"    //充值协议
    const val JUVENILE_PROTECTION = "/index/about/juvenile_protection_qy.html"    //未成年保护计划

    val AGREEMENT_BASE_URL = "http://asset.zhenban.top"
}