package com.live.module.info.fragment

import android.view.View
import android.widget.TextView
import com.live.module.info.R
import com.mshy.VInterestSpeed.common.ui.dialog.BaseDialogFragment

class AuthCommitAgainDialog : BaseDialogFragment() {
    var myListener: SelectListener? = null
    override fun getLayoutId(): Int = R.layout.info_dialog_auth_commit_again
    override fun initView(view: View?) {
       var  tvCommit=view?.findViewById<TextView>(R.id.tv_commit)
       var  tvAgain=view?.findViewById<TextView>(R.id.tv_again)
       var  tvCancel=view?.findViewById<TextView>(R.id.tv_cancel)
        tvCommit?.setOnClickListener {
            myListener?.commit()
            dismiss()
        }
        tvAgain?.setOnClickListener {
            myListener?.again()
            dismiss()
        }
        tvCancel?.setOnClickListener {
            myListener?.cancel()
            dismiss()
        }

    }

    fun setOnSelectListener(listener: SelectListener) {
        myListener = listener
    }

    interface SelectListener {
        fun again()
        fun commit()
        fun cancel()
    }
}