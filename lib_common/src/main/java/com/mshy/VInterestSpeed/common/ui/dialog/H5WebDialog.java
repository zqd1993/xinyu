package com.mshy.VInterestSpeed.common.ui.dialog;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import androidx.fragment.app.FragmentActivity;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.H5UrlBean;
import com.mshy.VInterestSpeed.common.ui.js.MyJavascriptInterface_;
import com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil;

import java.math.BigDecimal;

/**
 * FileName: com.live.vquonline.view.voiceroom.dialog
 * Author: Reisen
 * Date: 2021/12/14 11:31
 * Description:
 * History:
 */
public class H5WebDialog extends BaseDialogFragment {

    private WebView webView;

    private H5UrlBean h5UrlBean;

    private String loadUrl;
    //宽高比
    private double ratio;
    //屏幕宽高比
    private double widthRatio;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView(View view) {
        webView = view.findViewById(R.id.webview_view);
        webView.addJavascriptInterface(new H5JavascriptInterface(getActivity(), webView), "openObject");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(0); // 设置背景色
        webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        if (!h5UrlBean.getGravity().equals("full")) {
            widthRatio = divide(375, ScreenUtil.getDisplayWidth());
            ratio = divide(h5UrlBean.getWidth(), h5UrlBean.getHeight());
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webView.setBackgroundColor(0); // 设置背景色
//                webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                webView.setBackgroundColor(getResources().getColor(R.color.main_color_bg)); // 设置背景色
//                webView.getBackground().setAlpha(255); // 设置填充透明度 范围：0-255
            }

        });
        webView.loadUrl(loadUrl);
    }

    /**
     * valueOne:除数
     * valueTwo:被除数
     */
    private double divide(double valueOne, double valueTwo) {
        BigDecimal b1 = new BigDecimal(valueOne);
        BigDecimal b2 = new BigDecimal(valueTwo);
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_h5_web;
    }

    public H5WebDialog setH5UrlBean(H5UrlBean h5UrlBean) {
        this.h5UrlBean = h5UrlBean;
        return this;
    }

    public H5WebDialog setLoadUrl(String loadUrl) {
        this.loadUrl = loadUrl;
        return this;
    }

    @Override
    protected int getGravity() {
        String gravity = h5UrlBean.getGravity();
        switch (gravity) {
            case "left":
                return Gravity.START;
            case "right":
                return Gravity.END;
            case "top":
                return Gravity.TOP;
            case "bottom":
                return Gravity.BOTTOM;
            default:
                return Gravity.CENTER;
        }
    }

    @Override
    protected int getWidth() {
        int width;
        if (h5UrlBean.getGravity().equals("full")) {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            width = (int) divide(h5UrlBean.getWidth(), widthRatio);
        }
        Log.e("H5WebDialog", "getWidth: " + width);
        return width;
    }

    @Override
    protected int getHeight() {
        int width;
        int height;
        if (h5UrlBean.getGravity().equals("full")) {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            width = (int) divide(h5UrlBean.getWidth(), widthRatio);
            height = (int) divide(width, ratio);
        }
        Log.e("H5WebDialog", "getHeight: " + height);
        return height;
    }

    private class H5JavascriptInterface extends MyJavascriptInterface_ {


        public H5JavascriptInterface(FragmentActivity activity, WebView webView) {
            super(activity, webView);
        }

        @JavascriptInterface
        public void closeWindow() {
            dismissAllowingStateLoss();
        }


    }
}
