package com.mshy.VInterestSpeed.common.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.vquonline.service.common.ICommonService
import com.mshy.VInterestSpeed.common.utils.UserSpUtils

/**
 * author: Lau
 * date: 2022/6/8
 * description:
 */
@Route(path = ICommonService.COMMON_SERVICE_PATH)
class CommonService : ICommonService {
    override fun getToken(): String? {
        val token = UserSpUtils.getUserBean()?.token
        return token
    }

    override fun init(context: Context?) {

    }
}