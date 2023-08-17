package com.mshy.VInterestSpeed.common.ui.dialog

import android.view.View
import androidx.fragment.app.DialogFragment
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogCommonDetainmentBinding
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.UserManager

/**
 * author: Tany
 * date: 2022/7/4
 * description:挽留弹框
 */
class CommonVquDetainmentDialog : BaseDialogFragment<CommonVquDialogCommonDetainmentBinding>(),
    View.OnClickListener {
    var myRecentContact: RecentContact? = null
    private var mClickListener: OnClickListener? = null

    override fun CommonVquDialogCommonDetainmentBinding.initView() {
        if (myRecentContact == null) {
            dismissAllowingStateLoss()
            return
        }
        val nimInfo = com.mshy.VInterestSpeed.uikit.api.NimUIKit.getUserInfoProvider().getUserInfo(myRecentContact?.contactId)
        if (nimInfo != null) {
            mBinding.ivHead?.vquLoadCircleImage(NetBaseUrlConstant.IMAGE_URL + if (nimInfo.avatar.isNullOrEmpty()) {
                ""
            } else {
                nimInfo.avatar
            }, R.mipmap.ic_common_head_circle_def)
            mBinding?.tvTitle?.text = nimInfo.name
        }
        var userInfo = UserManager.userInfo
        mBinding.tvContent.text = if (userInfo?.gender == 1) {
            "小哥哥发来新消息，确定不去看看嘛？"
        } else {
            "小姐姐发来新消息，确定不去看看嘛？"
        }
        mBinding.tvLeft.setOnClickListener {
            mClickListener?.onLeft(this@CommonVquDetainmentDialog)
        }
        mBinding.tvRight.setOnClickListener {
            mClickListener?.onRight(this@CommonVquDetainmentDialog)
        }
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.mClickListener = clickListener
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }
        }
    }

    interface OnClickListener {
        fun onLeft(dialogFragment: DialogFragment?)
        fun onRight(dialogFragment: DialogFragment?)
    }

    fun setData(recentContact: RecentContact?) {
        myRecentContact = recentContact
    }
}