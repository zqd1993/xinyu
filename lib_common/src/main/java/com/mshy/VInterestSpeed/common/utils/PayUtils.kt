package com.mshy.VInterestSpeed.common.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import com.alipay.sdk.app.PayTask
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.WechatPayInfo
import com.mshy.VInterestSpeed.common.constant.PAY_RECHARGE_COIN_SUCCESS
import com.mshy.VInterestSpeed.common.constant.WECHAT_APP_ID
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

/**
 * author: Lau
 * date: 2022/4/28
 * description:
 */
object PayUtils {
    const val SDK_PAY_FLAG = 0xff01 //handlerMessage

    fun aliPay(activity: Activity, orderInfo: String) {
        val payRunnable = Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(orderInfo, true)
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }

        val payThread = Thread(payRunnable)
        payThread.start()
    }

    /**
     * 支付宝支付结果的回调
     */
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == SDK_PAY_FLAG) {
                val result = msg.obj as Map<String, String>

                val payResult = com.mshy.VInterestSpeed.alipay.PayResult(result)
                //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                val resultStatus: String? = payResult.resultStatus
                //判断resultStatus 为9000则代表支付成功
                if ("9000" == resultStatus) {
                    //该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    //支付成功
//                    mViewModel.vquWalletIndex()
//                    this@BillVquRechargeActivity.setResult(Activity.RESULT_OK)
                    EventBus.getDefault().post(PayResultEvent())
                    EventBus.getDefault().post(com.mshy.VInterestSpeed.common.bean.RechargeCoinSuccessEvent())


//                    val create =
//                        GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
//                    create.vquIndexActivitySendGift().execute()
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    toast("支付失败")
                }
            }
        }
    }

    fun wechatPay(context: Context, result: TantaPayBean) {
        val wxApi = WXAPIFactory.createWXAPI(context, WECHAT_APP_ID, false)
        val wechatPayInfo: WechatPayInfo = result.wechatpayinfo

        val req = PayReq()
        req.appId = WECHAT_APP_ID
        req.partnerId = wechatPayInfo.mchId //商户号

        req.prepayId = wechatPayInfo.prepayId //预付款id

        req.nonceStr = wechatPayInfo.nonceStr
        req.timeStamp = wechatPayInfo.timestamp
        req.packageValue = "Sign=WXPay" //固定值

        req.sign = wechatPayInfo.paySign
        req.extData = PAY_RECHARGE_COIN_SUCCESS

        //先检测是否安装了微信
        val isWXAppInstalledAndSupported = wxApi.isWXAppInstalled
        if (isWXAppInstalledAndSupported) {
            wxApi.registerApp(WECHAT_APP_ID)
            wxApi.sendReq(req)
        } else {
            toast("未安装微信，不能支付")
        }
    }

    fun wxMiniPay(context: Context, result: TantaPayBean) {
        val wxApi = WXAPIFactory.createWXAPI(context, WECHAT_APP_ID, false)
        val req =WXLaunchMiniProgram.Req()
        req.openId

    }
}