package com.mshy.VInterestSpeed.common.utils

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.alipay.sdk.app.PayTask
import com.live.vquonline.base.utils.ToastUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.WechatPayInfo
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.constant.PAY_RECHARGE_COIN_SUCCESS
import com.mshy.VInterestSpeed.common.constant.WECHAT_APP_ID
import com.pay.paytypelibrary.base.OnPayResultListener
import com.pay.paytypelibrary.base.OrderInfo
import com.pay.paytypelibrary.base.PayUtil
import com.sand.qzf.paytypesdk.base.PayTypeSdk
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import com.sand.qzf.paytypesdk.base.CallBack

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
                    EventBus.getDefault()
                        .post(com.mshy.VInterestSpeed.common.bean.RechargeCoinSuccessEvent())


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
        val req = WXLaunchMiniProgram.Req()
        req.openId

    }

    fun sandAliPay(activity: Activity, orderJson: String) {
        PayUtil.CashierPayMulti(activity, orderJson)
    }

    fun sendWechat(activity: Activity, infoBean: PayOrderInfoBean){
        val orderJson = JSONObject()
        orderJson.put("version", infoBean.version)
        orderJson.put("mer_no", infoBean.mer_no)
        orderJson.put("mer_order_no", infoBean.mer_order_no)
        orderJson.put("create_time", infoBean.create_time)
        orderJson.put("expire_time", infoBean.expire_time)
        orderJson.put("order_amt", infoBean.order_amt)
        orderJson.put("notify_url", infoBean.notify_url)
        orderJson.put("return_url", infoBean.return_url)
        orderJson.put("create_ip", infoBean.create_ip)
        orderJson.put("product_code", infoBean.product_code)
        orderJson.put("store_id", infoBean.store_id)
        orderJson.put("goods_name", infoBean.goods_name)
        orderJson.put("clear_cycle", infoBean.clear_cycle)
        orderJson.put("pay_extra", infoBean.pay_extra)
        orderJson.put("accsplit_flag", infoBean.accsplit_flag)
        orderJson.put("jump_scheme", infoBean.jump_scheme)
        orderJson.put("meta_option", infoBean.meta_option)
        orderJson.put("sign_type", infoBean.sign_type)
        orderJson.put("sign", infoBean.sign)
        orderJson.put("order_status", infoBean.order_status)
        PayUtil.CashierPaySingle(activity, orderJson.toString(), object : OnPayResultListener {
            override fun onSuccess(orderInfo: OrderInfo) {
                if(infoBean.product_code == "02010005") {
                    startSandWeChatPay(activity, orderInfo)
                }
            }

            override fun onError(s: String) {
                ToastUtils.showToast("支付失败 ： $s", Toast.LENGTH_SHORT)
            }
        })
    }

    private  fun startSandWeChatPay(context: Context?, orderInfo: OrderInfo) {
        val appId = orderInfo.wxAppId // 填应用AppId
        val api = WXAPIFactory.createWXAPI(context, appId)
        api.registerApp(appId)
        val req = WXLaunchMiniProgram.Req()
        req.userName = orderInfo.ghOriId // 填小程序原始id
        req.path =
            orderInfo.pathUrl + "token_id=" + orderInfo.tokenId //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = orderInfo.miniProgramType.toInt() // 可选打开 开发版，体验版和正式版
        //先检测是否安装了微信
        val isWXAppInstalledAndSupported = api.isWXAppInstalled
        if (isWXAppInstalledAndSupported) {
            api.sendReq(req)
        } else {
            toast("未安装微信，不能支付")
        }
    }

    fun sandWechat(activity: Activity, cashierUrl: String){
        PayTypeSdk.getInstance().cashierPay(activity, cashierUrl, object : CallBack{
            override fun onFinish(p0: String?) {
                ToastUtils.showShort("支付完成")
            }

            override fun onSuccess(p0: String?) {
                ToastUtils.showShort("支付成功")
            }

            override fun onError(p0: String?, p1: String?, p2: String?) {
                ToastUtils.showShort("支付失败")
            }

        })
    }
}