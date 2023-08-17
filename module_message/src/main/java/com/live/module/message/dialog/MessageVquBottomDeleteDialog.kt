package com.live.module.message.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.live.module.message.databinding.MessageTantaDialogBottomDeleteBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment

/**
 * author: Lau
 * date: 2022/7/4
 * description:
 */
class MessageVquBottomDeleteDialog() : BaseDialogFragment<MessageTantaDialogBottomDeleteBinding>() {

    private var mListener: (() -> Unit)? = null

    override fun MessageTantaDialogBottomDeleteBinding.initView() {
        mBinding.tvMessageVquBottomDelete.setViewClickListener {
            dismiss()
            mListener?.invoke()
        }

        mBinding.tvMessageVquBottomDeleteCancel.setViewClickListener {
            dismiss()
        }
    }

    fun setOnDeleteClickListener(listener: (() -> Unit)?) {
        mListener = listener
    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }

}