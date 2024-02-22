package com.mshy.VInterestSpeed.common.ui.js;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.live.vquonline.base.utils.ToastUtils;
import com.mshy.VInterestSpeed.common.bean.H5UrlBean;
import com.mshy.VInterestSpeed.common.bean.NotifyH5LoadEvent;
import com.mshy.VInterestSpeed.common.bean.PosterBean;
import com.mshy.VInterestSpeed.common.bean.VquUserInfo;
import com.mshy.VInterestSpeed.common.constant.ConstantKt;
import com.mshy.VInterestSpeed.common.constant.RouteKey;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.constant.SpKey;
import com.mshy.VInterestSpeed.common.event.WxPaySuccessEvent;
import com.mshy.VInterestSpeed.common.ui.dialog.H5WebDialog;
import com.mshy.VInterestSpeed.common.utils.JumpUtils;
import com.mshy.VInterestSpeed.common.utils.UserManager;
import com.mshy.VInterestSpeed.common.utils.UserSpUtils;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;


/**
 * FileName: com.live.vquonline.view.base
 * Author: Reisen
 * Date: 2021/7/8 18:07
 * Description: JS交互界面
 * History:
 */
public class MyJavascriptInterface_ extends BaseLifecycleJavascriptInterface {
    //webView的弱引用
    private final WeakReference<WebView> wek;
    private final String TAG = getClass().getSimpleName();

    public MyJavascriptInterface_(FragmentActivity activity, WebView webView) {
        super(activity);
        wek = new WeakReference<>(webView);
    }

    /**
     * @param userId 用户id
     * @param type   跳转界面 1：个人主页
     */
    @JavascriptInterface
    public void openUserDetail(int userId, int type) {
        switch (type) {
            case 1:
                if (getActivity() != null) {
                    ARouter.getInstance()
                            .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                            .withInt( RouteKey.USERID, userId)
                            .navigation();
//                    Intent intent = new Intent(getActivity(), MainHomePageActivityOld.class);
//                    intent.putExtra("userId", userId);
//                    getActivity().startActivity(intent);
                }
                break;
            case 2:

                break;
        }
    }

    /**
     * 跳转到直播页
     */
    @JavascriptInterface
    public void openUserLive(String roomId) {
        if (getActivity() != null) {
          //  WatchLiveActivity.startActivity(getActivity(), roomId);
        }
    }

    /**
     * 跳转到内页
     *
     * @param linkType
     * @param linkUrl
     */
    @JavascriptInterface
    public void openActivity(int linkType, String linkUrl) {
        if (getActivity() != null) {
            JumpUtils.jump(linkType, linkUrl, getActivity());
        }
    }

    /**
     * 跳转到主页
     *
     * @param type"main" 主页 "dynamic" 动态 "msg" 消息 "my" 我的
     */
    @JavascriptInterface
    public void openMainActivity(String type) {
//        if (getActivity() != null) {
//            int pos;
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            switch (type) {
//                case "main":
//                    pos = 0;
//                    break;
//                case "dynamic":
//                    pos = 1;
//                    break;
//                case "msg":
//                    pos = 2;
//                    break;
//                case "my":
//                    pos = 3;
//                    break;
//                default:
//                    pos = -1;
//                    break;
//            }
//            intent.putExtra("pos", pos);
//            getActivity().startActivity(intent);
//        }
    }

    /**
     * 显示海报弹窗
     *
     * @param jsonContent json内容字符串
     */
    @JavascriptInterface
    public void showPosterDialog(String jsonContent) {
        Gson gson = new Gson();
        final PosterBean data = gson.fromJson(jsonContent, PosterBean.class);
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    new PosterDialog()
//                            .setData(data)
//                            .show(getActivity().getSupportFragmentManager());
                }
            });
        }
    }

    /**
     * 关闭界面
     *
     * @return
     */
    @JavascriptInterface
    public void closeWindow() {
        Log.e(TAG, "closeWindow: ------------------------");
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    /**
     * 获取用户id
     *
     * @return
     */
    @JavascriptInterface
    public int getUserId() {
        if (UserManager.INSTANCE.getUserInfo() == null) {
            UserManager.INSTANCE.setUserInfo(UserSpUtils.INSTANCE.getBean(SpKey.userInfo, VquUserInfo.class));
        }
        if (UserManager.INSTANCE.getUserInfo() != null) {
            return Integer.parseInt(UserManager.INSTANCE.getUserInfo().getUserId()+"");
        } else {
            return 0;
        }
    }

    /**
     * 联系客服
     */
    @JavascriptInterface
    public void startP2PSession() {
        if (getActivity() != null) {
            NimUIKit.startP2PSession(getActivity(), "4");
        }
    }

    @JavascriptInterface
    public void showH5Dialog(final String gravity, final float width, final float height, final String loadUrl) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    H5UrlBean h5UrlBean = new H5UrlBean();
                    h5UrlBean.setWidth((int) width);
                    h5UrlBean.setHeight((int) height);
                    h5UrlBean.setGravity(gravity);
                    new H5WebDialog()
                            .setLoadUrl(loadUrl)
                            .setH5UrlBean(h5UrlBean)
                            .show(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
                }
            });
        } else {
//            MaleToast.showFailureMsg(BaseApplication.getInstance(), "容器为空");
        }
    }

    /*-----------------------生命周期回调----------------------------*/
    @Override
    public void onCreate(@NonNull @NotNull LifecycleOwner owner) {
        super.onCreate(owner);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy(@NonNull @NotNull LifecycleOwner owner) {
        super.onDestroy(owner);
         EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(WxPaySuccessEvent event) {
        if (event.getPayType().equals(ConstantKt.PAY_H5_SUCCESS)) {
            wek.get().loadUrl("javascript:callback()");
        }
        Log.e(TAG, "onEventMainThread: callback()");
    }

    @Subscribe
    public void onEventMainThread(NotifyH5LoadEvent event) {
        if (wek.get() != null) {
            wek.get().loadUrl("javascript:vue_onload()");
        }
        Log.e(TAG, "onEventMainThread: NotifyH5LoadEvent()");
    }

    /**
     * 充值金币成功
     *
     * @param
     */
//    public void onEventMainThread(RechargeCoinSuccessEvent event) {
//        if (wek.get() != null) {
//            wek.get().loadUrl("javascript:vue_onload()");
//        }
//        Log.e(TAG, "RechargeCoinSuccessEvent: callback()");
//    }

    /*-----------------------------分享弹窗-------------------------*/

//    /**
//     * 显示分享弹窗
//     */
//    @JavascriptInterface
//    public void showShareDialog(final String imageUrl
//            , final String weChatUrl) {
//        if (getActivity() != null) {
//            final BottomShareDialog shareDialog = new BottomShareDialog(getActivity(), R.style.SelectiveDialog);
//            shareDialog.setOnButtonSelectListener(new BottomShareDialog.OnButtonSelectListener() {
//                @Override
//                public void onClicked(View view, int index) {
//                    shareDialog.dismiss();
//                    switch (index) {
//                        case 0:
//                            //老版本方法
////                            Intent intent = new Intent(getActivity(), CopyBoardActivity.class);
////                            intent.putExtra("imageUrl", imageUrl);
////                            getActivity().startActivity(intent);
//                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    wek.get().loadUrl("javascript:notifyPoster()");
//                                }
//                            });
//                            break;
//                        case 1:
//                            //微信
//                            ShareManager.getInstance().
//                                    shareToPlatform(1, weChatUrl, "", "分享链接", "描述文字", getActivity());
//                            break;
//                        case 2:
//                            //朋友圈
//                            ShareManager.getInstance().
//                                    shareToPlatform(2, weChatUrl, "", "分享链接", "描述文字", getActivity());
//                            break;
//                        case 3:
//                        case 4:
//                        case 5:
//                        case 6:
//
//                            copyInviteCode(weChatUrl);
//                            break;
//                    }
//                }
//            });
//            shareDialog.show();
//        }
//    }

    @JavascriptInterface
    public void showNewShareDialog(String jsonContent) {
//        Gson gson = new Gson();
//        final ShareNewBean data = gson.fromJson(jsonContent, ShareNewBean.class);
//
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    new ShareDialog()
//                            .setOnButtonSelectListener(new ShareDialog.OnButtonSelectListener() {
//                                @Override
//                                public void onClicked(View view, int index) {
//                                    switch (index) {
//                                        case 0:
//                                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    wek.get().loadUrl("javascript:notifyPoster()");
//                                                }
//                                            });
//                                            break;
//                                        case 1:
//                                            //微信
//                                            ShareManager.getInstance().
//                                                    setShareStatus(onShareStatus).
//                                                    shareToPlatform(1, data.getLinkUrl(), data.getThumbUrl()
//                                                            , data.getTitle(), data.getDes(), getActivity());
//                                            break;
//                                        case 2:
//                                            //朋友圈
//                                            ShareManager.getInstance().
//                                                    setShareStatus(onShareStatus).
//                                                    shareToPlatform(2, data.getLinkUrl(), data.getThumbUrl()
//                                                            , data.getTitle(), data.getDes(), getActivity());
//                                            break;
//                                        case 3:
//                                            //QQ
//                                            ShareManager.getInstance().
//                                                    setShareStatus(onShareStatus).
//                                                    shareToPlatform(3, data.getLinkUrl(), data.getThumbUrl()
//                                                            , data.getTitle(), data.getDes(), getActivity());
//                                            break;
//                                        case 4:
//                                            ShareManager.getInstance().
//                                                    setShareStatus(onShareStatus).
//                                                    shareToPlatform(4, data.getLinkUrl(), data.getThumbUrl()
//                                                            , data.getTitle(), data.getDes(), getActivity());
//                                            //QQ空间
//                                            break;
//                                        case 5:
//                                            //微博
//                                        case 6:
//                                            copyInviteCode(data.getLinkUrl());
//                                            break;
//                                    }
//                                }
//                            })
//                            .show(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
//                }
//            });
//        }
    }

//    private final ShareManager.iShareStatus onShareStatus = new ShareManager.iShareStatus() {
//        @Override
//        public void success() {
//            shareTask();
//        }
//
//        @Override
//        public void error() {
//
//        }
//
//        @Override
//        public void cancel() {
//
//        }
//    };

    /**
     * 分享成功调取红包接口
     */
    private void shareTask() {
//        MainManager.shareTask((BaseActivity) getActivity(), new HttpUiCallBack<JSONObject>() {
//            @Override
//            public void onSuccess(BaseActivity activity, JSONObject result) {
//                MaleToast.showMessage(activity, "分享成功");
//                Log.e(TAG, "onSuccess: shareTask");
//            }
//
//            @Override
//            public void onFailure(BaseActivity activity, String tip) {
//
//            }
//
//            @Override
//            public void onException(BaseActivity activity, Throwable e) {
//
//            }
//        });
    }

    @JavascriptInterface
    public void copyInviteCode(String inviteCode) {
        if (getActivity() == null) return;
        ClipboardManager cm = (ClipboardManager)
                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        cm.setPrimaryClip(ClipData.newPlainText(null, inviteCode));
        ToastUtils.showToast("复制成功，快去粘贴吧！",1000);
       // MaleToast.showMessage(getActivity(), "复制成功，快去粘贴吧！");
    }




    /*-----------------------------------支付相关-------------------------------------*/

    private String mSelectedPayMethod = "alipay";  //默认支付宝
    private String mOrderInfo; //订单信息
    private final int SDK_PAY_FLAG = 0xff01; //handlerMessage

    /**
     * 支付方式
     */
    @JavascriptInterface
    public void showPay(final int id, final String price) {
        weakReference.get().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                new PayNewDialog()
//                        .setOnPayTypeListener(new PayNewDialog.OnPayTypeListener() {
//                            @Override
//                            public void onPay(int payType) {
//                                switch (payType) {
//                                    case PayNewDialog.ALIPAY_PAY:
//                                        mSelectedPayMethod = "alipay";
//                                        confirmToPay(id);
//                                        break;
//                                    case PayNewDialog.WECHAT_PAY:
//                                        mSelectedPayMethod = "wechat";
//                                        confirmToPay(id);
//                                        break;
//                                }
//                            }
//                        })
//                        .setPrice(price)
//                        .show(weakReference.get().getSupportFragmentManager());
            }
        });
    }

    private final Runnable mPayRunnable = new Runnable() {

        @Override
        public void run() {
            PayTask alipay = new PayTask(getActivity());
            Map<String, String> result = alipay.payV2(mOrderInfo, true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
//            if (msg.what == SDK_PAY_FLAG) {
//                @SuppressWarnings("unchecked")
//                Map<String, String> result = (Map<String, String>) msg.obj;
//                AliPayResult payResult = new AliPayResult(result);
//                //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
//                String resultStatus = payResult.getResultStatus();
//                //判断resultStatus 为9000则代表支付成功
//                if (TextUtils.equals(resultStatus, "9000")) {
//                    //该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                    //支付成功
//                    wek.get().loadUrl("javascript:callback()");
//                    Log.e(TAG, "mHandler: callback()");
//                } else {
//                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                    MaleToast.showFailureMsg(getActivity(), "支付失败:" + resultStatus);
//                }
//            }
        }
    };


    private void confirmToPay(int id) {
//        SettingManager.payToNobleCoinRecharge((BaseActivity) getActivity(), String.valueOf(id), mSelectedPayMethod,
//                new HttpUiCallBack<PayBean>() {
//                    @Override
//                    public void onSuccess(BaseActivity activity, PayBean result) {
//                        if ("alipay".equals(mSelectedPayMethod)) {
//                            if (null != result.getPayinfo()) {
//                                mOrderInfo = result.getPayinfo();
//                                Thread payThread = new Thread(mPayRunnable);
//                                payThread.start();
//                            }
//                        } else if ("wechat".equals(mSelectedPayMethod)) {
//                            if (null != result.getWechatpayinfo()) {
//                                IWXAPI com.gxyisikeji.micheng.wxapi = WXAPIFactory.createWXAPI(getActivity(), Constant.WECHAT_APP_ID, false);
//                                WeChatPayInfoBean wechatpayinfo = result.getWechatpayinfo();
//                                //TODO：调起微信支付
//                                PayReq req = new PayReq();
//                                req.appId = Constant.WECHAT_APP_ID;
//                                req.partnerId = wechatpayinfo.getMch_id();  //商户号
//                                req.prepayId = wechatpayinfo.getPrepay_id();  //预付款id
//                                req.nonceStr = wechatpayinfo.getNonce_str();
//                                req.timeStamp = wechatpayinfo.getTimestamp() + "";
//                                req.packageValue = "Sign=WXPay";  //固定值
//                                req.sign = wechatpayinfo.getPaySign();
//                                req.extData = Constant.PAY_H5_SUCCESS;
//                                //先检测是否安装了微信
//                                boolean isWXAppInstalledAndSupported = com.gxyisikeji.micheng.wxapi.isWXAppInstalled();
//                                if (isWXAppInstalledAndSupported) {
//                                    com.gxyisikeji.micheng.wxapi.registerApp(Constant.WECHAT_APP_ID);
//                                    com.gxyisikeji.micheng.wxapi.sendReq(req);
//                                } else {
//                                    MaleToast.showMessage(getActivity(), "未安装微信，不能支付");
//                                }
//
//                            }
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(BaseActivity activity, String tip) {
//                        MaleToast.showMessage(activity, tip);
//                    }
//
//                    @Override
//                    public void onException(BaseActivity activity, Throwable e) {
//                        MaleToast.showMessage(activity, e.getMessage());
//                    }
//                });
    }

}
