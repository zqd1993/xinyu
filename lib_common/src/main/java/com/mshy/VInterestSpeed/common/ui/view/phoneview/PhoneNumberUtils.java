package com.mshy.VInterestSpeed.common.ui.view.phoneview;

/**
 * 手机号格式化工具类
 *
 * @author jaydroid
 * @version 1.0
 * @date 4/5/21
 */
class PhoneNumberUtils {

    public static PhoneNumberUtils createInstance() {
        return new PhoneNumberUtils();
    }

    /**
     * True if c is ISO-LATIN characters 0-9, *, # , +
     *
     * (c >= '0' && c <= '9') || c == '*' || c == '#' || c == '+'
     */
    public static boolean isNonSeparator(char c) {
//        || c == '*' || c == '#' || c == '+'
        return (c >= '0' && c <= '9') ;
    }

    public static AsYouTypeFormatter getAsYouTypeFormatter() {
        return new AsYouTypeFormatter();
    }

}
