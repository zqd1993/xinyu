package com.mshy.VInterestSpeed.common.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mshy.VInterestSpeed.common.constant.RouteKey;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/15 19:26
 */
public class MyClickSpan extends ClickableSpan {

    String url;

    public MyClickSpan(String url) {
        this.url = url;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//        super.updateDrawState(ds);
//        ds.linkColor = ContextCompat.getColor(BaseApplication.context, R.color.base_colorPrimary);//设置a链接颜色
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
//        ToastUtils.showShort(BaseApplication.context,url+"");

        ARouter.getInstance().build(RouteUrl.Common.WebViewActivity).withString(RouteKey.URL, url).navigation();
    }

}
