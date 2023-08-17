package com.mshy.VInterestSpeed.common.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.live.vquonline.base.utils.ToastUtils;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.pay.paytypelibrary.base.OnPayResultListener;
import com.pay.paytypelibrary.base.OrderInfo;
import com.pay.paytypelibrary.base.PayUtil;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class RechargePayPopWindow extends BaseDialogFragment {
    @Override
    protected void initView(View view) {
        TextView rechargeTv = view.findViewById(R.id.tv_pay);
        String orderJson = "{\"version\":10,\"mer_no\":\"6888804120591\",\"mer_order_no\":\"20230817151316600000607517\",\"create_time\":\"20230817151316\",\"expire_time\":\"20230817161316\",\"order_amt\":\"8\",\"notify_url\":\"http:\\/\\/api.gxyisi.com\\/sandpay\\/callback\",\"return_url\":\"\",\"create_ip\":\"218.89.238.225\",\"product_code\":\"02010005\",\"store_id\":\"000000\",\"goods_name\":\"购买80金币\",\"clear_cycle\":\"3\",\"pay_extra\":\"{\\\"wx_app_id\\\":\\\"wx46981c005d7b326c\\\",\\\"gh_ori_id\\\": \\\"gh_dbf1a103ad67\\\",\\\"path_url\\\":\\\"pages\\/zf\\/index?\\\",\\\"miniProgramType\\\":\\\"0\\\"}\",\"accsplit_flag\":\"NO\",\"jump_scheme\":\"\",\"meta_option\":\"[{\\\"s\\\":\\\"Android\\\",\\\"n\\\":\\\"payDemo\\\",\\\"id\\\":\\\"com.pay.paytypetest\\\",\\\"sc\\\":\\\"com.pay.paytypetest\\\"}]\",\"sign_type\":\"RSA\",\"sign\":\"Dzogl3lW1mBUA1e6B7YVzxLGPrO4PsShyjtHuCCQwKiqtS9IzBqUaeZAHAiP2M3JwesLGzFkVvA9mpE7nRjZ4CwlQAbFHTaEJwZMyMYw\\/OUvKOyVxE2zaqf39mlM4ydYUXdHfcn5vewFkgA551OKTwGnQsrvhU8\\/+NVapebuPxl3UOF4lQRPhBFNCKG9kX2Cv\\/dVxuYnBGlrCw+D+\\/F87cBj8hDJKSu2xERdX96ByHYc3Jf3bdDevmQNvzingQs02y2TaHTwYDdPkbgwgf90q0jnU0\\/9vkVwr9PLBuRoIs6HDWtAy9FD476Z42OJdt\\/nmwGrymPemEdaV3HIEAe51g==\",\"order_status\":200}";
        rechargeTv.setOnClickListener(v -> {
            PayUtil.CashierPaySingle(getActivity(), orderJson, new OnPayResultListener() {
                @Override
                public void onSuccess(OrderInfo orderInfo) {
                    startSandWeChatPay(getActivity(), orderInfo);
                }

                @Override
                public void onError(String s) {
                    ToastUtils.showToast("支付失败 ： " + s, Toast.LENGTH_SHORT);
                }
            });
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recharge_pay_pop_window_layout;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getWidth() {
        int width = 0;
        try {
            width = UIUtil.getScreenWidth(requireContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;
    }

    public static void startSandWeChatPay(Context context, OrderInfo orderInfo) {
        String appId = orderInfo.getWxAppId(); // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = orderInfo.getGhOriId(); // 填小程序原始id
        req.path = orderInfo.getPathUrl() + "token_id=" + orderInfo.getTokenId();  //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = Integer.parseInt(orderInfo.getMiniProgramType());// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }
}
