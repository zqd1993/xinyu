package com.live.module.dynamic.activity

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.gyf.immersionbar.ImmersionBar
import com.live.module.dynamic.R
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.event.ScrollToPositionEvent
import com.mshy.VInterestSpeed.common.ui.view.photoDrag.DragRelativeLayout
import com.mshy.VInterestSpeed.common.ui.view.photoDrag.OnDragListener
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
class DynamicTantaVideoActivity : Activity() {
    var mDragLayout: DragRelativeLayout? = null
    var video: EmptyControlVideo? = null
    var ivPlay:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this)
            .reset()
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init();
        setContentView(R.layout.dynamic_tanta_activity_video)
        val loc = intent.getIntArrayExtra("region")
        var url = intent.getStringExtra("video_url")
        var coverUrl = intent.getStringExtra("cover_url")
        val pos = intent.getIntExtra("position", 0)
        mDragLayout = findViewById<DragRelativeLayout>(R.id.rl_drag)
        ivPlay = findViewById<ImageView>(R.id.iv_play)
        video = findViewById<EmptyControlVideo>(R.id.video)
        if (loc != null) {
            mDragLayout?.setTransitionsRegion(loc[0], loc[1], loc[2], loc[3], loc[4], loc[5])
        }
        val index = intent.getIntExtra("index", 0)
        if (index == 0) {
            mDragLayout?.startTransitions()
            EventBus.getDefault().post(
                ScrollToPositionEvent(
                    pos,
                    mDragLayout!!.getDuration(),
                    index == 0,
                    loc
                ) { l, t, r, b, w, h -> mDragLayout?.setTransitionsRegion(l, t, r, b, w, h) }
            )
        }
        mDragLayout?.setOnoDragListener(object : OnDragListener() {
            override fun onStartDrag() {
                super.onStartDrag()
                video?.onVideoPause()
            }

            override fun onStartEnter(outOfBound: Boolean) {
                super.onStartEnter(outOfBound)
            }

            override fun onRelease(isResume: Boolean) {
                super.onRelease(isResume)
            }

            override fun onEndExit() {
                super.onEndExit()
                finish()
            }

            override fun onEndEnter() {
                super.onEndEnter()
            }

            override fun onStartExit(outOfBound: Boolean) {
                super.onStartExit(outOfBound)
            }

            override fun onEndResume() {
                super.onEndResume()
                video?.onVideoResume()
            }
        })
        startVideo(url, coverUrl)
    }

    private fun startVideo(url: String?, coverUrl: String?) {
        video?.isLooping=true
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
            when(it){
                CURRENT_STATE_PLAYING->{
                    ivPlay?.gone()
                }
                CURRENT_STATE_PAUSE->{
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
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        mDragLayout?.endTransitions();
    }

}