package com.mshy.VInterestSpeed.common.constant

/**
 * @Author: QuYunShuo
 * @Time: 2020/8/28
 * @Class: RoutePath
 * @Remark: 路由地址
 */
object RouteUrl {

    /**
     * Main模块
     */
    object Main {
        /**
         * 首页
         */
        const val CommonVquMainAcitvity = "/main/CommonVquMainAcitvity"

    }

    object VquMainFragment {
        const val VquMainFragment = "/main_fragment/VquMainFragment"
        const val HomeVquSearchActivity = "/main_fragment/HomeVquSearchActivity"
        const val IceBreakingDialog = "/main_fragment/IceBreakingDialog"
    }

    /**
     * 登录模块
     */
    object Login {

        /**
         * 登录界面
         */
        const val LoginTantaLoginActivity = "/module_login/LoginActivity"
        const val LoginTantaPhoneLoginActivity = "/module_login/PhoneLoginActivity"
        const val LoginVquInputCodeLoginActivity = "/module_login/InputCodeLoginActivity"
        const val LoginTantaAuthCodeActivity = "/module_login/AuthCodeActivity"
        const val LoginTantaSetInfoActivity = "/module_login/LoginTantaSetInfoActivity"
        const val LoginEmptyLoginActivity = "/module_login/EmptyLoginActivity"
    }

    //动态模块
    object Dynamic {
        /**
         * 动态fragment
         */
        const val DynamicVquDynamicFragment = "/module_dynamic/DynamicVquDynamicFragment"

        /**
         * 动态activity
         */
        const val DynamicVquDynamicListActivity = "/module_dynamic/DynamicVquDynamicListActivity"

        /**
         * 动态点赞activity
         */
        const val DynamicVquLikeListActivity = "/module_dynamic/DynamicVquLikeListActivity"

        /**
         * 动态详情
         */
        const val DynamicVquDynamicDetailActivity =
            "/module_dynamic/DynamicVquDynamicDetailActivity"

        /**
         * 动态发布视频页面
         */
        const val DynamicVquPublishVideoActivity = "/module_dynamic/DynamicVquPublishVideoActivity"

        /**
         * 动态发布图片页面
         */
        const val DynamicVquPublishImgActivity = "/module_dynamic/DynamicVquPublishImgActivity"

        /**
         * 举报
         */
        const val DynamicVquReportActivity = "/module_dynamic/DynamicVquReportActivity"
    }

    //设置模块
    object Setting {
        /**
         * 设置页面
         */
        const val SettingVquActivity = "/module_setting/SettingVquActivity"

        /**
         * 绑定手机号页面
         */
        const val SettingVquBindMobileActivity = "/module_setting/SettingVquBindMobileActivity"

        /**
         * 忘记密码关闭青少年模式
         */
        const val SettingVquCloseTeenModeActivity =
            "/module_setting/SettingVquCloseTeenModeActivity"

        /**
         * 消息通知页面
         */
        const val SettingVquMessageActivity = "/module_setting/SettingVquMessageActivity"

        /**
         * 青少年模式已关闭页面
         */
        const val SettingVquTeenModeActivity = "/module_setting/SettingVquTeenModeActivity"

        /**
         * 开启青少年模式
         */
        const val SettingVquTeenModeOpenActivity = "/module_setting/SettingVquTeenModeOpenActivity"

        /**
         * 青少年模式确认密码
         */
        const val SettingVquTeenModePwdActivity = "/module_setting/SettingVquTeenModePwdActivity"

        /**
         * 关闭青少年模式
         */
        const val SettingVquTeenModePwdCloseActivity =
            "/module_setting/SettingVquTeenModePwdCloseActivity"

        /**
         * 黑名单页面
         */
        const val SettingVquBlackListActivity = "/module_setting/SettingVquBlackListActivity"

        /**
         * 注销账号页面
         */
        const val SettingVquLogoffActivity = "/module_setting/SettingVquLogoffActivity"

        /**
         * 关于鹊娘
         */
        const val SettingVquAboutActivity = "/module_setting/SettingVquAboutActivity"
    }

    //个人信息模块
    object Info {

        /**
         * 信息编辑页面
         */
        const val InfoVquEditActivity = "/module_info/InfoVquEditActivity"

        /**
         * 预览图片页面
         */
        const val InfoVquPreviewPictureActivity = "/module_info/InfoVquPreviewPictureActivity"

        /**
         * 个人页
         */
        const val InfoVquPersonalInfoActivity = "/module_info/InfoVquPersonalInfoActivity"

        /**
         * 礼物墙
         */
        const val InfoVquGiftListActivity = "/module_info/InfoVquGiftListActivity"
    }

    /**
     * 粉丝、关注、访客模块
     */
    object Relation {
        const val RelationVquActivity = "/module_relation/RelationActivity"
        const val RelationVquFriendActivity = "/module_relation/RelationFriendActivity"
        const val RelationVquFriendFragment = "/module_relation/RelationFriendFragment"
        const val RelationVquPraiseListActivity = "/module_relation/RelationVquPraiseListActivity"
    }

    /**
     * 收支明细，充值，收益，账单
     */
    object Bill {
        const val BillTantaRechargeActivity = "/module_bill/TantaRechargeActivity"
        const val BillTantaEarningsActivity = "/module_bill/TantaEarningsActivity"
        const val BillTantaDetailActivity = "/module_bill/TantaBillDetailActivity"
        const val BillTantaExchangeDiamondActivity = "/module_bill/BillTantaExchangeDiamondActivity"
        const val BillTantaDetailFragment = "/module_bill/BillTantaDetailFragment"
        const val BillTantaAccountListActivity = "/module_bill/BillTantaAccountListActivity"
        const val BillTantaNewWithdrawalAccountActivity =
            "/module_bill/BillTantaNewWithdrawalAccountActivity"
        const val BillTantaWithdrawalListFragment = "/module_bill/BillTantaWithdrawalListFragment"
    }

    /**
     * VIP模块
     */
    object Vip {
        const val VipTantaCenterActivity = "/module_vip/VipTantaCenterActivity"
        const val VipTantaMemberOpenSucceedActivity = "/module_vip/VipTantaMemberOpenSucceedActivity"

    }

    /**
     * 公共模块
     */
    object Common {
        const val WebViewActivity = "/lib_common/WebViewActivity"
        const val CommonVquCitySelectorActivity = "/lib_common/CommonVquCitySelectorActivity"
        const val CommonPayResultActivity = "/lib_common/CommonPayResultActivity"
        const val CommonVquVideoActivity = "/lib_common/CommonVquVideoActivity"
        const val CommonVquLoginDialog = "/lib_common/CommonVquLoginDialog"
        const val LoginEmptyActivity = "/lib_common/LoginEmptyActivity"
    }

    /**
     * 我的
     */
    object Mine {
        private const val MINE = "/lib_mine"
        const val MineVquFragment = "${MINE}/MineVquFragment"
        const val MineVquMyBackpackActivity = "${MINE}/MineVquMyBackpackActivity"
        const val MineVquTryCardFragment = "${MINE}/MineVquTryCardFragment"
        const val MineVquCouponFragment = "${MINE}/MineVquCouponFragment"
    }

    object Message {

        private const val MESSAGE = "/module_message"

        /**
         * 消息主页Tab项
         */
        const val MessageVquMainFragment = "$MESSAGE/MessageVquMainFragment"

        /**
         * 联系人列表
         */
        const val MessageVquRecentFragment = "$MESSAGE/MessageVquRecentFragment"

        /**
         * 联系人列表
         */
        const val MessageVquRecentBackupFragment = "$MESSAGE/MessageVquRecentBackupFragment"

        /**
         * 亲密列表
         */
        const val MessageVquRecentIntimateFragment = "$MESSAGE/MessageVquRecentIntimateFragment"

        /**
         * 聊天界面的Fragment
         */
        const val MessageVquMsgFragment = "$MESSAGE/MessageVquMsgFragment"

        /**
         * P2P聊天界面
         */
        const val MessageVquP2PAct = "$MESSAGE/MessageVquP2PActivity"

        /**
         * 好友设置界面
         */
        const val MessageVquChatSettingActivity = "$MESSAGE/MessageVquChatSettingActivity"

        /**
         * 通用语界面
         */
        const val MessageVquCommonWordActivity = "$MESSAGE/MessageVquCommonWordActivity"

        /**
         * 引导页界面
         */
        const val MessageVquGuideActivity = "$MESSAGE/MessageVquGuideActivity"

        /**
         * 通话记录Fragment
         */
        const val MessageVquCallRecordFragment = "$MESSAGE/MessageVquCallRecordFragment"

    }

    /**
     * 声网模块
     */
    object Agora {

        const val AgoraVquVideoActivity = "/agora/AgoraVquVideoActivity"
        const val AgoraVquAudioActivity = "/agora/AgoraVquAudioActivity"
        const val AgoraVquBeautySettingActivity = "/agora/MineVquBeautySettingActivity"
        const val AgoraVquMatchingActivity = "/agora/AgoraVquMatchingActivity"
        const val IceBreakingDialog = "/agora/IceBreakingDialog"

    }

    object Agora2 {

        const val AgoraTantaVideoActivity = "/agora2/AgoraTantaVideoActivity"
//        const val AgoraVquVideo2Activity = "/agora2/AgoraVquVideo2Activity"
        const val AgoraTantaAudioActivity = "/agora2/AgoraTantaAudioActivity"
        const val AgoraTantaBeautySettingActivity = "/agora2/MineTantaBeautySettingActivity"
        const val AgoraTantaMatchingActivity = "/agora2/AgoraTantaMatchingActivity"
        const val IceBreakingDialog = "/agora2/IceBreakingDialog"

    }

    /**
     * 我的人脉模块
     */
    object Contact {
        private const val CONTACT = "/module_contact"

        const val ContactTantaMyContactActivity = "$CONTACT/ContactVquMyContactActivity"
        const val ContactTantaDailyActivity = "$CONTACT/ContactTantaDailyActivity"
        const val ContactTantaFriendValueActivity = "$CONTACT/ContactTantaFriendValueActivity"
        const val ContactTantaExportActivity = "$CONTACT/ContactTantaExportActivity"
        const val ContactTantaInvitedRecordActivity = "$CONTACT/ContactTantaInvitedRecordActivity"
        const val ContactTantaInvitedRecordSearchActivity =
            "$CONTACT/ContactTantaInvitedRecordSearchActivity"
    }


    /**
     * 女神设置
     */
    object Anchor {
        private const val ANCHOR = "/module_anchor"

        const val AnchorVquSettingsActivity = "$ANCHOR/AnchorVquSettingsActivity"
        const val AnchorTantaRateSettingActivity = "$ANCHOR/AnchoTantaRateSettingActivity"
        const val AnchorVquHelloSettingActivity = "$ANCHOR/AnchorVquHelloSettingActivity"
        const val AnchorVquNewHelloTemplateActivity = "$ANCHOR/AnchorVquNewHelloTemplateActivity"
    }

    /**
     * 认证模块
     */
    object Auth {
        private const val AUTH = "/module_auth"

        const val AuthVquRealAuthActivity = "$AUTH/AuthVquRealAuthActivity"
        const val AuthVquResultActivity = "$AUTH/AuthVquResultActivity"
        const val AuthVquFaceIdentifyActivity = "$AUTH/AuthVquFaceIdentifyActivity"
        const val AuthVquCenterActivity = "$AUTH/AuthVquCenterActivity"
        const val AuthVquAvatarActivity = "$AUTH/AuthVquAvatarActivity"
        const val AuthVquCameraActivity = "$AUTH/AuthVquCameraActivity"
        const val AuthVquCameraFragment = "$AUTH/AuthVquCameraFragment"
    }
}