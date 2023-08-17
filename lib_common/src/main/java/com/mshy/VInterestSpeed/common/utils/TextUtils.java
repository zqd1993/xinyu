package com.mshy.VInterestSpeed.common.utils;

import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/15 19:09
 */
public class TextUtils {

    static String html = "<a href=\"http://www.google.com\"><font color=\"#6200EE\">google</font></a><font color=\"#FFFFFF\">123132</font><a href=\"http://www.baidu.com\"><font color=\"#FF5CA2\">baidu</font></a>";

    public static CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = getSpannedHtml(html);
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilder(spannedHtml);
        URLSpan[] urlSpans = getURLSpans(spannableStringBuilder, spannedHtml);
        for (final URLSpan span : urlSpans) {
            setLinkClickable(spannableStringBuilder, span);
        }
        return spannableStringBuilder;
    }

    public static URLSpan[] getURLSpans(SpannableStringBuilder clickableHtmlBuilder, Spanned spannedHtml) {
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        return urls;
    }

    public static Spanned getSpannedHtml(String html) {
        Spanned spannedHtml;
        if (Build.VERSION.SDK_INT >= 24) {
            spannedHtml = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT);
        } else {
            spannedHtml = Html.fromHtml(html);
        }

        return spannedHtml;
    }

    public static SpannableStringBuilder getSpannableStringBuilder(Spanned spannedHtml) {
        return new SpannableStringBuilder(spannedHtml);
    }

    public static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);

        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);

        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        MyClickSpan clickableSpan = new MyClickSpan(urlSpan.getURL());
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }

    public static void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan, ClickableSpan clickableSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);

        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);

        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }


}
