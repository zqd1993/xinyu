package com.live.module.anchor.dialog

import com.live.module.anchor.databinding.AnchorVquDialogFateLinkBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
class FateLinkDialog : BaseDialogFragment<AnchorVquDialogFateLinkBinding>() {
    override fun AnchorVquDialogFateLinkBinding.initView() {
        mBinding.tvConfirm.setViewClickListener {
            dismiss()
        }
    }
}