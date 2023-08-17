package com.mshy.VInterestSpeed.common.ui.js

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.live.vquonline.base.BaseApplication.Companion.context
import com.live.vquonline.base.utils.GsonUtil
import com.live.vquonline.base.utils.ToastUtils
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.constant.PAY_H5_SUCCESS
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage.getRetrofit
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquPosterDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquShareDialog
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog
import com.mshy.VInterestSpeed.common.utils.JumpKtUtils
import com.mshy.VInterestSpeed.common.utils.ShareManager.iShareStatus
import com.mshy.VInterestSpeed.common.utils.UserManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference


class MyJavascriptInterface(activity: FragmentActivity, webView: WebView) :
    com.mshy.VInterestSpeed.common.ui.js.BaseLifecycleJavascriptInterface(activity) {
    //webView的弱引用
    private var weak: WeakReference<WebView> = WeakReference(webView)
    private val TAG = javaClass.simpleName

    private var mPayListener: ((id: String, type: String) -> Unit)? = null
    var globalApiService: GlobalApiService? = null

    /**
     * @param userId 用户id
     * @param type   跳转界面 1：个人主页
     */
    @JavascriptInterface
    fun openUserDetail(userId: Int, type: Int) {
        Log.d(TAG, "openUserDetail: userId: $userId type: $type")
        when (type) {
            1 -> {
                //跳转到个人主页
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                        RouteKey.USERID,
                        userId
                    )
                    .navigation()
            }
        }
    }


    /**
     * 跳转到直播页
     */
    @JavascriptInterface
    fun openUserLive(roomId: String?) {
        //跳转到直播
        Log.d(TAG, "openUserLive: roomId: $roomId")
    }


    /**
     * 跳转到内页
     *
     * @param linkType
     * @param linkUrl
     */
    @JavascriptInterface
    fun openActivity(linkType: Int, linkUrl: String?) {
        Log.d(TAG, "openActivity: linkType: $linkType linkUrl: $linkUrl")
        JumpKtUtils.jump(context, linkType, linkUrl ?: "")
    }

    /**
     * 跳转到主页
     *
     * @param type"main" 主页 "dynamic" 动态 "msg" 消息 "my" 我的
     */
    @JavascriptInterface
    fun openMainActivity(type: String?) {
        val build = ARouter.getInstance().build(RouteUrl.Main.CommonVquMainAcitvity)
        val pos: Int = when (type) {
            "main" -> 0
            "dynamic" -> 1
            "msg" -> 2
            "my" -> 3
            else -> -1
        }
        build.withInt(RouteKey.POS, pos).navigation()
    }

    /**
     * 显示海报弹窗
     *
     * @param jsonContent json内容字符串
     */
    @JavascriptInterface
    fun showPosterDialog(jsonContent: String?) {
        Log.d(TAG, "showPosterDialog: jsonContent: $jsonContent")
//        val gson = Gson()
//        val data: PosterBean = gson.fromJson(jsonContent, PosterBean::class.java)
        val bean = GsonUtil.GsonToBean(jsonContent, com.mshy.VInterestSpeed.common.bean.PosterBean::class.java)
        //海报弹窗

        val dialog = CommonVquPosterDialog()
        dialog.setData(bean)

        dialog.show(activity!!.supportFragmentManager, "")
    }

    /**
     * 关闭界面
     *
     * @return
     */
    @JavascriptInterface
    fun closeWindow() {
        Log.d(TAG, "closeWindow")
        activity?.finish()
    }

    /**
     * 获取用户id
     *
     * @return
     */
    @JavascriptInterface
    fun getUserId(): Int {
        Log.d(TAG, "getUserId")
        return UserManager.userInfo?.userId?.toInt() ?: 0
    }

    /**
     * 联系客服
     */
    @JavascriptInterface
    fun startP2PSession() {
        Log.d(TAG, "startP2PSession")
        com.mshy.VInterestSpeed.uikit.api.NimUIKit.startP2PSession(context, "100091")
    }

    @JavascriptInterface
    fun showH5Dialog(gravity: String?, width: Float, height: Float, loadUrl: String?) {
        //H5弹窗
        Log.d(TAG, "showH5Dialog")

        activity?.runOnUiThread {
            val h5UrlBean = com.mshy.VInterestSpeed.common.bean.H5UrlBean()
            h5UrlBean.width = width.toInt()
            h5UrlBean.height = height.toInt()
            h5UrlBean.gravity = gravity
            com.mshy.VInterestSpeed.common.ui.dialog.H5WebDialog()
                .setLoadUrl(loadUrl)
                .setH5UrlBean(h5UrlBean)
                .show(activity?.supportFragmentManager)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWxPayEvent(event: com.mshy.VInterestSpeed.common.bean.WxPayEvent) {
        Log.d(TAG, "onWxPayEvent")
        if (event.payType.equals(PAY_H5_SUCCESS)) {
            weak.get()?.loadUrl("javascript:callback()")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNotifyH5LoadEvent(event: com.mshy.VInterestSpeed.common.bean.NotifyH5LoadEvent) {
        Log.d(TAG, "onNotifyH5LoadEvent")
        weak.get()?.loadUrl("javascript:vue_onload()")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRechargeCoinSuccessEvent(event: com.mshy.VInterestSpeed.common.bean.RechargeCoinSuccessEvent) {
        Log.d(TAG, "onRechargeCoinSuccessEvent")
        weak.get()?.loadUrl("javascript:vue_onload()")
    }

    @JavascriptInterface
    fun showNewShareDialog(jsonContent: String?) {
        val gson = Gson()
        val data: com.mshy.VInterestSpeed.common.bean.ShareNewBean = gson.fromJson(jsonContent, com.mshy.VInterestSpeed.common.bean.ShareNewBean::class.java)
        //分享弹窗
        Log.d(TAG, "showNewShareDialog")
        var shareDialog = CommonVquShareDialog()
        shareDialog.setOnShareListener(object : CommonVquShareDialog.OnShareListener {
            override fun createImg() {
                activity?.runOnUiThread {
                    weak.get()?.loadUrl("javascript:notifyPoster()")
                }
            }

            override fun shareWx() {
                //微信
                if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isWxInstalled()) {
                    com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().setShareStatus(onShareStatus).shareToPlatform(
                        1, data.linkUrl, data.thumbUrl, data.title, data.des, activity
                    )
                } else {
                    "请安装微信".toast()
                }
            }

            override fun sharePyq() {
                //朋友圈
                if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isWxInstalled()) {
                    com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().setShareStatus(onShareStatus).shareToPlatform(
                        2, data.linkUrl, data.thumbUrl, data.title, data.des, activity
                    )
                } else {
                    "请安装微信".toast()
                }
            }

            override fun shareQQ() {
                //QQ
                if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isQqInstalled()) {
                    com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().setShareStatus(onShareStatus).shareToPlatform(
                        3, data.linkUrl, data.thumbUrl, data.title, data.des, activity
                    );
                } else {
                    "请安装QQ".toast()
                }
            }

            override fun shareQQZone() {
                if (com.mshy.VInterestSpeed.common.utils.AppInstallUtil.isQqInstalled()) {
                    com.mshy.VInterestSpeed.common.utils.ShareManager.getInstance().setShareStatus(onShareStatus).shareToPlatform(
                        4, data.linkUrl, data.thumbUrl, data.des, data.title, activity
                    );
                } else {
                    "请安装QQ".toast()
                }
            }

            override fun copy() {
                copyInviteCode(data.linkUrl)
            }

        })
        shareDialog?.show(activity!!.supportFragmentManager, "")
    }

    private val onShareStatus: iShareStatus = object : iShareStatus {
        override fun success() {
            shareTask()
        }

        override fun error() {}
        override fun cancel() {}
    }

    private fun shareTask() {
        globalApiService = getRetrofit().create(GlobalApiService::class.java)

        globalApiService?.vquShareTask(1)?.enqueue(object : Callback<BaseResponse<Any>> {
            override fun onResponse(
                call: Call<BaseResponse<Any>>,
                response: Response<BaseResponse<Any>>,
            ) {
            }

            override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
            }
        })

    }

    @JavascriptInterface
    fun copyInviteCode(inviteCode: String?) {
        Log.d(TAG, "copyInviteCode: inviteCode: $inviteCode")
        if (activity == null) return
        val cm: ClipboardManager =
            activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.setPrimaryClip(ClipData.newPlainText(null, inviteCode))
//        MaleToast.showMessage(activity, "复制成功，快去粘贴吧！")
        ToastUtils.showLong("复制成功，快去粘贴吧！")
    }

    /**
     * 支付方式
     */
    @JavascriptInterface
    fun showPay(id: String, price: String?) {
        Log.d(TAG, "showPay: id: $id price: $price:")
        weakReference.get()?.runOnUiThread {
            val payDialog = PayDialog()
            payDialog.setPrice(price ?: "0")
            payDialog.setOnPayListener {
                mPayListener?.invoke(id, it)
            }
        }
    }

    fun setPayListener(listener: (id: String, type: String) -> Unit) {
        mPayListener = listener
    }

}