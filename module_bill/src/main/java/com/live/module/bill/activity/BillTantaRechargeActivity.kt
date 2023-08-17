package com.live.module.bill.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.bill.R
import com.live.module.bill.adapter.BillTantaRechargeAdapter
import com.live.module.bill.bean.BillTantaWechatPayTypeData
import com.live.module.bill.databinding.BillTantaActivityRechargeBinding
import com.live.module.bill.vm.TantaRechargeViewModel
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.bean.CommonTantaRechargeOptions
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.WebUrl
import com.mshy.VInterestSpeed.common.constant.*
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.BottomGiftFragmentDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonFirstRechargeDialog
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog
import com.mshy.VInterestSpeed.common.ui.dialog.RechargePayPopWindow
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView
import com.mshy.VInterestSpeed.common.utils.*
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author: Lau
 * date: 2022/4/2
 * description:充值页面
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaRechargeActivity)
@EventBusRegister
class BillTantaRechargeActivity :
    BaseActivity<BillTantaActivityRechargeBinding, TantaRechargeViewModel>() {

    override val mViewModel: TantaRechargeViewModel by viewModels()

    private val mAdapter = BillTantaRechargeAdapter()

    private var mSelectedPosition = -1

    private var mTantaWebUrl: WebUrl? = null

    private var mPayType = WECHAT

    //是否首充
    private var mVquIsActivity = false
    private var wechatPayType: BillTantaWechatPayTypeData? = null

    private var itemRecharge: CommonTantaRechargeOptions? = null

    private val tantaPayDialog by lazy {
        PayDialog()
    }

    private val loadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "支付请求中...")
    }
    private val firstRechargeDialog by lazy {
        CommonFirstRechargeDialog().apply {
            onVquRechargeClickListener {
                dismiss()
                return@onVquRechargeClickListener true
            }
        }
    }

    override fun BillTantaActivityRechargeBinding.initView() {

        mBinding.tvTantaBillRechargeDiamondNum.typeface = FontsFamily.tfDDinExpBold

        UmUtils.setUmEvent(
            this@BillTantaRechargeActivity,
            UmUtils.ENTERRECHARGEPAGE
        )
        mBinding.tbTantaBillBar.toolbar.initClose(
            getString(R.string.vqu_bill_balance),
            getString(R.string.vqu_bill_detail),
            onBack = {
                finish()
            },
            backImg = R.mipmap.ic_back_white,
            onClickRight = {
                ARouter.getInstance().build(RouteUrl.Bill.BillTantaDetailActivity).navigation()
            },
            backgroundColor = R.color.transparent,
            rightColor = R.color.white,
        )
        mBinding.tbTantaBillBar.toolbar.findViewById<ShapeTextView>(R.id.tv_right).solidColor =
            ResUtils.getColor(R.color.transparent)
        mBinding.tbTantaBillBar.tvTitle.setTextColor(ResUtils.getColor(R.color.white))
        val height = BarUtils.getStatusBarHeight()
        val layoutParams =
            mBinding.tbTantaBillBar.toolbar.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin += height

        mBinding.stvBillTantaRechargePay.clickDelay {

            clickPay()
//            vquPayDialog.setPrice(item.price)
//            vquPayDialog.setRechargeID(item.id.toString())
//            vquPayDialog.show(supportFragmentManager, "")

        }




        mBinding.llTantaWechatPay.setViewClickListener(0) {
            mPayType = if (wechatPayType?.payType == 5) {
                ADAPAY_WECHAT_APPLET
            } else {
                WECHAT
            }
            LoginUtils.equals("mPayType=$mPayType")
            mBinding.cbTantaWechat.isChecked = true
            mBinding.cbTantaAlipay.isChecked = false
        }

        mBinding.llTantaAlipayPay.setViewClickListener(0) {
            mPayType = ALIPAY
            mBinding.cbTantaWechat.isChecked = false
            mBinding.cbTantaAlipay.isChecked = true
        }
        mBinding.vFirstRechargeBanner.setViewClickListener(0) {

            firstRechargeDialog.show(supportFragmentManager, "")
        }

        initAdapter()

        initAgreement()
    }

    override fun setStatusBar() {
        //设置共同沉浸式样式
//        ImmersionBar.with(this)
//            .statusBarColor(com.mshy.VInterestSpeed.common.R.color.base_colorPrimary)
//            .statusBarDarkFont(true)
//            .navigationBarColor(com.mshy.VInterestSpeed.common.R.color.base_colorPrimary).init()

        ImmersionBar.with(this).transparentStatusBar().init()
    }

    private fun clickPay() {
//        val rechargePayPopWindow =
//            RechargePayPopWindow()
//        rechargePayPopWindow.show(supportFragmentManager)
        if (mSelectedPosition < 0) {
            toast(R.string.common_vqu_select_recharge_tips)
            return
        }

        itemRecharge = mAdapter.getItemOrNull(mSelectedPosition)
        if (itemRecharge == null) {
            toast(R.string.common_vqu_select_recharge_data_error_tips)
            return
        }

        if (mPayType.isEmpty()) {
            toast(R.string.common_vqu_select_pay_way_tips)
            return
        }

        mViewModel.rechargeWarning(true)
        RiskControlUtil.getToken(this@BillTantaRechargeActivity, 5)

    }

    private fun payTask() {
        UmUtils.setUmEvent(
            this@BillTantaRechargeActivity,
            UmUtils.RECHARGECLICK
        )

        if (mPayType == ALIPAY) {
            mViewModel.createRechargeOrder(mPayType, itemRecharge!!.id, -1)
        } else {
            mViewModel.getWechatPayType(1)
        }
    }

    private fun initAdapter() {
        (mBinding.rvTantaBillRechargeOptionsList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        mBinding.rvTantaBillRechargeOptionsList.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->

            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            if (position != mSelectedPosition) {
                item.isSelected = true
                mAdapter.notifyItemChanged(position)

                val lastItem = mAdapter.getItemOrNull(mSelectedPosition)
                if (lastItem != null) {
                    lastItem.isSelected = false
                    mAdapter.notifyItemChanged(mSelectedPosition)
                }
            }

            mSelectedPosition = position
        }
    }

    private fun initAgreement() {

        val agreement = getString(R.string.vqu_bill_recharge_agreement)
        val spannedHtml = TextUtils.getSpannedHtml(agreement)
        val spannableStringBuilder =
            TextUtils.getSpannableStringBuilder(spannedHtml)
        val urlSpans = TextUtils.getURLSpans(
            spannableStringBuilder,
            spannedHtml
        )
        urlSpans.forEach {
            TextUtils.setLinkClickable(
                spannableStringBuilder,
                it,
                RechargeClickableSpan(it.url)
            )
        }

        mBinding.tvBillTantaRechargeAgreement.movementMethod = LinkMovementMethod.getInstance()
        mBinding.tvBillTantaRechargeAgreement.text = spannableStringBuilder
    }

    override fun initObserve() {
        //充值列表
        mViewModel.rechargeOptionsListResultData.observe(this) {
            mAdapter.setNewInstance(it.list)
        }

        //钱包首页数据
        mViewModel.walletIndexResultData.observe(this) {
            mTantaWebUrl = it.webUrl
            mBinding.tvTantaBillRechargeDiamondNum.text = it.account.coin
        }

        mViewModel.defaultPositionData.observe(this) {
            mSelectedPosition = it
        }

        //创建订单数据
        mViewModel.rechargeOrderCreateResultData.observe(this) {
            com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("mPayType2222=$mPayType")
            if (mPayType == PayDialog.WECHAT) {
                PayUtils.wechatPay(this, it)
            } else if (mPayType == PayDialog.ALIPAY) {
                PayUtils.aliPay(this, it.payinfo)
            } else if (mPayType == wechatPayType?.payChannel.toString()) {
                jumpToWechatApplet(it)
            }

        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> loadingDialog.show()
                else -> loadingDialog.dismiss()
            }
        }
        //微信支付方式，原生or小程序
        mViewModel.wechatPayTypeResultData.observe(this) {
            wechatPayType = it
            if (wechatPayType?.payType == 5) {
                mPayType = wechatPayType?.payChannel.toString()
            } else {
                mPayType = WECHAT
            }

            LogUtil.e("mPayType111=$mPayType")
            var polling = -1
            if (wechatPayType != null) {
                polling = wechatPayType!!.polling
            }
            mViewModel.createRechargeOrder(mPayType, itemRecharge!!.id, polling)
//            LogUtil.e("wechatPayType=$wechatPayType")
        }
        //首冲弹框
        mViewModel.firstRechargeResultData.observe(this) {

            mVquIsActivity = it.isActivity ?: false

            //没有首冲
            if (!mVquIsActivity) {
                mBinding.vFirstRechargeBanner.visibility = View.VISIBLE

                val lastTime = SpUtils.getLong(SpKey.SHOWED_FIRST_RECHARGE_DIALOG_TIME) ?: 0L

                val time = System.currentTimeMillis()

                val lastTimeDate = DateUtils.getDateFormatString(lastTime, "yyyy-MM-dd")

                val timeDate = DateUtils.getDateFormatString(time, "yyyy-MM-dd")

                firstRechargeDialog.setList(it.list)

                if (lastTimeDate == timeDate) {
                    return@observe
                }
                SpUtils.putLong(SpKey.SHOWED_FIRST_RECHARGE_DIALOG_TIME, time)
//                val firstRechargeDialog = CommonFirstRechargeDialog()
//                firstRechargeDialog.onVquRechargeClickListener {
//
//                    firstRechargeDialog.dismiss()
//                    return@onVquRechargeClickListener true
//                }


//                firstRechargeDialog.showClose(false)
//                firstRechargeDialog.setCancelAble(false)
                firstRechargeDialog.show(supportFragmentManager, "")


            } else {
                mBinding.vFirstRechargeBanner.visibility = View.GONE
            }
        }

        mViewModel.warningData.observe(this) {

            if (it.isPop == 1 && !it.msg.isNullOrEmpty()) {
                val dialog = MessageDialog()
                dialog.setIsSingleButton(true)
                dialog.setContent(it.msg!!)
                dialog.setTitle("提示")
                dialog.setOnButtonClickListener(object : MessageDialog.OnSingleButtonClickListener {
                    override fun onClick(): Boolean {
                        if (it.isClickRechargeBtn) {
                            payTask()
                        }
                        return false
                    }
                })
                dialog.show(this@BillTantaRechargeActivity.supportFragmentManager, "")
            } else {
                if (it.isClickRechargeBtn) {
                    payTask()
                }
            }
        }
    }
    private var firstInto = true
    override fun onResume() {
        super.onResume()
        if (!firstInto) {
            mViewModel.rechargeWarning(false)
        }
        mViewModel.getWalletIndexData()
        mViewModel.getFirstRechargeInfo()
        mViewModel.getRechargeOptionsListData(2)
    }

    override fun initRequestData() {

        val time = SpUtils.getBoolean(SpKey.SHOWED_FIRST_RECHARGE_DIALOG_TIME)

//        if (isShowed != true) {

//        }

    }


    private inner class RechargeClickableSpan(val url: String) :
        MyClickSpan(url) {

        override fun onClick(widget: View) {

            when (url) {
                "#copy_official_account#" -> {
                    val cm: ClipboardManager =
                        this@BillTantaRechargeActivity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(
                        ClipData.newPlainText(
                            null,
                            mTantaWebUrl?.wxOfficial ?: "心语交友"
                        )
                    )
                    ToastUtils.showLong(R.string.vqu_bill_copy_success)
                }
                "#vqu_recharge_agreement#" -> {
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(
                            RouteKey.URL,
                            mTantaWebUrl?.rechargeAgreement
                                ?: (NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.RECHARGE_AGREEMENT)
                        )
                        .navigation()
                }
                "#vqu_juvenile_protection#" -> {
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(
                            RouteKey.URL,
                            mTantaWebUrl?.juvenileProtection
                                ?: (NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.JUVENILE_PROTECTION)
                        )
                        .navigation()
                }
            }

        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
        }
    }

    /**
     * 跳转小程序支付
     */
    private fun jumpToWechatApplet(payBean: TantaPayBean) {
        //var appId: String = "wxd930ea5d5a258f4f"; // 填移动应用(App)的 AppId，非小程序的 AppID
        com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("jumpToWechatApplet...payBean=$payBean")
        var appId: String = WECHAT_APP_ID; // 填移动应用(App)的 AppId，非小程序的 AppID
        var api: IWXAPI = WXAPIFactory.createWXAPI(this@BillTantaRechargeActivity, appId);
        var req: WXLaunchMiniProgram.Req = WXLaunchMiniProgram.Req();
        req.userName = payBean.applet.wechatOriginalId; // 填小程序原始id
        req.path =
            payBean.applet.wechatAppletPath; //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
//        api.handleIntent(getIntent(), this@BillVquRechargeActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRechargeEvent(event: PayResultEvent) {
        if (event.isSuccess) {
            toast("充值成功")
            tantaPayDialog.dismiss()
            mViewModel.getWalletIndexData()
            if (!mVquIsActivity) {
                mVquIsActivity = true
                mViewModel.getRechargeOptionsListData(2)
            }
        }
    }
}