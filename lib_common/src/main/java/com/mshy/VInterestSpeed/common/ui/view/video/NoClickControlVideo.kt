package com.mshy.VInterestSpeed.common.ui.view.video

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import com.mshy.VInterestSpeed.common.R
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * Created by Tany on 2021/11/15.
 * Desc:
 */


class NoClickControlVideo : StandardGSYVideoPlayer {
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag) {}
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun getLayoutId(): Int {
        return R.layout.common_empty_control_video
    }

    override fun touchSurfaceMoveFullLogic(absDeltaX: Float, absDeltaY: Float) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY)
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false
        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false
        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false
    }

    init {
        post {
            GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT)
            gestureDetector = GestureDetector(
                context.applicationContext,
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        return super.onDoubleTap(e)
                    }

                    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                        return super.onSingleTapConfirmed(e)

                    }

                    override fun onLongPress(e: MotionEvent) {
                        super.onLongPress(e)
                    }
                })
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return false

    }
}