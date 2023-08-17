package com.live.vquonline.service.common

import com.alibaba.android.arouter.facade.template.IProvider

/**
 * author: Lau
 * date: 2022/6/8
 * description:
 */
interface ICommonService : IProvider {
    companion object {
        const val COMMON_SERVICE_PATH = "/lib_common/CommonService"
    }

    fun getToken(): String?
}