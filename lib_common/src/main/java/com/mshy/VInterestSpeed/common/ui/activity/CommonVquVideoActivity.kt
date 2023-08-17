package com.mshy.VInterestSpeed.common.ui.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.view.video.EmptyControlVideo
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PAUSE
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView.CURRENT_STATE_PLAYING
import org.greenrobot.eventbus.EventBus


/**
 * author: Tany
 * date: 2022/4/22
 * description:
 */
@Route(path = RouteUrl.Common.CommonVquVideoActivity)
class CommonVquVideoActivity : FragmentActivity() {
    var video: EmptyControlVideo? = null
    var ivPlay: ImageView? = null
    var ivBack: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
            .reset()
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init();
        setContentView(R.layout.common_vqu_activity_video)
        var url = intent.getStringExtra("video_url")
        var coverUrl = intent.getStringExtra("cover_url")
        var showDel = intent.getBooleanExtra("del", false)
        ivPlay = findViewById<ImageView>(R.id.iv_play)
        ivBack = findViewById<ImageView>(R.id.iv_back)
        video = findViewById<EmptyControlVideo>(R.id.video)
        var ivDel = findViewById<TextView>(R.id.tv_del)
        ivDel.visibility = if (showDel) {
            View.VISIBLE
        } else {
            View.GONE
        }
        startVideo(url, coverUrl)
        ivBack?.setOnClickListener { finish() }
        ivDel.setOnClickListener {
            com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
                .setTitle(getString(R.string.setting_tip))
                .setContent("是否删除视频?")
                .setLeftText("取消")
                .setRightText("删除")
                .setContentSize(15)
                .setContentGravity(Gravity.CENTER)
                .setOnClickListener(object : com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                    override fun onLeft(dialogFragment: DialogFragment) {}
                    override fun onRight(dialogFragment: DialogFragment) {
                        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.DelVideoEvent())
                        finish()
                    }
                })
                .show(supportFragmentManager)

        }

    }

    private fun startVideo(url: String?, coverUrl: String?) {
        video?.isLooping = true
        video?.setUp(url, true, "")
        video?.startPlayLogic()
        video?.setVideoAllCallBack(object : VideoAllCallBack {
            override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onClickResume(url: String?, vararg objects: Any?) {
                ivPlay?.visible()
            }

            override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onStartPrepared(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartIcon(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartThumb(url: String?, vararg objects: Any?) {
                ivPlay?.gone()
            }

            override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartError(url: String?, vararg objects: Any?) {
            }

            override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onPrepared(url: String?, vararg objects: Any?) {
                GSYVideoManager.instance().isNeedMute = UserManager.isVideo
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
            }

            override fun onComplete(url: String?, vararg objects: Any?) {
            }

            override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
            }

            override fun onClickBlank(url: String?, vararg objects: Any?) {
            }

            override fun onClickStop(url: String?, vararg objects: Any?) {
            }

            override fun onClickSeekbar(url: String?, vararg objects: Any?) {
            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
            }

            override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
            }


        })
        video?.currentPlayer?.setGSYStateUiListener {
            when (it) {
                CURRENT_STATE_PLAYING -> {
                    ivPlay?.gone()
                }
                CURRENT_STATE_PAUSE -> {
                    ivPlay?.visible()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        video?.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        video?.onVideoPause()
    }

    override fun finish() {
        super.finish()
        video?.release()
    }


}