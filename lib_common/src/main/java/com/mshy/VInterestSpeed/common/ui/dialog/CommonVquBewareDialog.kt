package com.mshy.VInterestSpeed.common.ui.dialog

import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogBewareBinding
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
/**
 * author: Tany
 * date: 2022/5/2
 * description:官方防诈骗弹框
 */
class CommonVquBewareDialog : BaseDialogFragment<CommonVquDialogBewareBinding>(),
    View.OnClickListener {
    override fun CommonVquDialogBewareBinding.initView() {
        ivClose.setOnClickListener(this@CommonVquBewareDialog)
        ivBg.setOnClickListener(this@CommonVquBewareDialog)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }
            R.id.iv_bg -> {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                    .withString(RouteKey.URL,
                        "http://asset.zhenban.top/html/fraud.html")
                    .navigation()
                dismiss()
            }
        }
    }

}