package com.mshy.VInterestSpeed.uikit.util;

import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;

import com.mshy.VInterestSpeed.common.utils.MyClickSpan;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/15 19:09
 */
public class HtmlUtils {
    // 测试文本
    String s1 = "<font color='#FF6A6A'>一般文本颜色设置红色</font><br><a href=\"https://www.baidu.com\"><font color='#FFD700'>百度链接</font></a><br>";

    public static CharSequence getClickableHtml(String html) {
        if (TextUtils.isEmpty(html)) {
            return "";
        }

        Spanned spannedHtml;
        if (Build.VERSION.SDK_INT >= 24) {
            spannedHtml = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            spannedHtml = Html.fromHtml(html);
        }
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);

        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);

        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);

        }
        return clickableHtmlBuilder;
    }

    public static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);

        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        MyClickSpan clickableSpan = new MyClickSpan(urlSpan.getURL());
        if (start >= 0 && end >= 0 ) {
            clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
        }

    }
}
