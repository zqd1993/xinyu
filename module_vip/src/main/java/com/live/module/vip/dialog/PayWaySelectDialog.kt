package com.live.module.vip.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.bill.adapter.BillTantaPaymentAdapter
import com.live.module.vip.R
import com.live.module.vip.adapter.VipPayWayAdapter
import com.live.module.vip.adapter.VipRechargeDialogAdapter
import com.live.module.vip.bean.VipPayInfoBean
import com.live.module.vip.bean.VipPayWayBean
import com.live.module.vip.bean.VipRechargeBean
import com.live.module.vip.databinding.VipTantaDialogPayWayBinding
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.RechargeRoute
import com.mshy.VInterestSpeed.common.constant.ADAPAY_WECHAT_APPLET
import com.mshy.VInterestSpeed.common.constant.ALIPAY
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.WECHAT
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog
import com.mshy.VInterestSpeed.common.utils.LoginUtils


class PayWaySelectDialog : BaseDialogFragment<VipTantaDialogPayWayBinding>() {
    private var mVquOnClick: ((payType: VipPayInfoBean) -> Unit)? = null
    public var selectPayWayBean: VipPayInfoBean? = null

    private val mPayData = mutableListOf<VipPayWayBean>()
    public var rechargeData = mutableListOf<VipRechargeBean>()
    private val rechargeAdapter = VipRechargeDialogAdapter()
    private val payWayAdapter = VipPayWayAdapter()

    private val mPaymentAdapter = BillTantaPaymentAdapter()

    private val mAliPaymentAdapter = BillTantaPaymentAdapter()
    var isVip = false
    var mContext: Context? = null

    private var rechargeRoute: RechargeRoute? = null

    private var paymentData: MutableList<BillPaymentData> = mutableListOf()

    private var wechatRechargeRoute: MutableList<RechargeRoute> = mutableListOf()

    private var aliRechargeRoute: MutableList<RechargeRoute> = mutableListOf()

    fun setContext(context: Context) {
        mContext = context;
    }

    fun setPayment(paymentData: MutableList<BillPaymentData>) {
        this.paymentData = paymentData
    }

    override fun VipTantaDialogPayWayBinding.initView() {
        mPayData.clear()
//        rechargeData.clear()
        if (isVip) {
            mBinding.rvRecharge.adapter = rechargeAdapter
            rechargeAdapter.setNewInstance(rechargeData)
            mBinding.llMoney.visibility = View.GONE
            mBinding.rvRecharge.visibility = View.VISIBLE
        } else {
            com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("money000=${selectPayWayBean?.money}")
            mBinding.tvPrice.text = selectPayWayBean?.money
            mBinding.llMoney.visibility = View.VISIBLE
            mBinding.rvRecharge.visibility = View.GONE
        }

        mBinding.rvRecharge.adapter = rechargeAdapter

        var wechatPay = VipPayWayBean()
        wechatPay.name = "微信支付"
        wechatPay.nameLabel = PayDialog.WECHAT
        wechatPay.icon = R.mipmap.ic_common_vqu_pay_dialog_wechat_logo
        var aliPay = VipPayWayBean()
        aliPay.name = "支付宝支付"
        aliPay.nameLabel = PayDialog.ALIPAY
        aliPay.icon = R.mipmap.ic_common_vqu_pay_dialog_alipay_logo
        //默认微信支付
        payWayAdapter.selectIndex = 0

        mPayData.add(wechatPay)
        mPayData.add(aliPay)
//        mPayData.add(aliPay)//先隐藏支付宝支付
        mBinding.rvPayWay.adapter = payWayAdapter
        payWayAdapter.setNewInstance(mPayData)

        mBinding.ivClose.setViewClickListener(200) {
            dismiss()
        }
        mBinding.btPay.setViewClickListener(200) {
            var selectWay = mPayData[payWayAdapter.selectIndex]
            if (isVip) {
                selectPayWayBean = rechargeAdapter.getPayInfo()
            }
            selectPayWayBean?.let {

                if (rechargeRoute == null) {
                    toast("没有有效的支付方式")
                    return@let
                }
                it.rechargeRoute = rechargeRoute
                it.pay_type = it.rechargeRoute!!.payCode
                mVquOnClick?.invoke(it)
            }
            dismiss()
        }
        mBinding.llVquWechatPay.setViewClickListener(0) {
            if (wechatRechargeRoute.size == 1) {
                rechargeRoute = wechatRechargeRoute[0]
                mBinding.cbWechatType.isChecked = true
                mBinding.cbAliType.isChecked = false
                for (paymentData in paymentData) {
                    for (rechargeRoute in paymentData.rechargeRoute) {
                        rechargeRoute.checked = false
                    }
                }
                if (mPaymentAdapter.data.size > 0) {
                    mPaymentAdapter.notifyDataSetChanged()
                }
                if (mAliPaymentAdapter.data.size > 0) {
                    mAliPaymentAdapter.notifyDataSetChanged()
                }
            }
        }

        mBinding.llVquAliPay.setViewClickListener(0) {
            if (aliRechargeRoute.size == 1) {
                rechargeRoute = aliRechargeRoute[0]
                mBinding.cbWechatType.isChecked = false
                mBinding.cbAliType.isChecked = true
                for (paymentData in paymentData) {
                    for (rechargeRoute in paymentData.rechargeRoute) {
                        rechargeRoute.checked = false
                    }
                }
                if (mPaymentAdapter.data.size > 0) {
                    mPaymentAdapter.notifyDataSetChanged()
                }
                if (mAliPaymentAdapter.data.size > 0) {
                    mAliPaymentAdapter.notifyDataSetChanged()
                }
            }
        }
        mBinding.tvAgreement.setOnClickListener {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(
                    RouteKey.URL,
                    NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.VIP_URL
                )
                .navigation()
        }

        initAdapter()
    }

    private fun initAdapter() {
        var isChecked = false
        for (payment in paymentData) {
            for (rechargeRoute in payment.rechargeRoute) {
                rechargeRoute.checked = false
            }
        }
        for (payment in paymentData) {
            if (!isChecked && payment.rechargeRoute.size > 0) {
                payment.rechargeRoute[0].checked = true
                rechargeRoute = payment.rechargeRoute[0]
                isChecked = true
            }
            if (payment.payType == "wechat") {
                wechatRechargeRoute = payment.rechargeRoute
                if (wechatRechargeRoute.size > 0) {
                    mBinding.llVquWechatPay.visibility = View.VISIBLE
                    if (wechatRechargeRoute.size > 1) {
                        paymentAdapter(mBinding.rvWechatPay, mPaymentAdapter, wechatRechargeRoute)
                        mBinding.cbWechatType.visibility = View.GONE
                    } else {
                        mBinding.cbAliType.visibility = View.VISIBLE
                    }
                }
            } else {
                aliRechargeRoute = payment.rechargeRoute
                if (aliRechargeRoute.size > 0) {
                    mBinding.llVquAliPay.visibility = View.VISIBLE
                    if (aliRechargeRoute.size > 1) {
                        paymentAdapter(mBinding.rvAliPay, mAliPaymentAdapter, aliRechargeRoute)
                        mBinding.cbAliType.visibility = View.GONE
                    } else {
                        mBinding.cbAliType.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun paymentAdapter(
        recyclerView: RecyclerView,
        adapter: BillTantaPaymentAdapter,
        dataList: MutableList<RechargeRoute>
    ) {
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
        adapter.setNewInstance(dataList)
        recyclerView.layoutManager = GridLayoutManager(mContext, 3)
        adapter.setOnItemClickListener { _, _, position ->

            val item = adapter.getItemOrNull(position) ?: return@setOnItemClickListener
            rechargeRoute = item
            for (paymentData in paymentData) {
                for (rechargeRoute in paymentData.rechargeRoute) {
                    rechargeRoute.checked = rechargeRoute.payCode == item.payCode
                }
            }
            if (rechargeRoute!!.payCode.contains("wechat")) {
                mBinding.cbAliType.isChecked = false
            } else {
                mBinding.cbWechatType.isChecked = false
            }
            mPaymentAdapter.notifyDataSetChanged()
            mAliPaymentAdapter.notifyDataSetChanged()
        }
    }


    override fun initWindow() {
        mCancelAble = false
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }


    fun setOnSelectionClickListener(onClick: (payType: VipPayInfoBean) -> Unit) {
        mVquOnClick = onClick
    }

}