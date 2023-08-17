package com.live.module.bill.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.bill.R
import com.live.module.bill.bean.TypeBean
import com.live.module.bill.databinding.BillVquDialogAccountTypeBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment

/**
 * author: Lau
 * date: 2022/5/20
 * description:
 */
class BillVquAccountTypeDialog : BaseDialogFragment<BillVquDialogAccountTypeBinding>() {
    private var mVquOnClick: ((type: TypeBean) -> Unit)? = null

    private val mVquTypes = mutableListOf<TypeBean>()

    private val mAdapter = TypeAdapter()

    override fun BillVquDialogAccountTypeBinding.initView() {
//        mBinding.tvBillVquAccountTypeAlipay.setViewClickListener(200) {
//            mVquOnClick?.invoke(1)
//            dismiss()
//        }

        mBinding.rvVquBillAccountTypeList.adapter = mAdapter
        mAdapter.setNewInstance(mVquTypes)

        mBinding.tvBillVquAccountTypeCancel.setViewClickListener(200) {
            dismiss()
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            val typeBean = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            mVquOnClick?.invoke(typeBean)
            dismiss()
        }
    }

    override fun initWindow() {
//        super.initWindow()

        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }


    fun setOnSelectionClickListener(onClick: (type: TypeBean) -> Unit) {
        mVquOnClick = onClick
    }

    private inner class TypeAdapter :
        BaseQuickAdapter<TypeBean, BaseViewHolder>(R.layout.bill_vqu_item_account_type) {
        override fun convert(holder: BaseViewHolder, item: TypeBean) {
            holder.setText(R.id.tv_bill_vqu_account_type_alipay, item.bank)
        }

    }

    fun setTypes(types: MutableList<TypeBean>) {
        mVquTypes.clear()
        mVquTypes.addAll(types)
    }
}