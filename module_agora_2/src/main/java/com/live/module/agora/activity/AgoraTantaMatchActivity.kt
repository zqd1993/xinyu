package com.live.module.agora.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkfuns.logutils.LogUtils
import com.ellison.glide.translibrary.ImageUtils
import com.ellison.glide.translibrary.base.LoaderBuilder
import com.gyf.immersionbar.ImmersionBar
import com.live.module.agora.R
import com.live.module.agora.databinding.AgoraTantaMatchingActivityBinding
import com.live.module.agora.vm.AgoraTantaMatchingViewModel
import com.live.module_agora.adapter.tantaMatchRecordAdapter
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.AgoraMtachCallBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.CommonBackstageEvent
import com.mshy.VInterestSpeed.common.event.ExitMatchEvent
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonRechargeDialog
import com.mshy.VInterestSpeed.common.ui.view.notification.SwipeMessageNotificationManager
import com.mshy.VInterestSpeed.common.utils.UiUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.tencent.mmkv.MMKV
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit


/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/6/8 17:58
 *
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Agora2.AgoraTantaMatchingActivity)
class AgoraTantaMatchActivity :
    BaseActivity<AgoraTantaMatchingActivityBinding, AgoraTantaMatchingViewModel>() {

    override val mViewModel: AgoraTantaMatchingViewModel by viewModels()


    override fun setStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .titleBarMarginTop(mBinding.tantaTitleBar)
            .fitsSystemWindows(false)
            .init()
    }

    override fun initObserve() {
        mViewModel.tantaExitMatchCode.observe(this) {
            if (it == 0) {
                finish()
            }
        }

        mViewModel.tantaVideoTantaCallBean.observe(this) {
            if (it != null) {
                try {
                    if (matchType == 1 && "voice" == it.type) {//语音
                        if (tantaMediaPlayer?.isLooping == true) {
                            tantaMediaPlayer?.pause()
                        }
                        tantaMediaPlayerDing?.start()
                        // 线程切换
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                delay(500)
                            }
                            //刷新UI，task1
                            tantaMediaPlayerDing?.pause()
                            //  ActivityStackManager.getCurrentActivity()?.javaClass?.simpleName?.toast()
                            if (isFront) {
                                if ("video" == it.type) {
                                    ARouter.getInstance()
                                        .build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                        .withString(RouteKey.SOCKET_URL, it.socket_url ?: "")
                                        .withBoolean("isCaller", true)
                                        .withBoolean("isMatch", true)
                                        .withParcelable("CallBean", it)
                                        .navigation()
                                } else {
                                    ARouter.getInstance()
                                        .build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                                        .withString(RouteKey.SOCKET_URL, it.socket_url ?: "")
                                        .withBoolean("isCaller", true)
                                        .withBoolean("isMatch", true)
                                        .withParcelable("CallBean", it)
                                        .navigation()
                                }
                            } else {
                                LogUtils.d("不在当前Activity")
                            }
                        }
                    } else if (matchType == 0 && "video" == it.type) {
                        if (tantaMediaPlayer?.isLooping == true) {
                            tantaMediaPlayer?.pause()
                        }
                        tantaMediaPlayerDing?.start()
                        // 线程切换
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                delay(500)
                            }
                            //刷新UI，task1
                            tantaMediaPlayerDing?.pause()
                            //  ActivityStackManager.getCurrentActivity()?.javaClass?.simpleName?.toast()
                            if (isFront) {
                                if ("video" == it.type) {
                                    ARouter.getInstance()
                                        .build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                        .withString(RouteKey.SOCKET_URL, it.socket_url ?: "")
                                        .withBoolean("isCaller", true)
                                        .withBoolean("isMatch", true)
                                        .withParcelable("CallBean", it)
                                        .navigation()
                                } else {
                                    ARouter.getInstance()
                                        .build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                                        .withString(RouteKey.SOCKET_URL, it.socket_url ?: "")
                                        .withBoolean("isCaller", true)
                                        .withBoolean("isMatch", true)
                                        .withParcelable("CallBean", it)
                                        .navigation()
                                }
                            } else {
                                LogUtils.d("不在当前Activity")
                            }
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }

        mViewModel.tantaMatchCode.observe(this) {
            if (it == 1003 || it == 1002) {
                //  "余额不足，请先充值".toast()
                val rechargeDialog = CommonRechargeDialog()
                rechargeDialog.show(supportFragmentManager, "")
            } else if (it == 1) {
                mBinding.tantaSllMatchStatus.visibility = View.VISIBLE
                mBinding.tantaCountDownMatch.visibility = View.GONE
                if (UserSpUtils.getUserBean()?.gender == 2) {
                    mBinding.tantaTvMatch.text = "继续速配"
                } else {
                    mBinding.tantaTvMatch.text = "后台等待"
                }
                // countDown(4)
            }
        }
        mViewModel.tantaMatchRecord.observe(this) {
            if (it != null) {
                if (it.log?.size != 0 && it.log != null) {
                    if ((tantaMatchRecordAdapter?.data?.size ?: 0) == 0) {
                        tantaMatchRecordAdapter?.setList(it.log)
                        mBinding.tantaRvMatchRecord.adapter = tantaMatchRecordAdapter
                        startAuto()
                    }
                }
            }
        }

    }

    override fun initRequestData() {
        UserManager.isMatch = true
        if (Settings.canDrawOverlays(BaseApplication.context)) {
            SwipeMessageNotificationManager.getInstance(this@AgoraTantaMatchActivity)
                .removeNotificationAll()
        }
        LogUtils.d("tantaAddMatching-initRequestData()")
        if (UserSpUtils.getUserBean()?.gender == 2) {
            if (matchType == 1) { // video
                mViewModel.tantaAddMatching("voice")
            } else {
                mViewModel.tantaAddMatching("video")
            }

        }
        if (UserSpUtils.getUserBean()?.gender == 1) {
            mBinding.tantaSllMatchStatus.visibility = View.VISIBLE
            mBinding.tantaCountDownMatch.visibility = View.GONE
            mBinding.tantaTvMatch.text = "后台等待"
        }
    }

    @Subscribe
    fun tantaMatchVoiceVideo(call: AgoraMtachCallBean) {
        if ("failed" == call.status) {
            "匹配失败,重新速配".toast()
            mBinding.tantaSllMatchStatus.visibility = View.VISIBLE
            //countDown(4)
        } else {
            mViewModel.tantaReceiveCall("receive", call.match_id.toString())
        }
    }


    private fun tantaSetAnimation() {

        //动画
        //沿x轴放大
        val scaleXAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(mBinding.tantaHeadLayout, "scaleX", 1f, 1.3f, 1f)
        //沿y轴放大
        val scaleYAnimator: ObjectAnimator =
            ObjectAnimator.ofFloat(mBinding.tantaHeadLayout, "scaleY", 1f, 1.3f, 1f)
        //沿x轴放大
        val scaleXAnimatorType: ObjectAnimator =
            ObjectAnimator.ofFloat(mBinding.tantaIvMatchType, "scaleX", 1f, 1.3f, 1f)
        //沿y轴放大
        val scaleYAnimatorType: ObjectAnimator =
            ObjectAnimator.ofFloat(mBinding.tantaIvMatchType, "scaleY", 1f, 1.3f, 1f)
        scaleXAnimatorType.repeatCount = ValueAnimator.INFINITE
        scaleYAnimatorType.repeatCount = ValueAnimator.INFINITE
        scaleXAnimator.repeatCount = ValueAnimator.INFINITE
        scaleYAnimator.repeatCount = ValueAnimator.INFINITE
        tantaSet = AnimatorSet()
        //同时沿X,Y轴放大，且改变透明度，然后移动  .with(scaleXAnimatorType).with(scaleYAnimatorType)
        tantaSet?.play(scaleXAnimator)?.with(scaleYAnimator)
        //都设置3s，也可以为每个单独设置
        tantaSet?.duration = 2000
        tantaSet?.interpolator = AccelerateDecelerateInterpolator()
        tantaSet?.start()

    }


    private var tantaMediaPlayer: MediaPlayer? = null
    private var tantaMediaPlayerDing: MediaPlayer? = null
    private var tantaVoiceStatus = true
    private var tantaMatchRecordAdapter: tantaMatchRecordAdapter? = null
    private var matchType = 0
    private var tantaSet: AnimatorSet? = null
    override fun AgoraTantaMatchingActivityBinding.initView() {
        ImmersionBar.with(this@AgoraTantaMatchActivity).titleBarMarginTop(tantaTitleBar).init()
        matchType = intent.getIntExtra("matchType", 0)
        if (matchType == 1) { // video
            tantaMatchText.text = "语音速配"
            tantaIvMatchType.setImageResource(R.mipmap.agora_match_type_voice)
        } else {
            tantaMatchText.text = "视频速配"
            tantaIvMatchType.setImageResource(R.mipmap.agora_match_type_video)
        }
        tantaMediaPlayer =
            MediaPlayer.create(this@AgoraTantaMatchActivity, R.raw.matching)
        tantaMediaPlayer?.isLooping = true

        tantaMediaPlayerDing = MediaPlayer.create(this@AgoraTantaMatchActivity, R.raw.match_sucess)
        tantaMediaPlayerDing?.isLooping = false

        if (UserSpUtils.getUserBean()?.gender == 1) {
            tantaMatchShuoming.text = "正在为您匹配有缘小哥哥，加速中~"
        } else {
            tantaMatchShuoming.text = "正在为您匹配有缘小姐姐，加速中~"
        }

        val builderConfig = LoaderBuilder()
            .round(UiUtils.dip2px(this@AgoraTantaMatchActivity, 40f).toFloat())
            .circle(false)
            .width(UiUtils.dip2px(this@AgoraTantaMatchActivity, 80f))
            .height(UiUtils.dip2px(this@AgoraTantaMatchActivity, 80f))
            .scaleType(ImageView.ScaleType.CENTER_CROP)
            .round(
                floatArrayOf(
                    UiUtils.dip2px(this@AgoraTantaMatchActivity, 40f).toFloat(),
                    UiUtils.dip2px(this@AgoraTantaMatchActivity, 40f).toFloat(),
                    UiUtils.dip2px(this@AgoraTantaMatchActivity, 40f).toFloat(),
                    UiUtils.dip2px(this@AgoraTantaMatchActivity, 40f).toFloat()
                )
            )
            .borderColor(
                ContextCompat.getColor(
                    this@AgoraTantaMatchActivity,
                    R.color.color_FFFFFF
                )
            )
            .borderWidth(UiUtils.dip2px(this@AgoraTantaMatchActivity, 2f))
            .load(NetBaseUrlConstant.IMAGE_URL + UserSpUtils.getUserBean()?.avatar)
        ImageUtils.getInstance().bind(mBinding.tantaHeadPortrait, builderConfig)

        tantaCloseMatch.setOnClickListener {
            if (UserSpUtils.getUserBean()?.gender == 1) {
                if (MMKV.defaultMMKV()?.getInt("tvFutureNoTip", 1) == 2) {
                    EventBus.getDefault().post(
                        CommonBackstageEvent(
                            matchType,
                            0
                        )
                    )
                    if (matchType == 1) { // video

                        mViewModel.tantaExitMatch("voice")
                    } else {

                        mViewModel.tantaExitMatch("video")
                    }

                } else {
                    CommonHintDialog()
                        .setTitle("")
                        .setContent(
                            "系统正在为您极速匹配中！\n" +
                                    "\n" +
                                    "别错过缘分，小哥哥~"
                        )
                        .setLeftText("继续匹配")
                        .setRightText("放弃缘分")
                        .setContentSize(15)
                        .setShowFutureNoTip(true)
                        .setContentGravity(Gravity.CENTER)
                        .setOnClickListener(object : CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {

                            }

                            override fun onRight(dialogFragment: DialogFragment) {
                                UserManager.isMatch = false
                                EventBus.getDefault().post(
                                    CommonBackstageEvent(
                                        matchType,
                                        0
                                    )
                                )
                                if (matchType == 1) { // video
                                    mViewModel.tantaExitMatch("voice")
                                } else {
                                    mViewModel.tantaExitMatch("video")
                                }
                            }
                        })
                        .show(supportFragmentManager)
                }
            } else {
                CommonHintDialog()
                    .setTitle("")
                    .setContent(
                        "女神正在赶来的的路上\n" +
                                "\n" +
                                "别错过缘分，再等等呐~"
                    )
                    .setLeftText("继续匹配")
                    .setRightText("放弃缘分")
                    .setContentSize(15)

                    .setContentGravity(Gravity.CENTER)
                    .setOnClickListener(object : CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {}
                        override fun onRight(dialogFragment: DialogFragment) {
                            UserManager.isMatch = false
                            if (matchType == 1) { // video
                                mViewModel.tantaExitMatch("voice")
                            } else {
                                mViewModel.tantaExitMatch("video")
                            }
                        }
                    })
                    .show(supportFragmentManager)
            }
        }

        mBinding.tantaSllMatchStatus.setOnClickListener {

            if (disposable != null) {
                if (!disposable!!.isDisposed) {
                    disposable?.dispose()
                }
            }
            if (UserSpUtils.getUserBean()?.gender == 1) {
                EventBus.getDefault().post(
                    CommonBackstageEvent(
                        matchType,
                        1
                    )
                )
                finish()
            } else {
                LogUtils.d("tantaAddMatching-tantaSllMatchStatusOnClick")
                if (matchType == 1) {
                    mViewModel.tantaAddMatching("voice")
                } else {
                    mViewModel.tantaAddMatching("video")
                }
                tantaSllMatchStatus.visibility = View.GONE
            }
        }

        flTantaRingClose.setOnClickListener {
            if (tantaVoiceStatus) { // 关闭背景音
                tantaVoiceStatus = false
                tantaCloseVoice.visibility = View.VISIBLE
                tantaRingAmin.visibility = View.GONE
                tantaRingAmin.stopAnimation()
//                tantaCloseVoice.setImageResource(R.mipmap.agora_tanta_close_match_voice)
                tantaMediaPlayer?.pause()
            } else {
                tantaVoiceStatus = true
                tantaCloseVoice.visibility = View.GONE
                tantaRingAmin.visibility = View.VISIBLE
                tantaRingAmin.startAnimation()
//                tantaCloseVoice.setImageResource(R.mipmap.agora_tanta_open_match_voice)
                tantaMediaPlayer?.start()
            }
        }

        if (UserSpUtils.getUserBean()?.gender == 1) {
            tantaSllMatchDesc.visibility = View.VISIBLE
        } else {
            tantaSllMatchDesc.visibility = View.GONE
        }
        tantaSllMatchDesc.setOnClickListener {
            CommonHintDialog()
                .setTitle("")
                .setContent("使用速配功能时，平台将为你匹配缘分小哥哥，男用户的付费标准按照官方的音视速配费用结算.")
                .setSingleButton(true)
                .setContentSize(15)
                .setContentGravity(Gravity.CENTER)
                .setOnSingleClickListener { }.show(supportFragmentManager)
        }


        tantaMatchRecordAdapter = tantaMatchRecordAdapter()

        val layoutManager = object : LinearLayoutManager(this@AgoraTantaMatchActivity) {

            override fun smoothScrollToPosition(
                recyclerView: RecyclerView?,
                state: RecyclerView.State?,
                position: Int
            ) {
                val linearSmoothScroller = object : LinearSmoothScroller(recyclerView?.context) {
                    //返回滑动一个pixel需要多少毫秒
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                        //这边可以自定义进行控制速度
                        return 3f / (displayMetrics?.density ?: 1f)
                    }
                }
                linearSmoothScroller.targetPosition = position
                startSmoothScroll(linearSmoothScroller)
            }

        }
        tantaRvMatchRecord.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {


            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        tantaRvMatchRecord.layoutManager = layoutManager

        val rotateAnimation = RotateAnimation(0f, -360f, 0f, dp2px(150f).toFloat())
        rotateAnimation.duration = 2000
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = LinearInterpolator()
        tantaMatchScan.startAnimation(rotateAnimation)

        tantaSetAnimation()
    }

    var mAutoTask: Disposable? = null
    private fun startAuto() {
        if (mAutoTask != null && (mAutoTask?.isDisposed != false))
            mAutoTask?.dispose()
        //此处的3为当前显示的最后一个item
        mAutoTask = Observable.interval(1, 2, TimeUnit.SECONDS).subscribe {
            mBinding.tantaRvMatchRecord.smoothScrollToPosition((3 + it).toInt())
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //  startAuto()
    }

    private fun stopAuto() {
        if (mAutoTask != null && (mAutoTask?.isDisposed == true)) {
            mAutoTask?.dispose()
            mAutoTask = null
        }
    }


    private var disposable: Disposable? = null
    private fun countDown(countDownSeconds: Long) {
        if (disposable != null && !disposable!!.isDisposed) {    // 在创建计时器时，先检查之前的计时器是否还存在，如果存在，则dispose掉。
            disposable!!.dispose()
        }
        disposable = Flowable.intervalRange(0, countDownSeconds, 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                //    toast(""+it)
                mBinding.tantaCountDownMatch.text = "${3 - it}s后自动参与速配"
            }
            .doOnComplete {
                mBinding.tantaSllMatchStatus.visibility = View.GONE
                mBinding.tantaCountDownMatch.visibility = View.GONE
                LogUtils.d("tantaAddMatching-countDown")
                if (matchType == 1) { // video
                    mViewModel.tantaAddMatching("voice")
                } else {
                    mViewModel.tantaAddMatching("video")
                }
            }
            .subscribe()
    }

    override fun onResume() {
        super.onResume()

        UserManager.isMatch = true
        LogUtils.d("tantaAddMatching-onRestart")
        if (!UserManager.isMatchReturn) {
            UserManager.isMatchReturn = true

            if (UserSpUtils.getUserBean()?.gender == 2) {
                mBinding.tantaTvMatch.text = "自动速配"
                mBinding.tantaSllMatchStatus.visibility = View.VISIBLE
                mBinding.tantaCountDownMatch.visibility = View.VISIBLE
                countDown(4)
            } else {
                mBinding.tantaTvMatch.text = "后台等待"
                if (matchType == 1) { // video
                    mViewModel.tantaAddMatching("voice")
                } else {
                    mViewModel.tantaAddMatching("video")
                }
            }

            LogUtils.d("tantaAddMatching-onRestart false")

        } else {
            LogUtils.d("tantaAddMatching-onRestart true")
        }



        if (tantaVoiceStatus) {
            mBinding.tantaRingAmin.startAnimation()

            mBinding.tantaMatchApng.playAnimation()
            if (tantaMediaPlayer == null) {
                tantaMediaPlayerDing =
                    MediaPlayer.create(this@AgoraTantaMatchActivity, R.raw.match_sucess)
                tantaMediaPlayerDing?.isLooping = false
            }
            tantaMediaPlayer?.start()
            tantaSet?.resume()
        }
    }

    override fun networkConnectChange(isConnected: Boolean) {
        toast(if (isConnected) "网络已连接" else "网络已断开、请重新进入")
    }

    override fun onPause() {
        super.onPause()
        if (tantaVoiceStatus) {
            mBinding.tantaMatchApng.pauseAnimation()
            mBinding.tantaRingAmin.stopAnimation()
            tantaMediaPlayer?.pause()
            tantaSet?.pause()
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (UserSpUtils.getUserBean()?.gender == 1) {
                if (MMKV.defaultMMKV()?.getInt("tvFutureNoTip", 1) == 2) {
                    if (matchType == 1) { // video
                        mViewModel.tantaExitMatch("voice")
                    } else {
                        mViewModel.tantaExitMatch("video")
                    }
                } else {
                    CommonHintDialog()
                        .setTitle("")
                        .setContent(
                            "系统正在为您极速匹配中！\n" +
                                    "\n" +
                                    "别错过缘分，小哥哥~"
                        )
                        .setLeftText("继续匹配")
                        .setRightText("放弃缘分")
                        .setShowFutureNoTip(true)
                        .setContentSize(15)
                        .setContentGravity(Gravity.CENTER)
                        .setOnClickListener(object : CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {}
                            override fun onRight(dialogFragment: DialogFragment) {
                                UserManager.isMatch = false
                                EventBus.getDefault().post(
                                    CommonBackstageEvent(
                                        matchType,
                                        0
                                    )
                                )
                                if (matchType == 1) { // video
                                    mViewModel.tantaExitMatch("voice")
                                } else {
                                    mViewModel.tantaExitMatch("video")
                                }

                            }
                        })
                        .show(supportFragmentManager)
                }
            } else {
                CommonHintDialog()
                    .setTitle("")
                    .setContent(
                        "女神正在赶来的的路上\n" +
                                "\n" +
                                "别错过缘分，再等等呐~"
                    )
                    .setLeftText("继续匹配")
                    .setRightText("放弃缘分")
                    .setContentSize(15)

                    .setContentGravity(Gravity.CENTER)
                    .setOnClickListener(object : CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {}
                        override fun onRight(dialogFragment: DialogFragment) {
                            UserManager.isMatch = false
                            if (matchType == 1) { // video
                                mViewModel.tantaExitMatch("voice")
                            } else {
                                mViewModel.tantaExitMatch("video")
                            }
                        }
                    })
                    .show(supportFragmentManager)
            }
            return true;
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        UserManager.isMatch = false
        if (matchType == 1) { // video
            mViewModel.tantaExitMatch("voice")
        } else {
            mViewModel.tantaExitMatch("video")
        }
        stopAuto()
        super.onDestroy()
    }

    @Subscribe
    fun rechargeDestroy(event: ExitMatchEvent) {
        if (isFront || event.type == 1) {
            UserManager.isMatch = false
            if (matchType == 1) { // video
                mViewModel.tantaExitMatch("voice")
            } else {
                mViewModel.tantaExitMatch("video")
            }
        }
    }


}
