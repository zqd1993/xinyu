package com.live.module.bill.activity

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.bill.R
import com.live.module.bill.bean.TypeBean
import com.live.module.bill.databinding.BillVquActivityNewWithdrawalAccountBinding
import com.live.module.bill.dialog.BillVquAccountTypeDialog
import com.live.module.bill.vm.BillTantaNewWithdrawalAccountViewModel
import com.live.vquonline.base.utils.ToastUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.html
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.CommonStringUtil
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaNewWithdrawalAccountActivity)
class BillTantaNewWithdrawalAccountActivity :
    BaseActivity<BillVquActivityNewWithdrawalAccountBinding, BillTantaNewWithdrawalAccountViewModel>() {
    override val mViewModel: BillTantaNewWithdrawalAccountViewModel by viewModels()

    private var mCarType = 1


    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0


    @Autowired(name = RouteKey.ID)
    @JvmField
    var id = "0"

    @Autowired(name = RouteKey.IS_MASTER)
    @JvmField
    var isMaster = 0

    private val mVquAccountTypeDialog by lazy {
        BillVquAccountTypeDialog().apply {
            setOnSelectionClickListener {
                changeType(it)
            }
        }
    }

    override fun initObserve() {
        mViewModel.accountInfo.observe(this) {

            if (type == 0) {
                mBinding.etBillVquNewWithdrawalAccountRealName.setText(it.cardName)
                mBinding.etBillVquNewWithdrawalAccountIdCard.setText(it.idCard)

            }


            if (!it.cardTypes.isNullOrEmpty()) {
                if (type == 0) {
                    changeType(it.cardTypes[0])
                } else {
                    for (typeBean in it.cardTypes) {
                        if (typeBean.type == it.cardType) {
                            changeType(typeBean)
                        }
                    }
                }
                mVquAccountTypeDialog.setTypes(it.cardTypes)
            }

            mBinding.etBillVquNewWithdrawalAccount.setText(it.cardAccount)
        }

        mViewModel.saveResult.observe(this) {
            if (it != "success") {
                val messageDialog = MessageDialog()
                messageDialog.setTitle("温馨提示")
                messageDialog.setContent(it)
                messageDialog.setIsSingleButton(true)
                messageDialog.setSingleText("好的")
                messageDialog.show(supportFragmentManager, "")
            } else {
//                EventBus.getDefault().post(BillVquAccountEvent())
                toast("绑定成功")
                finish()
            }
        }

        mViewModel.time.observe(this) {
            mBinding.stvBillVquNewWithdrawalAccountGetCode.text = it
            mBinding.stvBillVquNewWithdrawalAccountGetCode.isEnabled =
                it.equals(ResUtils.getString(R.string.login_send_auth_code))
        }

    }

    private fun changeType(type: TypeBean) {
        mCarType = type.type
//        if (type == 1) {
        mBinding.tvBillVquNewWithdrawalAccountTypeName.text = type.bank
        mBinding.ivBillVquNewWithdrawalAccountTypeImg.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + type.icon)
        mBinding.etBillVquNewWithdrawalAccount.hint = "请输入您的" + type.bank + "账号"
//        }
    }

    override fun initRequestData() {
        if (type == 1) {
            mViewModel.vquAccountInfo(id, isMaster)
        } else {
            mViewModel.vquAccountInfo(id, 1)
        }
    }

    override fun BillVquActivityNewWithdrawalAccountBinding.initView() {
        mBinding.tbBillVquNewWithdrawalAccountBar.toolbar.initClose(
            "绑定提现账号"
        ) {
            finish()
        }


        if (type == 1) {
            mBinding.clBillVquNewWithdrawalAccountPhoneParent.visibility = View.VISIBLE
        } else {
            mBinding.clBillVquNewWithdrawalAccountPhoneParent.visibility = View.GONE
            mBinding.etBillVquNewWithdrawalAccountIdCard.isEnabled = false
            mBinding.etBillVquNewWithdrawalAccountRealName.isEnabled = false
        }

//        mBinding.tvBillVquNewWithdrawalAccountTypeName.text = "支付宝"
//        mBinding.ivBillVquNewWithdrawalAccountTypeImg.setImageResource(R.mipmap.ic_common_vqu_pay_dialog_alipay_logo)
//        mBinding.etBillVquNewWithdrawalAccount.hint = "请输入您的支付宝账号"

        mBinding.tvBillVquNewWithdrawalAccountTips.html(getString(R.string.vqu_bill_new_withdrawal_account_tips))

        initEvent()
    }

    private fun initEvent() {

        mBinding.llBillVquNewWithdrawalAccountTypeParent.setViewClickListener(200) {
            mVquAccountTypeDialog.show(supportFragmentManager, "")
        }


        mBinding.etBillVquNewWithdrawalAccountRealName.addTextChangedListener {
            refreshSubmitStatus()
        }

        mBinding.etBillVquNewWithdrawalAccountIdCard.addTextChangedListener {
            refreshSubmitStatus()
        }

        mBinding.etBillVquNewWithdrawalAccount.addTextChangedListener {
            refreshSubmitStatus()
        }

        mBinding.etBillVquNewWithdrawalAccountPhone.addTextChangedListener {
            refreshSubmitStatus()
        }

        mBinding.etBillVquNewWithdrawalAccountAuthCode.addTextChangedListener {
            refreshSubmitStatus()
        }

        mBinding.stvBillVquNewWithdrawalAccountCommit.setViewClickListener {
            clickCommit()
        }

        mBinding.stvBillVquNewWithdrawalAccountGetCode.setViewClickListener {
            clickSendCode()
        }

    }

    private fun clickSendCode() {

        val phone = mBinding.etBillVquNewWithdrawalAccountPhone.text.toString()

        mViewModel.sendCode(phone)
    }

    private fun refreshSubmitStatus() {
        val account = mBinding.etBillVquNewWithdrawalAccount.text
        val idCard = mBinding.etBillVquNewWithdrawalAccountIdCard.text
        val name = mBinding.etBillVquNewWithdrawalAccountRealName.text
        if (type == 1) {
            val phone = mBinding.etBillVquNewWithdrawalAccountPhone.text
            val authCode = mBinding.etBillVquNewWithdrawalAccountAuthCode.text

            if (!account.isNullOrEmpty() && phone.length == 11 && !authCode.isNullOrEmpty() && !idCard.isNullOrEmpty() && !name.isNullOrEmpty()) {
                mBinding.stvBillVquNewWithdrawalAccountCommit.setStartColor(
                    ResUtils.getColor(R.color.color_FF7AC2), ResUtils.getColor(R.color.color_FF7AC2)
                )
            } else {
                mBinding.stvBillVquNewWithdrawalAccountCommit.setStartColor(
                    ResUtils.getColor(R.color.color_D3D1D7), ResUtils.getColor(R.color.color_D3D1D7)
                )
            }
        } else {
            if (!account.isNullOrEmpty() && !idCard.isNullOrEmpty() && !name.isNullOrEmpty()) {
                mBinding.stvBillVquNewWithdrawalAccountCommit.setStartColor(
                    ResUtils.getColor(R.color.color_FF7AC2), ResUtils.getColor(R.color.color_FF7AC2)
                )
            } else {
                mBinding.stvBillVquNewWithdrawalAccountCommit.setStartColor(
                    ResUtils.getColor(R.color.color_D3D1D7), ResUtils.getColor(R.color.color_D3D1D7)
                )
            }
        }
    }

    private fun clickCommit() {
        val account = mBinding.etBillVquNewWithdrawalAccount.text.toString().trim()
        val phone = mBinding.etBillVquNewWithdrawalAccountPhone.text.toString().trim()
        val phoneCode = mBinding.etBillVquNewWithdrawalAccountAuthCode.text.toString().trim()
        val idCard = mBinding.etBillVquNewWithdrawalAccountIdCard.text.toString().trim()
        val realName = mBinding.etBillVquNewWithdrawalAccountRealName.text.toString().trim()
//        if (mCarType == 1) {
//            val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
//            val phoneRegex = "^[1][3,4,5,7,8,9][0-9]{9}\$"
//            if (!Pattern.matches(phoneRegex, account) && !Pattern.matches(emailRegex, account)) {
//                ToastUtils.showShort("请输入正确的支付宝账号")
//                return
//            }
//        } else if(mCarType == 3){
//            if (!CommonStringUtil.checkBankCard(account)) {
//                ToastUtils.showShort("请输入正确的银行卡账号")
//                return
//            }
//        }
        mViewModel.vquAccountSave(
            account,
            mCarType,
            isMaster,
            phone,
            phoneCode,
            id,
            idCard,
            realName,
            type
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (window.superDispatchTouchEvent(ev)) {
            return true
        }
        return onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight();
            val right = left + v.getWidth();

            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

}