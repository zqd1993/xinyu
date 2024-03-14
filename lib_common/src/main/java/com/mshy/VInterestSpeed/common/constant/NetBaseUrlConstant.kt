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
    var BASE_URL = "https://api.shanhaihulian.com/"//正式环境

    const val DEBUG_BASE_URL = "https://api.shanhaihulian.com/"//测试环境
//    const val DEBUG_BASE_URL = "https://develop.shanhaihulian.cn/"//测试环境

    var IMAGE_URL = "https://asset.whzhenban.top/"

    const val IMAGE_URL_2 = "https://asset.whzhenban.top/"

    val MAIN_URL =
        if (BuildConfig.VERSION_TYPE == VersionStatus.RELEASE) BASE_URL + "api/" else DEBUG_BASE_URL + "api/"

    var ASSET_URL = ""
        get() {
            if (field.isEmpty()) {
                throw NotImplementedError("请求改你的 MAIN_URL 的值为自己的请求地址")
            }
            return field
        }

    const val VIP_URL = "/micheng/agreement/index/about/vip_agreement_qy.html"
    const val CONTRACT_URL = "/micheng/agreement/index/about/service.html"
    const val HELP_URL_BOY = "/micheng/agreement/index/about//help_boy.html"
    const val HELP_URL_GIRL = "/micheng/agreement/index/about/help_girl.html"
    const val HELP_URL_SPECIAL = "/micheng/agreement/index/about/help_audit.html"
    const val AGREEMENT_URL = "/micheng/agreement/index/about/agreement_qy.html" //用户协议
    const val RULE_URL = "/index/about/invite_help.html" //规则详情

    const val USER_PRIVACY_URL = "/micheng/agreement/index/about/privacy_qy.html" //用户隐私协议

    const val USER_permission_URL = "/micheng/agreement/index/about/permission_qy.html" //权限隐私

    const val USER_thirdparty_sdk_URL = "/micheng/agreement/index/about/thirdparty_sdk_qy.html" //第三方sdk目录

    const val RECHARGE_AGREEMENT = "/micheng/agreement/index/about/recharge_agreement_qy.html"    //充值协议
    const val JUVENILE_PROTECTION = "/micheng/agreement/index/about/juvenile_protection_qy.html"    //未成年保护计划

//    const val VIP_URL = "/index/about/vip_agreement_qy_hw.html"
//    const val CONTRACT_URL = "/index/about/service_hw.html"
//    const val HELP_URL_BOY = "/index/about/help_boy_hw.html"
//    const val HELP_URL_GIRL = "/index/about/help_girl_hw.html"
//    const val HELP_URL_SPECIAL = "/index/about/help_audit_hw.html"
//    const val AGREEMENT_URL = "/micheng/about/agreement_qy_hw.html" //用户协议
//    const val RULE_URL = "/index/about/invite_help_hw.html" //规则详情
//
//    const val USER_PRIVACY_URL = "/micheng/about/privacy_qy_hw.html" //用户隐私协议
//
//    const val USER_permission_URL = "/micheng/about/permission_qy_hw.html" //权限隐私
//
//    const val USER_thirdparty_sdk_URL = "/index/about/thirdparty_sdk_qy_hw.html" //第三方sdk目录
//
//    const val RECHARGE_AGREEMENT = "/index/about/recharge_agreement_qy_hw.html"    //充值协议
//    const val JUVENILE_PROTECTION = "/index/about/juvenile_protection_qy_hw.html"    //未成年保护计划

    val AGREEMENT_BASE_URL = "https://asset.whzhenban.top"
}