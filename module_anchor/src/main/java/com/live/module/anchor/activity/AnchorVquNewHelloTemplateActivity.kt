package com.live.module.anchor.activity

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.anchor.R
import com.live.module.anchor.databinding.AnchorVquActivityNewHelloTemplateBinding
import com.live.module.anchor.vm.AnchorVquNewHelloTemplateViewModel
import com.live.vquonline.base.utils.StateLayoutEnum
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.*
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.activity.CommonVquVideoCropActivity
import com.mshy.VInterestSpeed.common.utils.*
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback
import com.netease.nimlib.sdk.media.record.RecordType
import dagger.hilt.android.AndroidEntryPoint
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * author: Lau
 * date: 2022/4/26
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Anchor.AnchorVquNewHelloTemplateActivity)
class AnchorVquNewHelloTemplateActivity :
    BaseActivity<AnchorVquActivityNewHelloTemplateBinding, AnchorVquNewHelloTemplateViewModel>(),
    IAudioRecordCallback {
    override val mViewModel: AnchorVquNewHelloTemplateViewModel by viewModels()

    private var state = 0
    private var nowTime = 0L
    private var maxTime = 15//最长时长暂定15秒

    private var minTime = 5

    private var started = false

    private var cancelled = false

    private var audioMessageHelper: AudioRecorder? = null

    private var length = 0

    private var mDesc = ""
    private var mVideoPath = ""
    private var mVoicePath = ""

    private var audioFile: File? = null

    private var audioType: Int = 1

    private var mImagePathUri: String = ""

    private val mLoadDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "提交中")
    }

    private val audioPlay: MediaPlayer by lazy {
        MediaPlayer()
    }

    private var countDownTimerHelper: CommonVquCountDownTimerHelper? = null


    override fun AnchorVquActivityNewHelloTemplateBinding.initView() {

        mBinding.tbAnchorVquNewHelloTemplateBar.toolbar.initClose(
            getString(R.string.anchor_vqu_new_template),
            rightStr = getString(R.string.common_vqu_submit),
            onClickRight = {
                clickCommit()
            },
            onBack = {
                finish()
            })

        mBinding.ivRecord.setViewClickListener {
            clickRecord()
        }

        initAudioRecord()

        initEvent()
    }

    private fun clickCommit() {
        val title = mBinding.etAnchorVquNewHelloTemplateContent.text.toString()

        if (mVoicePath.isNotEmpty()) {
            audioPlay.reset()
            audioPlay.setDataSource(mVoicePath)
            audioPlay.prepare()
            val time = audioPlay.duration / 1000
            if (time < minTime) {
                toast("录音最短不少于5秒")
                return
            }

            if (time > maxTime) {
                toast("录音最长不超过15秒")
                return
            }
            length = time
        }

        mViewModel.saveToSerice(title, length, mImagePathUri, mVideoPath, mVoicePath)
    }

    private fun initEvent() {

        audioPlay.setOnCompletionListener {
            stopPlayRecord()
        }

        mBinding.etAnchorVquNewHelloTemplateContent.addTextChangedListener {
            mBinding.tvAnchorVquNewHelloTemplateContentNum.text = getString(
                R.string.anchor_vqu_new_template_content_length,
                it?.length?.toString() ?: "0"
            )
            mDesc = it?.toString() ?: ""
            refreshCommit()
        }

        mBinding.tvAgain.setViewClickListener {
            againAudioRecord()
        }

        mBinding.clHelloTemplateImageParent.setViewClickListener {
            PermissionUtils.cameraPermission(
                this@AnchorVquNewHelloTemplateActivity,
                requestCallback = { allGranted, _, _ ->
                    if (allGranted) {
                        clickImgParent()
                    } else {
                        toast("无法获取图片选择权限")
                    }
                })
        }

        mBinding.ivAnchorVquNewHelloTemplateImgClear.setViewClickListener {
            mImagePathUri = ""
            mBinding.raivAnchorVquNewHelloTemplateImg.vquLoadImage("")
            mBinding.ivAnchorVquNewHelloTemplateImgAdd.visibility = View.VISIBLE
            mBinding.ivAnchorVquNewHelloTemplateImgClear.visibility = View.GONE
            refreshCommit()
        }

        mBinding.clAnchorVquNewHelloTemplateVideoParent.setViewClickListener {
            clickVideoParent()
        }

    }

    private fun clickVideoParent() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofVideo())
            .setSelectionMode(SelectModeConfig.SINGLE)
            .setImageEngine(com.mshy.VInterestSpeed.common.utils.GlideEngine.createGlideEngine())
            .isDisplayCamera(!UserManager.isVideo)
            .setRecordVideoMaxSecond(15)
            .setRecordVideoMinSecond(5)
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: java.util.ArrayList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        val localMedia = result[0]

                        mVideoPath = localMedia.realPath
                        val videoPathParent = mVideoPath.substring(
                            0,
                            mVideoPath.lastIndexOf("/")
                        ) + File.separator
                        val videoFileName =
                            mVideoPath.substring(mVideoPath.lastIndexOf("/") + 1)
                        if (localMedia.duration > 15 * 1000) {
                            CommonVquVideoCropActivity.startActivityForResult(
                                this@AnchorVquNewHelloTemplateActivity,
                                videoPathParent,
                                videoFileName,
                                15 * 1000L,
                                100
                            )
                        } else {
                            refreshCommit()
                            mBinding.raivAnchorVquNewHelloTemplateVideo.vquLoadVideoFirstFrame(
                                mVideoPath
                            )
                            mBinding.ivAnchorVquNewHelloTemplateVideoAdd.visibility = View.GONE
                            mBinding.ivAnchorVquNewHelloTemplateVideoClear.visibility = View.VISIBLE
                        }
                    }


                }

                override fun onCancel() {
                }
            })
    }

    private fun clickImgParent() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setSelectionMode(SelectModeConfig.SINGLE)
            .isDisplayCamera(!UserManager.isVideo)
            .isGif(false)
            .isWebp(false)
//            .setSandboxFileEngine(object : SandboxFileEngine {
//                override fun onStartSandboxFileTransform(
//                    context: Context?,
//                    isOriginalImage: Boolean,
//                    index: Int,
//                    media: LocalMedia?,
//                    listener: OnCallbackIndexListener<LocalMedia>?
//                ) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                        media?.sandboxPath = SandboxTransformUtils.copyPathToSandbox(
//                            context,
//                            media?.realPath,
//                            media?.mimeType
//                        )
//                        listener?.onCall(media, index)
//                    }
//                }
//            })
            .setCompressEngine { _, list, listener ->
                if (!list.isNullOrEmpty()) {
                    val localMedia = list[0]
                        Luban.with(this).load(localMedia.realPath)
                            .ignoreBy(200)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {
//                                    mCompressDialog.show()
                                }

                                override fun onSuccess(index: Int, compressFile: File?) {
//                                    mCompressDialog.dismiss()
                                    localMedia.compressPath = compressFile?.absolutePath
                                    listener?.onCall(list)
                                }

                                override fun onError(index: Int, e: Throwable?) {
//                                    mCompressDialog.dismiss()
                                }
                            }).launch()
                }
            }
            .setImageEngine(com.mshy.VInterestSpeed.common.utils.GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        val localMedia = result[0]

                        val imageUri = if (localMedia.compressPath.isNullOrEmpty()) {
                            localMedia.realPath
                        } else {
                            localMedia.compressPath
                        }

                        if (imageUri.endsWith(".gif") || imageUri.endsWith(".webp")) {
                            toast("不支持的图片格式")
                            return
                        }

                        mImagePathUri = imageUri

                        refreshCommit()

                        mBinding.raivAnchorVquNewHelloTemplateImg.vquLoadImage(mImagePathUri)
                        mBinding.ivAnchorVquNewHelloTemplateImgAdd.visibility = View.GONE
                        mBinding.ivAnchorVquNewHelloTemplateImgClear.visibility = View.VISIBLE
                    }
                }

                override fun onCancel() {
                }
            })
    }

    private fun initAudioRecord() {
//        mBinding.progressBar.setTotalProgress(maxTime)

        if (audioMessageHelper == null) {
            val options = com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl.getOptions()
            audioMessageHelper = AudioRecorder(this, options.audioRecordType, maxTime + 1, this)
        }
    }

    private fun clickRecord() {
        PermissionUtils.record(
            this,
            requestCallback = { allGranted, _, _ ->
                if (allGranted) {
                    record()
                } else {
                    toast("缺少录音必须的权限")
                }
            }
        )
    }

    private fun record() {
        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        when (state) {
            0 -> { //点击录制
                startRecord()
            }
            1 -> {  //点击停止录制
                if (nowTime < minTime) {
                    toast("录音最短不少于5秒")
                    againAudioRecord()
                    onEndAudioRecord(true)
                } else {
                    stopAudioRecord()
                }
            }
            2 -> { // 点击试听功能
                startPlayRecord()
            }
            3 -> {  // 点击停止播放
                stopPlayRecord()

                countDownTimerHelper?.cancel()
                countDownTimerHelper?.setCountDownTimerListener(null)
            }
        }
    }

    private fun stopPlayRecord() {
        state = 2
        audioPlay.stop()
        mBinding.tvAgain.visibility = View.VISIBLE
        mBinding.progressBar.visibility = View.INVISIBLE
        mBinding.progressBar.setProgress(0)
        onEndAudioRecord(false)
        mBinding.tvTip.setText(R.string.anchor_vqu_new_template_record_listen)
        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_play)
        mBinding.downTimer.visibility = View.GONE
        mBinding.timer.visibility = View.VISIBLE
    }

    private fun startPlayRecord() {

        if (audioFile?.absolutePath.isNullOrEmpty()) {
            toast("无法获取录音文件")
            return
        }

        state = 3
        audioPlay.reset()
        audioPlay.setDataSource(audioFile?.absolutePath)
        // 设置音频流的类型
        audioPlay.setAudioStreamType(AudioManager.STREAM_MUSIC)
        // 通过异步的方式装载媒体资源
        audioPlay.prepare()
        audioPlay.start()

        mBinding.downTimer.visibility = View.VISIBLE
        mBinding.timer.visibility = View.GONE

        countDownTimerHelper = CommonVquCountDownTimerHelper()
        countDownTimerHelper?.setTime(audioPlay.duration.toLong(), 1 * 1000L)
        countDownTimerHelper?.setCountDownTimerListener(object :
            CommonVquCountDownTimerHelper.CountDownTimerListener {
            override fun onTickEvent(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                if (time >= 10) {
                    mBinding.downTimer.text = getString(
                        R.string.anchor_vqu_new_template_record_time,
                        time.toString()
                    )
                } else {
                    mBinding.downTimer.text = getString(
                        R.string.anchor_vqu_new_template_record_time,
                        "0${time}"
                    )
                }
            }

            override fun onFinishEvent() {
                countDownTimerHelper?.setCountDownTimerListener(null)
            }
        })?.build()

        countDownTimerHelper?.start()

        mBinding.tvTip.setText(R.string.anchor_vqu_new_template_record_playing)
        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_playing)
        mBinding.tvAgain.visibility = View.GONE
    }

    private fun stopAudioRecord() {
        state = 2
        mBinding.tvAgain.visibility = View.VISIBLE
        mBinding.progressBar.visibility = View.INVISIBLE
        mBinding.progressBar.setProgress(0)
        onEndAudioRecord(false)
        mBinding.tvTip.setText(R.string.anchor_vqu_new_template_record_listen)
        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_play)
    }

    private fun againAudioRecord() {
        state = 0
        mBinding.tvTip.setText(R.string.anchor_vqu_new_template_record_voice_tips)
        mBinding.tvAgain.visibility = View.GONE
        mBinding.ivRecord.setImageResource(R.mipmap.ic_info_record)
        length = 0
        mVoicePath = ""
    }

    private fun startRecord() {
        state = 1//显示录制进度
        mBinding.tvTip.setText(R.string.anchor_vqu_new_template_record_finish)
        startTimer()
        mBinding.tvAgain.visibility = View.GONE
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_record_stop)
        onStartAudioRecord()
    }

    /**
     * 开始语音录制
     */
    private fun onStartAudioRecord() {
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        audioMessageHelper!!.startRecord()
        cancelled = false
    }

    private fun startTimer() {
        mBinding.timer.base = SystemClock.elapsedRealtime()
        mBinding.timer.setOnChronometerTickListener { chronometer ->
            //当到达最大录制时间 停止计时器  并且停止录音
            nowTime = (SystemClock.elapsedRealtime() - chronometer.base) / 1000

            mBinding.progressBar.setProgress(((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toInt())
            if (SystemClock.elapsedRealtime() - chronometer.base >= maxTime * 1000) {
                //停止计时器
                chronometer.stop()
                stopAudioRecord()
//                onEndAudioRecord(false)
            }

        }
        mBinding.timer.start()
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private fun onEndAudioRecord(cancel: Boolean) {
        started = false
        window?.setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        audioMessageHelper!!.completeRecord(cancel)
        mBinding.timer.stop()
//        refreshCommit()
        mBinding.progressBar.visibility = View.INVISIBLE
    }

    private fun refreshCommit() {
        if (mDesc.isNotEmpty() || mImagePathUri.isNotEmpty() || mVoicePath.isNotEmpty()) {
            mBinding.tbAnchorVquNewHelloTemplateBar.toolbar.findViewById<TextView>(R.id.tv_right)
                .setTextColor(ResUtils.getColor(R.color.color_7C69FE))
        } else {
            mBinding.tbAnchorVquNewHelloTemplateBar.toolbar.findViewById<TextView>(R.id.tv_right)
                .setTextColor(ResUtils.getColor(R.color.color_A3AABE))

        }
    }

    override fun initObserve() {
        mViewModel.saveSuccess.observe(this) {
            setResult(Activity.RESULT_OK)
            finish()
        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> {
                    mLoadDialog.show()
                }
                StateLayoutEnum.HIDE -> {
                    mLoadDialog.dismiss()
                }
                else -> {
                    mLoadDialog.dismiss()
                }
            }
        }
    }

    override fun initRequestData() {

    }

    override fun onRecordSuccess(audioFile: File?, audioLength: Long, recordType: RecordType?) {
        logE("onRecordSuccess 录音成功")
        this.audioFile = audioFile
        mVoicePath = audioFile?.absolutePath ?: ""
        audioType = recordType!!.outputFormat
        state = 2
        mBinding.tvAgain.visibility = View.VISIBLE
        mBinding.progressBar.visibility = View.INVISIBLE
        mBinding.progressBar.setProgress(0)
        mBinding.progressBar.visibility = View.VISIBLE
        onEndAudioRecord(false)
        mBinding.tvTip.setText(R.string.anchor_vqu_new_template_record_listen)
        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_play)
    }

    override fun onRecordReady() {
        logE("onRecordReady 录音准备")
    }

    override fun onRecordStart(audioFile: File?, recordType: RecordType?) {
        logE("onRecordStart 录音开始")
    }


    override fun onRecordFail() {
        logE("onRecordFail 录音失败")
    }

    override fun onRecordCancel() {
        logE("onRecordCancel 录音取消")
    }

    override fun onRecordReachedMaxTime(maxTime: Int) {
        logE("onRecordReachedMaxTime")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                val videoPathParent =
                    data?.getStringExtra(CommonVquVideoCropActivity.KEY_CROP_SAVE_VIDEO_PATH) ?: ""
                val videoFileName =
                    data?.getStringExtra(CommonVquVideoCropActivity.KEY_CROP_SAVE_VIDEO_NAME) ?: ""
                mVideoPath = videoPathParent + videoFileName

                refreshCommit()
                mBinding.raivAnchorVquNewHelloTemplateVideo.vquLoadVideoFirstFrame(mVideoPath)
                mBinding.ivAnchorVquNewHelloTemplateVideoAdd.visibility = View.GONE
                mBinding.ivAnchorVquNewHelloTemplateVideoClear.visibility = View.VISIBLE
            }
        }
    }
}