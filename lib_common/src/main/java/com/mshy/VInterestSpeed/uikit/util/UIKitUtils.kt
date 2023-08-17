package com.mshy.VInterestSpeed.uikit.util

import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo

/**
 * FileName: com.netease.nim.uikit.util
 * Date: 2022/4/13 18:34
 * Description:
 * History:
 */
class UIKitUtils {
    companion object {
        fun loginNIM(account: String, token: String, callback: () -> RequestCallback<LoginInfo>) {
            com.mshy.VInterestSpeed.uikit.api.NimUIKit.login(LoginInfo(account, token), callback.invoke())
        }
    }
}