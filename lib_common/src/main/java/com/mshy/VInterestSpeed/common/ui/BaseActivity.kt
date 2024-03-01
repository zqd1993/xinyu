package com.mshy.VInterestSpeed.common.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.google.android.exoplayer2.util.NotificationUtil
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.Transport
import com.live.vquonline.base.mvvm.v.BaseFrameActivity
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.CommonApplication
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.event.CommonNotificationEvent
import com.mshy.VInterestSpeed.common.event.DynamicLikeEvent
import com.mshy.VInterestSpeed.common.event.NotificationManagerEvent
import com.mshy.VInterestSpeed.common.livedata.AppViewModel
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquRedPacketDialog
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.UserManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * Activity基类
 *
 * ""
 * @since 8/27/20
 */
@EventBusRegister
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : BaseFrameActivity<VB, VM>() {
    //是否在前台
    public var isFront = false
    val appViewModel: AppViewModel by lazy {
        CommonApplication.mCommonApplication.getAppViewModelProvider().get(
            AppViewModel::class.java
        )
    }

    protected val mPayViewModel: CommonPayViewModel by viewModels()

    /**
     * 设置状态栏
     * 子类需要自定义时重写该方法即可
     * @return Unit
     */
    override fun setStatusBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
            .statusBarColor(R.color.base_colorPrimary)
            .statusBarDarkFont(true)
            .navigationBarColor(R.color.base_colorPrimary).init()
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "ActivityStack: ${ActivityStackManager.activityStack}")
        isFront = true
        UserManager.isChatting = false
        val manager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }

    override fun onPause() {
        super.onPause()
        isFront = false
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解决某些特定机型会触发的Android本身的Bug
        AndroidBugFixUtils().fixSoftInputLeaks(this)
    }

//    override fun onStart() {
//        checkInstanceActivityAndGoTo()
//        super.onStart()
//    }

    protected var mLoadService: LoadService<*>? = null


    /**
     * 注册LoadSir
     *
     * @param view 替换视图
     */
    open fun setLoadSir(view: View?) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault()
                .register(view,
                    Callback.OnReloadListener {
                        onRetryBtnClick()
                    })
        }
    }

    /**
     * 失败重试,重新加载事件
     */
    open fun onRetryBtnClick() {

    }

    private var isShowedContent = false

    fun showContent() {
        if (mLoadService != null) {
            isShowedContent = true
            mLoadService?.showSuccess()
        }
    }

    fun showEmpty(
        mesage: String = getString(R.string.base_fine_no_data),
        defaultIcon: Int = R.mipmap.ic_tanta_base_empty,
    ) {
        if (null != mLoadService) {
            var emptyCallback = com.mshy.VInterestSpeed.common.ui.loadsir.EmptyCallback::class.java
            mLoadService!!.setCallBack(emptyCallback,
                Transport { context, view ->
                    val mTvEmpty: TextView =
                        view.findViewById<View>(R.id.tv_loadsir_empty) as TextView
                    val mIvImage: ImageView =
                        view.findViewById<View>(R.id.iv_loadsir_empty) as ImageView
                    mTvEmpty.setText(mesage)
                    mIvImage.setImageResource(defaultIcon)
                })
            mLoadService?.showCallback(emptyCallback)
        }
    }

    fun showFailure(
        mesage: String = getString(R.string.base_empty_tips_netword),
        defaultIcon: Int = R.mipmap.ic_base_network_error,
    ) {
        if (null != mLoadService) {
            if (!isShowedContent) {
                mLoadService!!.setCallBack(com.mshy.VInterestSpeed.common.ui.loadsir.ErrorCallback::class.java,
                    Transport { context, view ->
                        val mTvTextError: TextView =
                            view.findViewById<View>(R.id.tv_loadsir_error) as TextView
                        val mIvImageError: ImageView =
                            view.findViewById<View>(R.id.iv_loadsir_error) as ImageView
                        mTvTextError.setText(mesage)
                        mIvImageError.setImageResource(defaultIcon)
                    })
            } else {
                toast(mesage, 1000)
            }

        }
    }

    fun showLoading() {
        if (null != mLoadService) {
            mLoadService!!.showCallback(com.mshy.VInterestSpeed.common.ui.loadsir.LoadingCallback::class.java)
        }
    }


    @Subscribe
    fun onSwipeNotificationManager(event: NotificationManagerEvent) {
        if (isFront) {
            if (event.isAddNotification == 1) {//add

                // mWindowManager?.addNotification(matchData)
            } else {

            }
        }
    }

    @Subscribe
    fun onEventMainThread(event: com.mshy.VInterestSpeed.common.event.RedPacketEvent) {
        if (isFront) {
            var redPacketDialog = CommonVquRedPacketDialog()
            redPacketDialog.setData(event.data)
            redPacketDialog.show(supportFragmentManager, "red")
        }
    }

    /**
     * 亲密升级提示弹窗
     */
    @Subscribe
    fun onEventMainThread(event: com.mshy.VInterestSpeed.uikit.event.NotificationIntimateUpEvent) {
        if (isFront) {
            com.mshy.VInterestSpeed.common.ui.dialog.CommonVquIntimateLevelUpDialog()
                .setData(event.data.data)
                .show(supportFragmentManager)
        }
    }

    open fun isBackground(context: Context): Boolean {
        val appProcesses: List<ActivityManager.RunningAppProcessInfo> =
            (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance !== ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台" + appProcess.processName)
                    return true
                } else {
                    Log.i(context.getPackageName(), "处于前台" + appProcess.processName)
                    return false
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: CommonNotificationEvent) {
        if (event == null) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "customcaicai"
            val channelName = "山海甜缘系统通知"
            val importance = NotificationManager.IMPORTANCE_HIGH
            NotificationUtil.createNotificationChannel(
                this,
                channelId,
                R.string.app_name,
                R.string.common_vqu_notification,
                importance
            )
        }
        val intent: Intent = Intent(
            this,
            com.mshy.VInterestSpeed.uikit.receiver.NotificationClickReceiver::class.java
        )
        intent.putExtra("linkType", event.data?.linkType)
        intent.putExtra("linkUrl", event.data?.linkUrl)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val manager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val customNotification: Notification =
            Notification.Builder(this, "customcaicai")
                .setContentTitle(event.data?.title)
                .setContentText(event.data?.content)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent) //设置点击响应
                .build()
        manager.notify(2, customNotification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: DynamicLikeEvent) {

        if (SpUtils.getBoolean(SpKey.SETTING_DYNAMIC_MESSAGE, true)!!) {
            if (isBackground(this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelId = "customcaicai"
                    val channelName = "山海甜缘系统通知"
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    NotificationUtil.createNotificationChannel(
                        this,
                        channelId,
                        R.string.app_name,
                        R.string.common_vqu_notification,
                        importance
                    )
                }
                val intent: Intent = Intent(
                    this,
                    com.mshy.VInterestSpeed.uikit.receiver.NotificationClickReceiver::class.java
                )
                intent.putExtra("linkType", 2)
                intent.putExtra("linkUrl", "dynamic_like")
                val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val manager: NotificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val customNotification: Notification =
                    Notification.Builder(this, "customcaicai")
                        .setContentTitle("动态通知")
                        .setContentText(event.data.msg)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(
                            BitmapFactory.decodeResource(
                                resources,
                                R.mipmap.ic_launcher
                            )
                        )
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent) //设置点击响应
                        .build()
                manager.notify(2, customNotification)
            }

        }
    }


    @Subscribe
    fun onEventMainThread(event: com.mshy.VInterestSpeed.common.event.ShowAdDialogEvent) {
        if (isFront) {
            if (event.result != null) {
                val data: com.mshy.VInterestSpeed.common.bean.CommonVquAdBean.ListBean = event.result
                when (event.result.window_type) {
                    1 -> {}
                    2 -> {
                        val h5UrlBean = com.mshy.VInterestSpeed.common.bean.H5UrlBean()
                        h5UrlBean.height = data.getHeight()
                        h5UrlBean.width = data.getWidth()
                        h5UrlBean.gravity = data.getGravity()
                        com.mshy.VInterestSpeed.common.ui.dialog.H5WebDialog()
                            .setH5UrlBean(h5UrlBean)
                            .setLoadUrl(data.getWindow_link())
                            //.setLoadUrl("https://asset.vqu.show/h5/index.html#/todayfate?token=VqMJoJkpnezKwrsfavHjSr-FyLG673Kv9aeIV4loo7c")
                            .show(supportFragmentManager)
                    }
                }
            }
        }
    }

    @SuppressLint("NewApi", "WrongConstant")
    @Subscribe
    fun onEventMainAgoraVideo(event: com.mshy.VInterestSpeed.common.bean.notification.NotificationEvent) {
        if (!isBackground(this)) {
            return

        }
        var videoRequestBean = event.customNotification
        if (videoRequestBean != null) {
            if (11 == videoRequestBean.id || 21 == videoRequestBean.id) {
                val messageNotice: Boolean? =
                    SpUtils.getBoolean(SpKey.NEW_MESSAGE_NOTICE, true)
                if (messageNotice == true) {
                    if (!UserManager.isVideo) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val channelId = "customcaicai"
                            val channelName = "山海甜缘系统通知"
                            val importance = NotificationManager.IMPORTANCE_HIGH
                            NotificationUtil.createNotificationChannel(
                                this,
                                channelId,
                                R.string.app_name,
                                R.string.common_vqu_notification,
                                importance
                            )
                        }
                        val intent: Intent = Intent(
                            this,
                            com.mshy.VInterestSpeed.uikit.receiver.NotificationClickReceiver::class.java
                        )

                        intent.putExtra("linkType", 0)
                        intent.putExtra("linkUrl", "main")
                        intent.putExtra("VideoRequestBean", videoRequestBean)
                        UserManager.videoRequestBean = videoRequestBean
                        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                            this,
                            0,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val manager: NotificationManager =
                            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                        val customNotification: Notification =
                            Notification.Builder(this, "customcaicai")
                                .setContentTitle(videoRequestBean.data.title)
                                .setContentText(videoRequestBean.data.content)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(
                                    BitmapFactory.decodeResource(
                                        resources,
                                        R.mipmap.ic_launcher
                                    )
                                )
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent) //设置点击响应
                                .build()
                        manager.notify(1, customNotification)
                    }

                }

            }
        }
    }

    var delay: Boolean = true

    @Subscribe
    fun showDialogWhenUserInCallEvent(event: com.mshy.VInterestSpeed.common.bean.ShowDialogWhenUserInCallEvent) {
        com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
            .setTitle("余额不足")
            .setContent("金币余额不足1分钟，请及时充值避免错过缘分！")
            .setLeftText(getString(R.string.agora_vqu_dialog_left_txt))
            .setRightText(getString(R.string.agora_vqu_dialog_right_txt))
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object : com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {}
                override fun onRight(dialogFragment: DialogFragment) {
                    mPayViewModel.showRechargeDialog(supportFragmentManager)
                }
            })
            .show(supportFragmentManager)
    }

    private var lastClickTime: Long = 0
    protected open fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - lastClickTime >= 500) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }


//    protected fun checkInstanceActivityAndGoTo() {
//        val topActivity = ActivityStackManager.getCurrentActivity()
//        Log.d("aaaa", "checkInstanceActivityAndGoTo: "+topActivity)
//        if (topActivity != null) {
//            if (topActivity::class.java.name != this::class.java.name) {
//                Log.d("aaaa", "checkInstanceActivityAndGoTo: 1")
//                val stacks = ActivityStackManager.activitySingleInstanceStack
//                for (stack in stacks) {
//                    Log.d("aaaa", "checkInstanceActivityAndGoTo: 2")
//                    if (stack == topActivity) {
//                        Log.d("aaaa", "checkInstanceActivityAndGoTo: 3")
//                        ActivityStackManager.removeSingleInstanceActivityToStack(stack)
//                        startActivity(Intent(this, stack::class.java))
//                    }
//                }
//            }
//        }
//    }
}