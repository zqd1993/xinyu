package com.live.module.contact.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.live.module.contact.databinding.ContactVquDialogSortBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment


/**
 * author: Lau
 * date: 2022/6/24
 * description:
 */
class ContactVquSortDialog : BaseDialogFragment<ContactVquDialogSortBinding>() {

    private var mVquListener: ((type: String) -> Unit)? = null

    companion object {
        const val USER_ID = "user_id"
        const val CONTRIBUTION = "contribution"
    }

    override fun ContactVquDialogSortBinding.initView() {
        mBinding.tvContactVquSortCancel.setViewClickListener {
            dismiss()
        }

        mBinding.tvContactVquSortContribution.setViewClickListener {
            mVquListener?.invoke(CONTRIBUTION)
            dismiss()
        }

        mBinding.tvContactVquSortTime.setViewClickListener {
            mVquListener?.invoke(USER_ID)
            dismiss()
        }
    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }

    fun vquSetOnSortTypeClickListener(listener: (type: String) -> Unit) {
        mVquListener = listener
    }
}