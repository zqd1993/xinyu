package com.live.module.message.ui.activity

import android.text.TextUtils
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.message.R
import com.live.module.message.databinding.MessageTantaActivityChatSettingBinding
import com.live.module.message.vm.MessageVquChatSettingViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.SetReMarkNameDialog
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment
import com.mshy.VInterestSpeed.uikit.business.uinfo.UserInfoHelper
import com.mshy.VInterestSpeed.uikit.common.CommonUtil
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import dagger.hilt.android.AndroidEntryPoint


/**
 * FileName: com.live.module.message.ui.activity
 * Date: 2022/4/26 15:13
 * Description:
 * History:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquChatSettingActivity)
class MessageVquChatSettingActivity :
    BaseActivity<MessageTantaActivityChatSettingBinding, MessageVquChatSettingViewModel>() {

    @Autowired(name = RouteKey.USERID)
    @JvmField
    var mUid = 0

    var remarkName: String? = ""

    override val mViewModel: MessageVquChatSettingViewModel by viewModels()

    override fun MessageTantaActivityChatSettingBinding.initView() {
        includeTitle.toolbar.initClose(getString(R.string.message_vqu_chat_setting)) {
            finish()
        }
        llInfo.setViewClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    mUid
                )
                .navigation()
        }
        flRemarkName.setViewClickListener {
            showRemarkDialog()
        }
        cbTop.setOnCheckedChangeListener { view, _ ->
            if (view.isPressed) {
                topChat()
            }
        }
        cbBlack.setOnCheckedChangeListener { view, _ ->
            if (view.isPressed) {
                mViewModel.vquDoBlack(mUid.toString())
            }
        }
        flReport.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Dynamic.DynamicVquReportActivity)
                .withInt(
                    RouteKey.USERID,
                    mUid
                )
                .withInt(RouteKey.TYPE, 1)
                .navigation()
        }
    }

    override fun initObserve() {
        mViewModel.vquSaveRemarkNameBean.observe(this) {
            toast(it.message)
            mBinding.tvRemark.text=remarkName
        }
        mViewModel.vquDoBlackSuccess.observe(this) {
            if (it) {
                toast(resources.getString(R.string.vqu_operation_success))
            } else {
                toast(resources.getString(R.string.vqu_operation_failure))
                mBinding.cbBlack.isChecked = !mBinding.cbBlack.isChecked
            }
        }
        mViewModel.vquChatSettingBean.observe(this) {
            if (it.data.info != null) {
                //查询之前是不是存在会话记录
                val recentContact = NIMClient.getService(MsgService::class.java)
                    .queryRecentContact(mUid.toString(), SessionTypeEnum.P2P)
                mBinding.cbTop.isChecked = if (recentContact == null) {
                    false
                } else {
                    CommonUtil.isTagSet(recentContact, RecentContactsFragment.RECENT_TAG_STICKY)
                }
                mBinding.cbBlack.isChecked = it.data.is_black == 1
                mBinding.sdvHead.vquLoadCircleImage(
                    NetBaseUrlConstant.IMAGE_URL + it.data.info.avatar,R.mipmap.ic_common_head_circle_def
                )
                remarkName = it.data.info.remarkName
                mBinding.tvRemark.text=remarkName
                mBinding.tvNickName.text = it.data.info.nickname
                val spiltStr = " | "
                val builder: String = if (!TextUtils.isEmpty(it.data.info.city.toString())) {
                    it.data.info.city.toString() + spiltStr
                } else {
                    ""
                } + it.data.info.age +
                        "岁" +
                        spiltStr +
                        isEmpty(it.data.info.height) +
                        spiltStr +
                        isEmpty(it.data.info.occupation)

                mBinding.tvDes.text = builder
                mBinding.tvSign.text = it.data.info.sign
            }
        }
    }

    override fun initRequestData() {
        mViewModel.vquGetChatSetting(mUid.toString())
    }

    private fun showRemarkDialog() {
        SetReMarkNameDialog()
            .setTitle("设置备注名")
            .setContent(
                if (remarkName.isNullOrEmpty()) {
                    UserInfoHelper.getUserTitleName(
                        mUid.toString(),
                        SessionTypeEnum.P2P
                    )
                } else {
                    remarkName
                }
            )
            .setOnClickListener(object : SetReMarkNameDialog.OnClickListener {
                override fun onCancel() {}
                override fun onConfirm(content: String) {
                    remarkName = content
                    if (remarkName?.isNullOrEmpty() == true) {
                        mViewModel.vquSaveRemarkName(mUid.toString(), "")
                    } else {
                        mViewModel.vquSaveRemarkName(mUid.toString(), content)
//                        toast("备注名不能为空")
                    }
                }
            }).show(supportFragmentManager)
    }

    private fun topChat() {
        //查询之前是不是存在会话记录
        val recentContact = NIMClient.getService(MsgService::class.java)
            .queryRecentContact(mUid.toString(), SessionTypeEnum.P2P)
        //如果之前不存在，创建一条空的会话记录
        if (recentContact == null) {
            // RecentContactsFragment 的 MsgServiceObserve#observeRecentContact 观察者会收到通知
            NIMClient.getService(MsgService::class.java).createEmptyRecentContact(
                mUid.toString() + "",
                SessionTypeEnum.P2P,
                RecentContactsFragment.RECENT_TAG_STICKY,
                System.currentTimeMillis(),
                true
            )
        } else {
            if (CommonUtil.isTagSet(recentContact, RecentContactsFragment.RECENT_TAG_STICKY)) {
                //取消置顶
                CommonUtil.removeTag(recentContact, RecentContactsFragment.RECENT_TAG_STICKY)
            } else {
                // 之前存在，更新置顶flag
                CommonUtil.addTag(recentContact, RecentContactsFragment.RECENT_TAG_STICKY)
            }
            NIMClient.getService(MsgService::class.java).updateRecentAndNotify(recentContact)
        }
    }

    private fun isEmpty(content: String?): String {
        return if (content.isNullOrEmpty()) {
            "未知"
        } else {
            content
        }
    }

}