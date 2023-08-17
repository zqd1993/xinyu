package com.mshy.VInterestSpeed.common.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.aliyun.common.utils.ToastUtil
import com.aliyun.svideosdk.common.struct.common.VideoQuality
import com.aliyun.svideosdk.crop.AliyunICrop
import com.aliyun.svideosdk.crop.CropCallback
import com.aliyun.svideosdk.crop.CropParam
import com.aliyun.svideosdk.crop.impl.AliyunCropCreator
import com.gyf.immersionbar.ImmersionBar
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.ext.initClose
import com.waynell.videorangeslider.RangeSlider
import com.waynell.videorangeslider.RangeSlider.OnRangeChangeListener
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/4/24
 * description:
 */
class CommonVquVideoCropActivity : AppCompatActivity(), CropCallback {
    /**
     * 裁剪参数
     */
    private var mCrop: AliyunICrop? = null



    //需要裁剪视频的文件所在目录
    private var mVideoPath: String? = null

    //文件名:带后缀
    private var mFileName: String? = null

    //是否需要返回数据
    private var mIsNeedReturn = false
    private var mCropParam: CropParam? = null
    private var mProgressDialog: com.mshy.VInterestSpeed.common.ui.dialog.DownProgressDialog? = null
    private var mVideoView: VideoView? = null
    private var mTvCropTime: TextView? = null
    private var mRangeSlider: RangeSlider? = null

    //视频裁剪的开始时间 单位 毫秒
    private var mStartTime: Long = 0

    //视频裁剪的结束时间 单位 毫秒
    private var mEndTime: Long = 0

    //是否是视频第一次初始化
    private var isVideoInit = true
    private var  mOutPutPath:String=""

    //视频总时长
    private var mVideoDuration = 0
    private var mMaxCropTime = (60 * 1000).toLong()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //裁剪输出的文件所在目录
        mOutPutPath = this@CommonVquVideoCropActivity.externalCacheDir.toString()+ "/vqu/cache/crop/"
        ImmersionBar.with(this)
            .statusBarColor(R.color.base_colorPrimary)
            .statusBarDarkFont(true)
            .navigationBarColor(R.color.base_colorPrimary).init()
        setContentView(R.layout.common_vqu_activity_video_crop)
        mMaxCropTime = intent.getLongExtra(MAX_CROP_TIME, (60 * 1000).toLong())
        val time = mMaxCropTime / 1000
        var mToolBar: Toolbar = findViewById(R.id.includeTitle)
        mToolBar.initClose(
            "视频最长" + time + "秒",
            "下一步",
            R.color.color_273145,
            R.mipmap.ic_back,
            {
                finish()
            },
            {
                if ((mEndTime - mStartTime)/1000 > mMaxCropTime/1000) {
                    ToastUtil.showToast(this@CommonVquVideoCropActivity, "裁剪视频时间不能超过" + time + "秒")
                    return@initClose
                }
                if (mCropParam != null && mCrop != null) {
                    //这里单位是微秒  所以乘1000
                    mCropParam!!.startTime = mStartTime * 1000
                    mCropParam!!.endTime = mEndTime * 1000
                    mCrop!!.setCropParam(mCropParam)
                    //开始裁剪
                    mCrop!!.startCrop()
                }
            })
        parseIntent()
        initView()
        initVideoView()
        mCrop = AliyunCropCreator.createCropInstance(this)
        mCrop?.setCropCallback(this)
        mCropParam = initCrop()
    }

    private fun initVideoView() {
        mVideoView!!.setVideoPath(mVideoPath + mFileName) //设置视频文件
        mVideoView!!.setOnPreparedListener { //视频加载完成,准备好播放视频的回调
            mVideoDuration = mVideoView!!.duration
            if (isVideoInit) {
                mEndTime = mVideoView!!.duration.toLong()
            }
            mVideoView!!.start()
            if (mVideoView!!.isPlaying) {
                mVideoView!!.seekTo(mStartTime.toInt())
            }
            //开启线程监听事件
            startPlayListener()
        }
        mVideoView!!.setOnCompletionListener { //视频播放完成后的回调
            //播放完成从设置的开始时间开始播放
            isVideoInit = false
            mVideoView!!.resume()
        }
        mVideoView!!.setOnErrorListener { mp, what, extra ->
            //异常回调
            false //如果方法处理了错误，则为true；否则为false。返回false或根本没有OnErrorListener，将导致调用OnCompletionListener。
        }
        mVideoView!!.setOnInfoListener { mp, what, extra ->
            //信息回调
            //                what 对应返回的值如下
            //                public static final int MEDIA_INFO_UNKNOWN = 1;  媒体信息未知
            //                public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700; 媒体信息视频跟踪滞后
            //                public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3; 媒体信息\视频渲染\开始
            //                public static final int MEDIA_INFO_BUFFERING_START = 701; 媒体信息缓冲启动
            //                public static final int MEDIA_INFO_BUFFERING_END = 702; 媒体信息缓冲结束
            //                public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703; 媒体信息网络带宽（703）
            //                public static final int MEDIA_INFO_BAD_INTERLEAVING = 800; 媒体-信息-坏-交错
            //                public static final int MEDIA_INFO_NOT_SEEKABLE = 801; 媒体信息找不到
            //                public static final int MEDIA_INFO_METADATA_UPDATE = 802; 媒体信息元数据更新
            //                public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901; 媒体信息不支持字幕
            //                public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902; 媒体信息字幕超时
            false //如果方法处理了信息，则为true；如果没有，则为false。返回false或根本没有OnInfoListener，将导致丢弃该信息。
        }
    }

    private fun startPlayListener() {
        object : Thread() {
            override fun run() {
                try {
                    while (mVideoView!!.isPlaying) {
                        // 如果正在播放，没0.5.毫秒更新一次进度条
                        isVideoInit = false
                        val current = mVideoView!!.currentPosition
                        if (current >= mEndTime) {
                            mVideoView!!.seekTo(mStartTime.toInt())
                        }
                        sleep(500)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun initView() {
        mVideoView = findViewById(R.id.video_view)
        mRangeSlider = findViewById(R.id.range_slider)
        mTvCropTime = findViewById(R.id.tv_crop_time)
        mRangeSlider?.setRangeChangeListener(OnRangeChangeListener { rangeSlider, intLeftPinIndex, intRightPinIndex ->
            if (mVideoDuration == 0) {
                return@OnRangeChangeListener
            }
            val time = (mVideoDuration / 100).toFloat()
            //说明开始时间的进度条无变化 不设置进度
            if (!(mStartTime == (intLeftPinIndex * time).toLong())) {
                mStartTime = (intLeftPinIndex * time).toLong()
                mVideoView?.seekTo(mStartTime.toInt())
            }
            mStartTime = (intLeftPinIndex * time).toLong()
            mEndTime = (intRightPinIndex * time).toLong()
            val cropTime = (mEndTime - mStartTime).toInt() / 1000
            mTvCropTime?.setText("当前截取时长:" + cropTime + "秒")
            Log.e("onRangeChange", "onRangeChange: $mStartTime\nright:$mEndTime")
        })
    }

    private fun parseIntent() {
        mVideoPath = intent.getStringExtra(KEY_CROP_SAVE_VIDEO_PATH)
        mFileName = intent.getStringExtra(KEY_CROP_SAVE_VIDEO_NAME)
        mIsNeedReturn = intent.getBooleanExtra(KEY_CROP_RETURN, false)
    }

    public override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        if (mCrop != null) {
            mCrop!!.cancel()
            mCrop!!.dispose()
        }
        mVideoView!!.stopPlayback()
        mVideoView!!.suspend()
    }

    //------------------------------裁剪模块相关设置-------------------------------------
    private fun initCrop(): CropParam {
        val cropParam = CropParam()
        //清晰度设置
        cropParam.quality = VideoQuality.HD
        cropParam.videoPath = mVideoPath + mFileName
        cropParam.outputPath = mOutPutPath + mFileName
        //默认720P
        cropParam.outputHeight = com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil.screenHeight
        cropParam.outputWidth = com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil.screenWidth
        //默认帧率30
        cropParam.frameRate = 30
        return cropParam
    }

    //------------------------------裁剪模块相关回调-------------------------------------
    override fun onProgress(i: Int) {
        Log.e("result-crop", "onProgress$i")
        if (mProgressDialog == null) {
            mProgressDialog = com.mshy.VInterestSpeed.common.ui.dialog.DownProgressDialog(this)
            mProgressDialog!!.setProgress(0)
        } else {
            mProgressDialog!!.setProgress(i)
        }
        mProgressDialog!!.show()
    }

    override fun onError(i: Int) {
        if (mCrop != null) {
            mCrop!!.dispose()
        }
        if (mProgressDialog != null) {
            mProgressDialog = null
        }
        Log.e("result-crop", "onError$i")
    }

    override fun onComplete(l: Long) {
        if (mCrop != null) {
            mCrop!!.dispose()
        }
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
        val intent = Intent()
        intent.putExtra(KEY_CROP_SAVE_VIDEO_PATH, mOutPutPath)
        intent.putExtra(KEY_CROP_SAVE_VIDEO_NAME, mFileName)
        setResult(RESULT_OK, intent)
        finish()
        //        } else {
//            //完成之后跳转至发布界面
//            PublishVideoDynamicActivity.startActivity(VideoCropActivity.this, mOutPutPath, mFileName);
//            finishActivity();
//            Log.e("result-crop", "onComplete" + l);
//        }
    }

    override fun onCancelComplete() {
        if (mCrop != null) {
            mCrop!!.dispose()
        }
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
            mProgressDialog = null
        }
        Log.e("result-crop", "onCancelComplete")
    } //    public void onEventMainThread(DynamicVideoPublishSuccessEvent event) {

    //        finishActivity();
    //    }
    companion object {
        const val KEY_CROP_SAVE_VIDEO_PATH = "VideoPath"
        const val KEY_CROP_SAVE_VIDEO_NAME = "videoFileName"

        //是否需要返回数据
        const val KEY_CROP_RETURN = "isNeedReturn"

        //发布动态视频的请求code
        const val PUBLISH_REQUEST_CODE = 1001
        private const val MAX_CROP_TIME = "MAX_CROP_TIME"
        fun startActivity(context: Context, videoPath: String?, videoFileName: String?) {
            val intent = Intent(
                context,
                CommonVquVideoCropActivity::class.java
            )
            intent.putExtra(KEY_CROP_SAVE_VIDEO_PATH, videoPath)
            intent.putExtra(KEY_CROP_SAVE_VIDEO_NAME, videoFileName)
            context.startActivity(intent)
        }

        fun startActivityForResult(
            activity: Activity,
            videoPath: String?,
            videoFileName: String?,
            requestCode: Int
        ) {
            val intent = Intent(
                activity,
                CommonVquVideoCropActivity::class.java
            )
            intent.putExtra(KEY_CROP_SAVE_VIDEO_PATH, videoPath)
            intent.putExtra(KEY_CROP_SAVE_VIDEO_NAME, videoFileName)
            intent.putExtra(KEY_CROP_RETURN, true)
            activity.startActivityForResult(intent, requestCode)
        }

        fun startActivityForResult(
            activity: Activity,
            videoPath: String?,
            videoFileName: String?,
            maxCropTime: Long,
            requestCode: Int
        ) {
            val intent = Intent(
                activity,
                CommonVquVideoCropActivity::class.java
            )
            intent.putExtra(KEY_CROP_SAVE_VIDEO_PATH, videoPath)
            intent.putExtra(KEY_CROP_SAVE_VIDEO_NAME, videoFileName)
            intent.putExtra(KEY_CROP_RETURN, true)
            intent.putExtra(MAX_CROP_TIME, maxCropTime)
            activity.startActivityForResult(intent, requestCode)
        }
    }
}