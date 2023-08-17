package com.live.module.info.activity

import android.Manifest
import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.Chronometer
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.live.module.info.R
import com.live.module.info.databinding.InfoTantaActivityVoiceEditBinding
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog
import com.mshy.VInterestSpeed.common.ui.vm.UploadViewModel
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl
import com.netease.nimlib.sdk.media.player.AudioPlayer
import com.netease.nimlib.sdk.media.player.OnPlayListener
import com.netease.nimlib.sdk.media.record.AudioRecorder
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback
import com.netease.nimlib.sdk.media.record.RecordType
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class InfoVquVoiceEditActivity() : BaseActivity<InfoTantaActivityVoiceEditBinding, UploadViewModel>(),
    IAudioRecordCallback, Parcelable {
    /**
     * 0是初始状态 只显示录音按钮  点重录也是这个状态
     * 1 录制中 显示进度条 和录制按钮
     * 2 录制完成 显示重录  点击试听  确定按钮
     */
    var state = 0
    var audioFile: File? = null
    var audioType: Int = 1
    protected var audioMessageHelper: AudioRecorder? = null
    var audioPlay: AudioPlayer? = null
    var maxTime: Int = 15//最长时长暂定60秒
    var drscList: ArrayList<String>? = ArrayList()
    var time: Int = 0

    var loadingDialog:LoadingDialog? = null
    //录制语音相关

    /*----------------------------------音频录制相关-----------------------------------*/ //录制语音相关
    private val mPermissions =
        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    constructor(parcel: Parcel) : this() {
        state = parcel.readInt()
    }

    fun record() {
        PermissionUtils.record(this, requestCallback = { allGranted, grantedList, deniedList ->
            if (allGranted) {
                state = 1//显示录制进度
                mBinding.tvTip.text = "点击结束录音"
                startTimer()
                mBinding.tvAgain.visibility = View.GONE
                mBinding.tvCommit.visibility = View.GONE
                mBinding.progressBar.visible()
                mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_record_stop)
                onStartAudioRecord()
            } else {
                "未授权,将无法录制".toast()
            }
        })

    }


    /**
     * 声音展示实例文案
     */
    private fun getVoiceList() {
        mViewModel.vquGetVoiceTextList()
    }

    var index = 0

    fun updateDesc() {
        if (drscList!!.size > 0 && drscList!!.size - 1 > index) { // 判断下标
            var desc = drscList!![index]
            if (desc != null) {
                desc = desc.replace("\t|\n".toRegex(), "")
            }
            mBinding.tvContent.setText(desc)
            index++
        } else {
            index = 0
            getVoiceList()
        }
    }

    /**
     * 开启计时器
     */
    private fun startTimer() {
        mBinding.timer?.setBase(SystemClock.elapsedRealtime())
        mBinding.timer.onChronometerTickListener =
            Chronometer.OnChronometerTickListener { chronometer ->
                //当到达最大录制时间 停止计时器  并且停止录音
                mBinding.progressBar.setProgress(((SystemClock.elapsedRealtime() - chronometer.base) / 1000).toInt())
                if (SystemClock.elapsedRealtime() - chronometer.base >= maxTime * 1000) {
                    //停止计时器
                    chronometer.stop()
                    onEndAudioRecord(false)
                }

            }
        mBinding.timer?.start()
    }

    private var cancelled = false
    private var started = false

    /**
     * 初始化AudioRecord
     */
    private fun initAudioRecord() {
        if (audioMessageHelper == null) {
            val options = NimUIKitImpl.getOptions()
            audioMessageHelper = AudioRecorder(this, options.audioRecordType, 16, this)
        }
    }

    /**
     * 开始语音录制
     */
    private fun onStartAudioRecord() {
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        audioMessageHelper!!.startRecord()
        cancelled = false
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private fun onEndAudioRecord(cancel: Boolean) {
        started = false
        this.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        audioMessageHelper!!.completeRecord(cancel)
        mBinding.timer.stop()
        mBinding.progressBar.visibility = View.INVISIBLE
    }

    override fun onPause() {
        super.onPause()
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true)
        }
        if (audioPlay != null) {
            audioPlay?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // release
        if (audioMessageHelper != null) {
            audioMessageHelper!!.destroyAudioRecorder()
        }
    }


    override fun onRecordSuccess(file: File?, p1: Long, p2: RecordType?) {
        time = Math.ceil((p1 / 1000).toDouble()).toInt()
        if (time < 5) {
            "至少录制五秒".toast()
            state = 0
            mBinding.tvTip.text = "点击录音，至少5s"
            mBinding.tvAgain.visibility = View.GONE
            mBinding.tvCommit.visibility = View.GONE
            mBinding.ivRecord.setImageResource(R.mipmap.ic_info_record)
            mBinding.timer.setText("00:00")
        } else {
            state = 2
            audioFile = file
            audioType = p2!!.outputFormat
            mBinding.tvAgain.visibility = View.VISIBLE
            mBinding.tvCommit.visibility = View.VISIBLE
            mBinding.progressBar.visibility = View.INVISIBLE
            mBinding.progressBar.setProgress(0)
            mBinding.progressBar.visibility = View.VISIBLE
            onEndAudioRecord(false)
            mBinding.tvTip.text = "点击试听"
            mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_play)
        }

    }

    override fun onRecordReachedMaxTime(p0: Int) {
    }

    override fun onRecordReady() {
    }

    override fun onRecordCancel() {
    }

    override fun onRecordStart(p0: File?, p1: RecordType?) {
    }

    override fun onRecordFail() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(state)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InfoVquVoiceEditActivity> {
        override fun createFromParcel(parcel: Parcel): InfoVquVoiceEditActivity {
            return InfoVquVoiceEditActivity(parcel)
        }

        override fun newArray(size: Int): Array<InfoVquVoiceEditActivity?> {
            return arrayOfNulls(size)
        }
    }


    override val mViewModel: UploadViewModel by viewModels()

    override fun InfoTantaActivityVoiceEditBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.info_voice)) {
            finish()
        }
        initAudioRecord()
        mBinding.ivRecord.setViewClickListener {
            if (UserManager.isVideo) {
                toast("正在语音/视频通话中，请稍后再试...")
                return@setViewClickListener
            }
            when (state) {
                0 -> { //点击录制
                    record()
                }
                1 -> {
                    state = 2
//                    tv_again.visibility = View.VISIBLE
//                    tv_commit.visibility = View.VISIBLE
//                    progressBar.visibility = View.INVISIBLE
//                    progressBar.setProgress(0)
                    onEndAudioRecord(false)
//                    tv_tip.text = "点击试听"
//                    iv_record.setImageResource(R.mipmap.ic_voice_play)
                }
                2 -> {//点击试听功能
                    if (audioPlay?.isPlaying!!) {
                        audioPlay?.stop()
                        mBinding.tvTip.text = "点击试听"
                        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_play)
                    } else {
                        audioPlay?.setDataSource(audioFile?.absolutePath)
                        audioPlay?.start(3)
                        audioPlay?.onPlayListener = object : OnPlayListener {
                            override fun onPrepared() {
                            }

                            override fun onCompletion() {
                                mBinding.tvTip.text = "点击试听"
                                mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_play)
                            }

                            override fun onInterrupt() {
                            }

                            override fun onError(p0: String?) {
                            }

                            override fun onPlaying(p0: Long) {
                            }

                        }
                        mBinding.tvTip.text = "播放中"
                        mBinding.ivRecord.setImageResource(R.mipmap.ic_tanta_info_voice_playing)
                    }
                }

            }
        }
        mBinding.tvAgain.setViewClickListener {
            state = 0
            mBinding.tvTip.text = "点击录音，至少5s"
            mBinding.tvAgain.visibility = View.GONE
            mBinding.tvCommit.visibility = View.GONE
            mBinding.ivRecord.setImageResource(R.mipmap.ic_info_record)
            audioPlay?.stop()
        }
        mBinding.tvCommit.setViewClickListener {//提交录制的声音
            if (audioFile != null && audioFile!!.exists()) {
                loadingDialog =
                    LoadingDialog(this@InfoVquVoiceEditActivity,
                        "上传中")
                mViewModel.vquUploadVoice(audioFile!!, "voice")
            }
        }
        audioPlay = AudioPlayer(this@InfoVquVoiceEditActivity)
        getVoiceList()
        mBinding.tvChange.setViewClickListener {
            updateDesc()
        }
    }

    override fun initObserve() {
        mViewModel.vquVoiceUrlData.observe(this, Observer {
            loadingDialog?.dismiss()
            "上传成功".toast()
            intent.putExtra("voice", it)
            intent.putExtra("time", time)
            setResult(Activity.RESULT_OK, intent)
            finish()

        })
        mViewModel.vquUpLoadErrData.observe(this, Observer {
            "上传失败".toast()
            loadingDialog?.dismiss()
        })
        mViewModel.vquVoiceTextListData.observe(this, Observer {
            if (drscList != null) {
                drscList?.clear()
                drscList?.addAll(it.data.list)
                updateDesc()
            }
        })
    }

    override fun initRequestData() {
    }
}