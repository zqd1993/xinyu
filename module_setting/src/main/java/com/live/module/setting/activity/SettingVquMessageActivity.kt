package com.live.module.setting.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityMessageBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.NiMUIKitVquApplication
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.ResponseCode
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.mixpush.MixPushService
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


/**
 * author: Tany
 * date: 2022/4/2
 * description:消息设置
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquMessageActivity)
class SettingVquMessageActivity :
    BaseActivity<SettingTantaActivityMessageBinding, SettingVquViewModel>() {
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityMessageBinding.initView() {
        mBinding.includeTitle.toolbar.initClose("隐私设置") {
            finish()
        }
        mBinding.rlNotice.setViewClickListener { vquOpenNotice() }
        if (1 == UserManager.userInfo?.gender) {
            mBinding.rlFate.gone()
            mBinding.tvFate.gone()
        } else {
            mBinding.rlFate.visible()
            mBinding.tvFate.visible()
        }
        mBinding.rlNoticeSwitch.isChecked = SpUtils.getBoolean(SpKey.NEW_MESSAGE_NOTICE, true)!!
        mBinding.rlRecommendedSwitch.isChecked =
            SpUtils.getBoolean(SpKey.SETTING_RECOMMENDED_OPEN, true)!!
        mBinding.rlVoiceSwitch.isChecked = SpUtils.getBoolean(SpKey.RING_NOTICE, true)!!;
        mBinding.rlShakeSwitch.isChecked = SpUtils.getBoolean(SpKey.SHAKE_NOTICE, true)!!;

        mBinding.rlPredestinedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                mViewModel.vquSetFateMatch(
                    if (isChecked) {
                        1
                    } else {
                        0
                    }
                )
            }
        }
        mBinding.rlRecommendedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    SpUtils.putBoolean(SpKey.SETTING_RECOMMENDED_OPEN, true)!!
                } else {
                    SpUtils.putBoolean(SpKey.SETTING_RECOMMENDED_OPEN, false)!!
                }
                EventBus.getDefault().post("RecommendedSwitch")
            }
        }
        mBinding.rlNoticeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                setMessageNotify(isChecked)
            }
        }
        mBinding.rlVoiceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                setRing(isChecked);
            }
        }
        mBinding.rlShakeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                setVibrate(isChecked);
            }
        }
        mBinding.rlNoticeLike.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    SpUtils.putBoolean(SpKey.SETTING_DYNAMIC_MESSAGE, true)!!
                    mViewModel.setUserLikeStatus("0")
                } else {
                    SpUtils.putBoolean(SpKey.SETTING_DYNAMIC_MESSAGE, false)!!
                    mViewModel.setUserLikeStatus("1")
                }
            }
        }
        mBinding.rlLocationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    mViewModel.vquSetShowLocation(1)
                } else {
                    mViewModel.vquSetShowLocation(0)
                }
            }
        }
    }

    private fun setVibrate(vibrate: Boolean) {
        SpUtils.putBoolean(SpKey.SHAKE_NOTICE, vibrate)
        val config: StatusBarNotificationConfig = NiMUIKitVquApplication.getInstance()
            ?.getStatusConfig(SpUtils.getBoolean(SpKey.RING_NOTICE, true)!!, vibrate)!!
        NIMClient.updateStatusBarNotificationConfig(config)
    }

    private fun setRing(ring: Boolean) {
        SpUtils.putBoolean(SpKey.RING_NOTICE, ring)

        val config: StatusBarNotificationConfig =
            NiMUIKitVquApplication.getInstance()
                ?.getStatusConfig(ring, SpUtils.getBoolean(SpKey.SHAKE_NOTICE, true)!!)!!
        NIMClient.updateStatusBarNotificationConfig(config)
    }

    private fun setMessageNotify(checkState: Boolean) {
        // 如果接入第三方推送（小米），则同样应该设置开、关推送提醒
        // 如果关闭消息提醒，则第三方推送消息提醒也应该关闭。
        // 如果打开消息提醒，则同时打开第三方推送消息提醒。
        NIMClient.getService(MixPushService::class.java)
            .enable(checkState)
            .setCallback(object : RequestCallback<Void?> {
                override fun onSuccess(param: Void?) {
                    setToggleNotification(checkState)
                }

                override fun onFailed(code: Int) {
                    // 这种情况是客户端不支持第三方推送
                    if (code == ResponseCode.RES_UNSUPPORT.toInt()) {
                        setToggleNotification(checkState)
                    } else if (code == ResponseCode.RES_EFREQUENTLY.toInt()) {
                        mBinding.rlNoticeSwitch.isChecked = !checkState
                        "操作太频繁".toast()
                    } else {
                        mBinding.rlNoticeSwitch.isChecked = !checkState
                        "设置失败，请重试".toast()
                    }
                }

                override fun onException(exception: Throwable) {
                }
            })
    }

    private fun setToggleNotification(checkState: Boolean) {
        try {
            setNotificationToggle(checkState)
            NIMClient.toggleNotification(checkState)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setNotificationToggle(on: Boolean) {
        SpUtils.putBoolean(SpKey.NEW_MESSAGE_NOTICE, on)
    }

    private fun vquOpenNotice() {//跳转到系统界面打开通知
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts(
                "package",
                this@SettingVquMessageActivity.packageName,
                null
            )
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName(
                "com.android.settings",
                "com.android.settings.InstalledAppDetails"
            )
            localIntent.putExtra(
                "com.android.settings.ApplicationPkgName",
                this@SettingVquMessageActivity.packageName
            )
        }
        startActivity(localIntent)

    }

    override fun initObserve() {
        mViewModel.vquFateMatchData.observe(this, Observer {
            mBinding.rlPredestinedSwitch.isChecked = it.data.fate_match == 1
        })
        mViewModel.vquSetFateMatchData.observe(this, Observer {
            "设置成功".toast()
        })
        mViewModel.vquLikeStatusData.observe(this, Observer {
            if (it.data == "0") {
                SpUtils.putBoolean(SpKey.SETTING_DYNAMIC_MESSAGE, true)!!
            } else {
                SpUtils.putBoolean(SpKey.SETTING_DYNAMIC_MESSAGE, false)!!
            }
            mBinding.rlNoticeLike.isChecked =
                SpUtils.getBoolean(SpKey.SETTING_DYNAMIC_MESSAGE, true)!!
        })
        mViewModel.vquSetShowLocation.observe(this, Observer {
            "设置成功".toast()
            mBinding.rlLocationSwitch.isChecked = it.data.show_location == 1
        })
        mViewModel.vquGetShowLocation.observe(this, Observer {
            mBinding.rlLocationSwitch.isChecked = it.data.show_location == 1
        })
    }

    override fun initRequestData() {
        mViewModel.vquGetFateMatch()
        mViewModel.getUserLikeStatus()
        mViewModel.vquShowLocation()
    }

    override fun onResume() {
        super.onResume()
        vquNotificationRun();
    }

    private fun vquNotificationRun() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!com.mshy.VInterestSpeed.common.utils.NotificationsUpUtils.isEnableV26(this)) {
                mBinding.rlNotice.visible()
                mBinding.tvNotice.visible()
            } else {
                mBinding.rlNotice.gone()
                mBinding.tvNotice.gone()
            }
        } else {
            if (!com.mshy.VInterestSpeed.common.utils.NotificationsUtils.isNotificationEnabled(this)) {
                mBinding.rlNotice.visible()
                mBinding.tvNotice.visible()
            } else {
                mBinding.rlNotice.gone()
                mBinding.tvNotice.gone()
            }
        }
    }
}