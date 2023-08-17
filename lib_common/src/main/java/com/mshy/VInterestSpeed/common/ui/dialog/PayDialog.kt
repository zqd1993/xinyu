package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.viewModels
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.StateLayoutEnum
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogPayBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseVMDialogFragment
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.PayUtils

/**
 * author: Lau
 * date: 2022/4/15
 * description:
 */
class PayDialog : BaseVMDialogFragment<CommonVquDialogPayBinding, CommonPayViewModel>() {

    private var mVquCommonOnPayListener: ((type: String) -> Unit)? = null

    private var mVquPrice = ""


    private var mType = WECHAT

    private var mPayId = ""

    private var mPayButtonTextColor: Int = 0
    private var mPayButtonColor: Int = 0
    private var mPayButtonStartColor: Int = 0
    private var mPayButtonEndColor: Int = 0


    companion object {
        @JvmStatic
        val ALIPAY = "alipay"

        @JvmStatic
        val WECHAT = "wechat"

        @JvmStatic
        val WECHAT_APPLET = "wechat_applet"
    }

    private val loadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(requireActivity(), "支付请求中...")
    }


    fun setOnPayListener(listener: ((type: String) -> Unit)?) {
        mVquCommonOnPayListener = listener
    }

    /**
     * 设置价格，用于展示
     */
    fun setPrice(price: String) {
        mVquPrice = price
    }

    /**
     * 设置支付的ID，用于调用充值接口
     */
    fun setRechargeID(id: String) {
        mPayId = id
    }

    override val mViewModel: CommonPayViewModel by viewModels()

    override fun CommonVquDialogPayBinding.initView() {
        mBinding.tvVquPayPrice.text = mVquPrice

        initPayButton()

        initEvent()
    }

    private fun initPayButton() {
        if (mPayButtonStartColor != 0 && mPayButtonEndColor != 0) {
            mBinding.tvPay.setStartColor(mPayButtonStartColor, mPayButtonEndColor)
        } else if (mPayButtonTextColor != 0) {
            mBinding.tvPay.solidColor = mPayButtonColor
        }

        if (mPayButtonTextColor != 0) {
            mBinding.tvPay.setTextColor(mPayButtonColor)
        }
    }

    private fun initEvent() {
        mBinding.llTantaAlipayPay.clickDelay {
            mBinding.cbTantaWechat.isChecked = false
            mBinding.cbTantaAlipay.isChecked = true
            mType = ALIPAY
        }

        mBinding.llTantaWechatPay.clickDelay {
            mBinding.cbTantaWechat.isChecked = true
            mBinding.cbTantaAlipay.isChecked = false
            mType = WECHAT
        }

        mBinding.ivVquPayFinish.setViewClickListener {
            dismiss()
        }

        mBinding.tvPay.clickDelay {
            if (mType.isEmpty()) {
                toast(R.string.common_vqu_select_pay_way_tips)
                return@clickDelay
            }

            if (mPayId.isEmpty()) {
                toast(R.string.common_vqu_select_recharge_tips)
                return@clickDelay
            }

            mViewModel.recharge(mType, mPayId)

        }
    }

    override fun initObserve() {
        mViewModel.payData.observe(this) {
            if (mType == WECHAT) {
                PayUtils.wechatPay(requireActivity(), it)
            } else if (mType == ALIPAY) {
                PayUtils.aliPay(requireActivity(), it.payinfo)
            }

            dismiss()
        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> loadingDialog.show()
                else -> loadingDialog.dismiss()
            }
        }
    }

    override fun initRequestData() {

    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /**
     * 设置左边按钮颜色
     */
    fun setPayButtonTextColor(@ColorInt color: Int) {
        mPayButtonColor = color
    }

    /**
     * 设置左边按钮颜色
     */
    fun setPayButtonColor(@ColorInt color: Int) {
        mPayButtonColor = color
    }

    /**
     * 设置左边按钮颜色，渐变色
     */
    fun setPayButtonColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        mPayButtonStartColor = startColor
        mPayButtonEndColor = endColor
    }
}