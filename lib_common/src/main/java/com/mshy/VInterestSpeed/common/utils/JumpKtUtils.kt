package com.mshy.VInterestSpeed.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.utils.GsonUtil
import com.live.vquonline.base.utils.ToastUtils
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl


/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
object JumpKtUtils {
    fun jump(context: Context, linkType: Int, url: String) {
        when (linkType) {
            0 -> {
            }
            1 -> {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                    .withString(RouteKey.URL, url).navigation()
            }
            2 -> {
                jump2(context, url)
            }
            3 -> {
                jump3(context, url)
            }
        }
    }

    private fun jump3(context: Context, url: String) {
        if (url.isNotEmpty()) {
            val addQQGroupBean = GsonUtil.GsonToBean(url, com.mshy.VInterestSpeed.common.bean.AddQQGroupBean::class.java)
            if (addQQGroupBean != null) {
                val action = addQQGroupBean.action
                if (!action.isNullOrEmpty()) {
                    if (action == "add_qq_group") {
                        val androidKey = addQQGroupBean.data.androidKey
                        val intent = Intent()
                        intent.data =
                            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$androidKey")
                        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // 未安装手Q或安装的版本不支持
                            ToastUtils.showLong("检查是否安装QQ或QQ为最新版本")
                        }
                    } else if (action == "wexinPublic") {
                        //微信公众号弹窗
                    }
                }
            }
        }
    }

    private fun jump2(context: Context, url: String) {
        if ("help" == url) {
            val build = ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
            //                    Intent intentHelp = new Intent(context, BaseWebViewActivity.class);
            if (UserManager.userInfo?.type == 2) {
                build.withString(
                    RouteKey.URL,
                    NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.HELP_URL_SPECIAL
                )
            } else {
                if (UserManager.userInfo?.gender == 2) {
                    build.withString(
                        RouteKey.URL,
                        NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.HELP_URL_BOY
                    )
                } else {
                    build.withString(
                        RouteKey.URL,
                        NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.HELP_URL_GIRL
                    )
                }
            }
            build.navigation()
        } else if ("invite" == url) {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, UserManager.webUrl?.share ?: "")
                .navigation()
        } else if ("rechargeCoin" == url) {
            //跳转到充值金币页面
            ARouter.getInstance().build(RouteUrl.Bill.BillTantaRechargeActivity).navigation()
        } else if ("applyBigCast" == url) {
            //跳转到女神页面（如果没有成为女神，如果是女神了，就提示已经是女神）
            if (UserManager.userInfo?.isAnchor == 1) {
                ToastUtils.showLong("您已经是女神了")
            } else {
                //跳转到成为女神
            }
        } else if ("auditCamera" == url) {
            //跳到申请自拍认证的界面
        } else if ("member" == url) {
            //跳转到骑士页面
        } else if ("gameOrderList" == url) {
            //陪玩订单
        } else if ("rank" == url) {
            //榜单
        } else if ("main" == url) {
            ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity).navigation()
            if (context is Activity) {
                context.finish()
            }
        } else if ("infoEdit" == url) {
            //编辑资料
            ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
        } else if ("bindPhone" == url) {
            //绑定手机
            ARouter.getInstance()
                .build(RouteUrl.Setting.SettingVquBindMobileActivity)
                .navigation()
        } else if ("auth" == url) {
            //认证
            ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity).navigation()
//            if (UserManager.userInfo?.gender == 1) {
//                //女认证
//                ARouter.getInstance().build(RouteUrl.Auth.AuthVquFaceIdentifyActivity).navigation()
//            } else {
//                //男认证
//                ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity).navigation()
//            }
        }
    }
}