package com.mshy.VInterestSpeed.common.utils;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.constant.RouteKey;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;

public class JumpUtils {
    public static void jump(int linkType, String linkUrl) {
        switch (linkType) {
            case 1:
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity).withString(RouteKey.URL, linkUrl)
                        .withBoolean("is_show_title_bar", false)
                        .navigation();
                break;
        }
    }

    public static void jump(int linkType, String linkUrl, Context context) {
//        Intent intent = null;
        switch (linkType) {
            case 0:
                break;
            case 1:
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity).withString(RouteKey.URL, linkUrl).navigation();
                break;
            case 2:
                if ("help".equals(linkUrl)) {
                    Postcard build = ARouter.getInstance().build(RouteUrl.Common.WebViewActivity);
//                    Intent intentHelp = new Intent(context, BaseWebViewActivity.class);
                    if (UserManager.INSTANCE.getUserInfo().getType() == 2) {
                        build.withString(RouteKey.URL, NetBaseUrlConstant.INSTANCE.getAGREEMENT_BASE_URL() + NetBaseUrlConstant.HELP_URL_SPECIAL);
                    } else {
                        if (UserManager.INSTANCE.getUserInfo().getGender() == 2) {
                            build.withString(RouteKey.URL, NetBaseUrlConstant.INSTANCE.getAGREEMENT_BASE_URL() + NetBaseUrlConstant.HELP_URL_BOY);
                        } else {
                            build.withString(RouteKey.URL, NetBaseUrlConstant.INSTANCE.getAGREEMENT_BASE_URL() + NetBaseUrlConstant.HELP_URL_GIRL);
                        }
                    }
                    build.navigation();
                } else if ("invite".equals(linkUrl)) {
//                    Intent intentInvite = new Intent(context, InvitationActivity.class);
//                    context.startActivity(intentInvite);
                } else if ("rechargeCoin".equals(linkUrl)) {  //跳转到充值金币页面
                    ARouter.getInstance().build(RouteUrl.Bill.BillTantaRechargeActivity).navigation();
                } else if ("applyBigCast".equals(linkUrl)) {  //跳转到女神页面（如果没有成为女神，如果是女神了，就提示已经是女神）

                    if (UserManager.INSTANCE.getUserInfo().isAnchor() == 1) {
//                        ToastUtils.showLong();
                    }

//                    if (SPUtils.getInt(context, ConsUser.IS_ANCHOR) == 1) {
//                        MaleToast.showMessage(context, "您已经是女神了");
//                    } else {
//                        Intent intentApplyBigCast = new Intent(context, BeAnchorActivity.class);
//                        context.startActivity(intentApplyBigCast);
//                    }
                } else if ("auditCamera".equals(linkUrl)) {   //跳到申请自拍认证的界面
//                    Intent intentAuditCamera = new Intent(context, PreSelfieActivity.class);
//                    context.startActivity(intentAuditCamera);
                } else if ("gameOrderList".equals(linkUrl)) {
//                    Intent orderIntent = new Intent(context, PlayWithOrderListActivity.class);
//                    context.startActivity(orderIntent);
                } else if ("rank".equals(linkUrl)) {
//                    Intent intent = new Intent(context, RankActivity.class);
//                    context.startActivity(intent);
                } else if ("main".equals(linkUrl)) {
//                    EventBus.getDefault().post(new DestroyWatchLiveEvent());
//                    Intent intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//                    //关闭当前界面
//                    ((FragmentActivity) context).finish();

                    Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.mshy.VInterestSpeed");
                    context.startActivity(intent);

                } else if ("infoEdit".equals(linkUrl)) {
//                    Intent intent = new Intent(context, InfoEditWhiteActivity.class);
//                    context.startActivity(intent);
                } else if ("bindPhone".equals(linkUrl)) {
//                    Intent intent = new Intent(context,
//                            BindPhoneConfirmActivity.class);
//                    context.startActivity(intent);
                } else if ("auth".equals(linkUrl)) {
                    //认证

                } else if ("dynamic_like".equals(linkUrl)) {
                    ARouter.getInstance().build(RouteUrl.Dynamic.DynamicVquLikeListActivity).navigation();
                } else if ("fate".equals(linkUrl)) {//跳转到首页
                    ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity).withInt(RouteKey.POS, 0).navigation();
                } else if ("dynamic".equals(linkUrl)) {//跳转到动态
                    ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity).withInt(RouteKey.POS, 1).navigation();
                } else if ("message".equals(linkUrl)) {//跳转到消息
                    ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity).withInt(RouteKey.POS, 2).navigation();
                } else if ("my".equals(linkUrl)) {//跳转到我的
                    ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity).withInt(RouteKey.POS, 3).navigation();
                }else if("member".equals(linkUrl)){
                    ARouter.getInstance().build(RouteUrl.Vip.VipTantaCenterActivity).withBoolean(RouteKey.USER_VIP, UserManager.INSTANCE.getUserInfo().getVip() > 0).navigation();
                }
                break;
            case 3:
//                if (!TextUtils.isEmpty(linkUrl)) {
//                    Gson gson = new Gson();
//                    AddQQGroupBean addQQGroupBean = gson.fromJson(linkUrl, AddQQGroupBean.class);
//                    if (null != addQQGroupBean) {
//                        String action = addQQGroupBean.getAction();
//                        if (!TextUtils.isEmpty(action)) {
//                            if (action.equals("add_qq_group")) {
//                                String androidKey = addQQGroupBean.getData().getAndroidKey();
//                                Intent intent = new Intent();
//                                intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + androidKey));
//                                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                try {
//                                    context.startActivity(intent);
//                                } catch (Exception e) {
//                                    // 未安装手Q或安装的版本不支持
//                                    MaleToast.showMessage(context, "检查是否安装QQ或QQ为最新版本");
//                                }
//                            } else if (action.equals("wexinPublic")) {
//                                String weixinId = addQQGroupBean.getData().getWeixinId();
//                                WeChatAccountsDialog wechatAccounts = new WeChatAccountsDialog(context, weixinId);
//                                wechatAccounts.show();
//                            }
//                        }
//                    }
//                }
                break;
            case 4://用户详情页
                ARouter.getInstance().build(RouteUrl.Info.InfoVquPersonalInfoActivity).withInt(
                        RouteKey.USERID, Integer.parseInt(linkUrl)
                ).navigation();
                break;
        }
    }

}
