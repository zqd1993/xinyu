package com.live.module.vip.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.vip.R
import com.live.module.vip.adapter.VipPayWayAdapter
import com.live.module.vip.adapter.VipRechargeDialogAdapter
import com.live.module.vip.bean.VipPayInfoBean
import com.live.module.vip.bean.VipPayWayBean
import com.live.module.vip.bean.VipRechargeBean
import com.live.module.vip.databinding.VipTantaDialogPayWayBinding
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog


class PayWaySelectDialog : BaseDialogFragment<VipTantaDialogPayWayBinding>() {
    private var mVquOnClick: ((payType: VipPayInfoBean) -> Unit)? = null
    public var selectPayWayBean: VipPayInfoBean? = null

    private val mPayData = mutableListOf<VipPayWayBean>()
    public var rechargeData = mutableListOf<VipRechargeBean>()
    private val rechargeAdapter = VipRechargeDialogAdapter()
    private val payWayAdapter = VipPayWayAdapter()
    var isVip = false


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
                it.pay_type = selectWay.nameLabel
                mVquOnClick?.invoke(it)
            }
            dismiss()
        }
        mBinding.tvAgreement.setOnClickListener {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(
                    RouteKey.URL,
                    NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.VIP_URL
                )
                .navigation()
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