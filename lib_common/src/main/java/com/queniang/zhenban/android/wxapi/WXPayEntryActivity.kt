package com.queniang.zhenban.android.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.constant.*
import com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_PAY_BY_WX
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelpay.PayResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus


/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
    private lateinit var api: IWXAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.common_pay_result)

        api = WXAPIFactory.createWXAPI(this, WECHAT_APP_ID)
        api.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent);
        api.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {

    }

    override fun onResp(resp: BaseResp?) {
        if (resp == null) {
            return
        }
        if (resp.type == COMMAND_PAY_BY_WX) {
            if (0 == resp.errCode) {

                val payResp = resp as PayResp
                val payType = payResp.extData
                when (payType) {
                    PAY_NOBLE, PAY_RECHARGE_NOBLE_COIN -> {
                        ARouter.getInstance().build(RouteUrl.Common.CommonPayResultActivity)
                            .navigation()
                    }
                    PAY_RECHARGE_COIN_SUCCESS -> {
                        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.RechargeCoinSuccessEvent())
                    }
                }

//                val create =
//                    GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
//
//                    create.vquIndexActivitySendGift().execute()
                EventBus.getDefault().post(PayResultEvent())
                EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.WxPayEvent(payType))
//                EventBus.getDefault().post(VoiceRoomRefreshCoinEvent())
            }
//            EventBus.getDefault().post(PayEvent(resp.errCode))
            finish()
        }
    }

}