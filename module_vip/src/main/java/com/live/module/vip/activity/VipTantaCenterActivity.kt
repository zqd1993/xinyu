package com.live.module.vip.activity

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.vip.R
import com.live.module.vip.adapter.VipPrivilegeAdapter
import com.live.module.vip.adapter.VipRechargeAdapter
import com.live.module.vip.bean.VipPayInfoBean
import com.live.module.vip.bean.TantaVipInfoBean
import com.live.module.vip.databinding.VipTantaActivityCenterBinding
import com.live.module.vip.dialog.PayWaySelectDialog
import com.live.module.vip.dialog.VipDailyGifDialog
import com.live.module.vip.vm.VipTantaCenterModel
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.WECHAT_APP_ID
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog
import com.mshy.VInterestSpeed.common.utils.PayUtils
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
@AndroidEntryPoint
@EventBusRegister
@Route(path = RouteUrl.Vip.VipTantaCenterActivity)
class VipTantaCenterActivity : BaseActivity<VipTantaActivityCenterBinding, VipTantaCenterModel>() {
    @Autowired(name = RouteKey.TYPE_OPEN_SUCCEED)
    @JvmField
    var isVip = false
    private var vipInfo: TantaVipInfoBean? = null
    override val mViewModel: VipTantaCenterModel by viewModels()
    private val rechargeAdapter = VipRechargeAdapter()
    private val privilegeAdapter = VipPrivilegeAdapter()
    private var vipPayInfoBean: VipPayInfoBean? = null

    private var paymentList: MutableList<BillPaymentData> = mutableListOf()

    private var payCode = ""

    private val vquPayDialog by lazy {
        PayDialog()
    }

    override fun setStatusBar() {
        ImmersionBar.with(this@VipTantaCenterActivity).reset()
            .transparentStatusBar()
            .statusBarDarkFont(true, 0.5f)
            .fitsSystemWindows(false)
            .init()
    }

    override fun initObserve() {
        //充值列表
        mViewModel.vipIndexResultData.observe(this) {
            vipInfo = it
            isVip = it.info.vip_id > 0
            if (it.info.vip_id > 0) {
                UserManager.userInfo?.vip = it.info.vip_id
                UserManager.userInfo?.vipDes = it.info.expire_time
                mBinding.tvVipSubText.text = it?.info?.expire_time
                mBinding.vipCenterUserName.text = it.info.nickname
                mBinding.ivAvatar.vquLoadCircleImage(
                    NetBaseUrlConstant.IMAGE_URL + it.info.avatar
                )
                LogUtil.e("IMAGE_URL=${NetBaseUrlConstant.IMAGE_URL}")
                LogUtil.e("avatar=${it.info.avatar}")
            }
            refreshVipValue()
            rechargeAdapter.setNewInstance(it.list)
            privilegeAdapter.setNewInstance(it.privilege)

            //每日礼物
            if (it.info.gift != null) {
                var giftBean = it.info.gift
                mBinding.srlDailyGif.visibility =
                    if (giftBean.is_show == 1
                        && UserManager.userInfo?.gender == 1
                    ) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                LogUtil.e("is_reward=${giftBean.is_reward}")
                //is_reward
                if (giftBean.is_reward == 0) {
                    mBinding.btDailyGif.text = "领取"
                    mBinding.btDailyGif.setTextColor(ResUtils.getColor(R.color.color_934800))
                    mBinding.btDailyGif.setStartColor(
                        ResUtils.getColor(R.color.color_E5D0AB),
                        ResUtils.getColor(R.color.color_CDAF78)
                    )
                } else {
                    mBinding.btDailyGif.text = "已领取"
                    mBinding.btDailyGif.setTextColor(resources.getColor(R.color.color_FFFFFF))
                    mBinding.btDailyGif.setStartColor(
                        ResUtils.getColor(R.color.color_CCCCCC),
                        ResUtils.getColor(R.color.color_CCCCCC)
                    )

                }
            }
        }

        mViewModel.vipOrderCreateResultData.observe(this) {
            var payType = vipPayInfoBean?.pay_type
            when (payType) {
                PayDialog.WECHAT -> {
                    PayUtils.wechatPay(this, it)
                }

                PayDialog.ALIPAY -> {
                    PayUtils.aliPay(this, it.payinfo)
                }

                PayDialog.TAI_SHAN_PAY -> {
                    PayUtils.aliPay(this, it.payUrl)
                }

                PayDialog.WECHAT_APPLET -> {
                    jumpToWechatApplet(it)
                }

                PayDialog.LE_SHUA_PAY, PayDialog.WECHAT_H5_PAY -> {
                    if (it.payUrl.isNotEmpty()) {
                        val intent = Intent()
                        intent.action = Intent.ACTION_VIEW
                        val targetUrl = Uri.parse(it.payUrl)
                        intent.data = targetUrl
                        startActivity(intent)
                    }
                }
            }
        }

        //每日礼物领取
        mViewModel.vipDailyGifResultData.observe(this) {
            mViewModel.getVipIndexInfo()
        }
        mViewModel.payConfigData.observe(this) {
            paymentList = it
        }
        mViewModel.orderJson.observe(this) {
            if (it != null) {
                when (payCode) {
                    "sand_wechat" -> {
                        PayUtils.sendWechat(this, it)
                    }

                    "sand_alipay" -> {
                        PayUtils.sendWechat(this, it)
                    }

                    "sand_wechat_v4", "sand_alipay_v4" -> {
                        PayUtils.sandWechat(this, it.url)
                    }
                }
            }
        }
    }

    override fun initRequestData() {

    }

    override fun VipTantaActivityCenterBinding.initView() {
        mBinding.ivAvatar.vquLoadCircleImage(
            NetBaseUrlConstant.IMAGE_URL + UserManager.userInfo?.avatar
        )
        mBinding.vipCenterUserName.text = UserManager.userInfo?.nickname

        refreshVipValue()
        mBinding.rvRecharge.adapter = rechargeAdapter
        mBinding.rvPrivilege.adapter = privilegeAdapter

        vquSetViewClick()
    }

    private fun refreshVipValue() {
        if (isVip) {
            mBinding.rvRecharge.visibility = View.GONE
            mBinding.btPay.text = "立即续费"
            mBinding.tvVipSubText.text = UserManager.userInfo?.vipDes
            mBinding.tvVipSubText.setTextColor(resources.getColor(R.color.color_FF3B30))
        } else {
            mBinding.rvRecharge.visibility = View.VISIBLE
            mBinding.btPay.text = "立即开通"
            mBinding.tvVipSubText.text = "未开通VIP会员"
            mBinding.tvVipSubText.setTextColor(resources.getColor(R.color.color_894C15))
        }
    }

    private fun vquSetViewClick() {
        mBinding.ivBack.setViewClickListener(1) {
            finish()
        }
        mBinding.btPay.setViewClickListener(1) {
            if (vipInfo == null || vipInfo?.list == null || vipInfo?.list?.size == 0 || paymentList.size == 0) {
                toast("网络异常~")
                return@setViewClickListener
            }
            var payWaySelectDialog = PayWaySelectDialog()

            if (isVip) {
                payWaySelectDialog.rechargeData = vipInfo?.list!!
            } else {
                payWaySelectDialog.selectPayWayBean = rechargeAdapter.getPayInfo()
            }
            payWaySelectDialog.isVip = isVip
            payWaySelectDialog.setContext(this)
            payWaySelectDialog.setPayment(paymentList)
            payWaySelectDialog.show(supportFragmentManager, "")
            payWaySelectDialog.setOnSelectionClickListener {
                vipPayInfoBean = it
                payCode = it.pay_type
                when (it.pay_type) {
                    PayDialog.WECHAT, PayDialog.ALIPAY, PayDialog.LE_SHUA_PAY, PayDialog.WECHAT_H5_PAY, PayDialog.TAI_SHAN_PAY -> {
                        mViewModel.createRechargeOrder(
                            it.vip_id, it.pay_type,
                            it.type, it.vip_goods_id
                        )
                    }

                    "sand_alipay" -> {
                        mViewModel.payNobleOrder(
                            "sand_alipay",
                            it.vip_goods_id,
                            -1,
                            "xinyu://xinyu.vip",
                            it.type,
                            it.vip_id
                        )
                    }

                    "sand_alipay_v4" -> {
                        mViewModel.payNobleOrder(
                            "sand_alipay_v4",
                            it.vip_goods_id,
                            -1,
                            "xinyu://xinyu.vip",
                            it.type,
                            it.vip_id
                        )
                    }

                    "sand_wechat" -> {
                        mViewModel.payNobleOrder(
                            "sand_wechat",
                            it.vip_goods_id,
                            -1,
                            "xinyu://xinyu.vip",
                            it.type,
                            it.vip_id
                        )
                    }

                    "sand_wechat_v4" -> {
                        mViewModel.payNobleOrder(
                            "sand_wechat_v4",
                            it.vip_goods_id,
                            -1,
                            "xinyu://xinyu.vip",
                            it.type,
                            it.vip_id
                        )
                    }
                }
            }
        }

        mBinding.btDailyGif.setViewClickListener(1) {
            var vipDailyGifDialog = VipDailyGifDialog()

            vipDailyGifDialog.tantaVipInfoBean = vipInfo
            vipDailyGifDialog.show(supportFragmentManager, "")
            vipDailyGifDialog.setOnSelectionClickListener {
                mViewModel.getVipDailyGif()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getVipIndexInfo()
        mViewModel.payConfig()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent!!.data
        if (uri != null) {
            val payCode = uri.getQueryParameter("payCode") // 支付宝支付完后返回app后 所传的code
            Log.e("onNewIntent", "payCode:$payCode") //  2为成功
            paySuccess()
        }
    }

    /**
     * 跳转小程序支付
     */
    private fun jumpToWechatApplet(payBean: TantaPayBean) {
        //var appId: String = "wxd930ea5d5a258f4f"; // 填移动应用(App)的 AppId，非小程序的 AppID
        var appId: String = WECHAT_APP_ID; // 填移动应用(App)的 AppId，非小程序的 AppID
        var api: IWXAPI = WXAPIFactory.createWXAPI(this@VipTantaCenterActivity, appId);
        var req: WXLaunchMiniProgram.Req = WXLaunchMiniProgram.Req();
        req.userName = payBean.applet.wechatOriginalId; // 填小程序原始id
        req.path =
            payBean.applet.wechatAppletPath; //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRechargeEvent(event: PayResultEvent) {
        if (event.isSuccess) {
            toast("充值成功")
            paySuccess()
        }
    }

    private fun paySuccess() {
        mViewModel.getVipIndexInfo()
        ARouter.getInstance()
            .build(RouteUrl.Vip.VipTantaMemberOpenSucceedActivity)
            .withBoolean(RouteKey.TYPE_OPEN_SUCCEED, isVip)
            .navigation()
    }
}