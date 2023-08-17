package com.mshy.VInterestSpeed.common.ui.dialog

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.SpKey.KEY_NOTICE
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogNoticeBinding
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
/**
 * author: Tany
 * date: 2022/5/2
 * description:开启通知弹框
 */
class CommonVquNoticeDialog : BaseDialogFragment<CommonVquDialogNoticeBinding>(),
    View.OnClickListener {
    var globalApiService: GlobalApiService? = null
    override fun CommonVquDialogNoticeBinding.initView() {
        mBinding.tvOpen.setOnClickListener(this@CommonVquNoticeDialog)
        mBinding.tvNot.setOnClickListener(this@CommonVquNoticeDialog)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_open -> {//去开启
                val localIntent = Intent()
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts("package",
                        activity?.packageName,
                        null)
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.action = Intent.ACTION_VIEW
                    localIntent.setClassName("com.android.settings",
                        "com.android.settings.InstalledAppDetails")
                    localIntent.putExtra("com.android.settings.ApplicationPkgName",
                        activity?.packageName)
                }
                startActivity(localIntent)
                dismiss()
            }
            R.id.tv_not -> {//不再提示
                SpUtils.putBoolean(KEY_NOTICE,true)
                dismiss()
            }
        }
    }

}