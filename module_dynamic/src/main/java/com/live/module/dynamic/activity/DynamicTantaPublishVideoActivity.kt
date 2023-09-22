package com.live.module.dynamic.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aliyun.svideosdk.editor.AliyunIVodCompose
import com.aliyun.svideosdk.editor.impl.AliyunComposeFactory
import com.aliyun.svideosdk.editor.impl.AliyunVodCompose
import com.live.module.dynamic.R
import com.live.module.dynamic.bean.DynamicPostBean
import com.live.module.dynamic.databinding.DynamicTantaActivityPublishVideoBinding
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.activity.CommonVquVideoCropActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.*
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.mshy.VInterestSpeed.common.ui.dialog.DownProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.io.File
import java.io.Serializable


/**
 * author: Tany
 * date: 2022/3/30
 * description: 发布动态页面
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquPublishVideoActivity)
class DynamicTantaPublishVideoActivity :
    BaseActivity<DynamicTantaActivityPublishVideoBinding, DynamicPublishViewModel>() {
    var videoPath: String = ""
    var content: String? = ""
    private val EDIT_VIDEO_CODE = 10001
    var maxDuration: Long = 60
    private var mProgressDialog: DownProgressDialog? = null
    private var compose: AliyunVodCompose? = null
    var rightBtn: Button? = null
    var isFirst: Boolean? = true
    override fun DynamicTantaActivityPublishVideoBinding.initView() {
        mBinding.includeTitle.toolbar.run {
            initClose("发布视频") {
                finish()
            }
        }
        rightBtn = mBinding.includeTitle.toolbar.findViewById(R.id.btn_right)
        rightBtn?.text = "发布"
        rightBtn?.isEnabled = false
        rightBtn?.visible()
        isFirst = SpUtils.getBoolean("isFirstPublish", true)
        compose = AliyunComposeFactory.createAliyunVodCompose();
        compose?.init(this@DynamicTantaPublishVideoActivity);
//        vquSelectVideo()
        mBinding.ivAdd.setViewClickListener {//点添加按钮进行选择
            vquSelectVideo()
        }
        mBinding.ivDel.setOnClickListener {
            vquDelVideo()
        }
        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                content = s.toString().trim()
                mBinding.tvHint.text = content?.length.toString() + "/100"
            }
        })
        mBinding.publishTipStv.setViewClickListener { showTipDialog() }
        mBinding.tvPublish.setViewClickListener { vquPublishVideo() }
        rightBtn?.setViewClickListener { vquPublishVideo() }
        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                content = s.toString().trim()
                changeState()
            }
        })
        var imgList: Serializable? = intent.getSerializableExtra("list") ?: return
        var selectPicList = imgList as ArrayList<LocalMedia?>?
        if (isFirst!!) {
            showTipDialog()
        }
        var video = selectPicList!![0]!!
        videoPath = video.realPath
        val videoPathParent = videoPath?.substring(
            0,
            videoPath.lastIndexOf("/")
        ) + File.separator
        val videoFileName =
            videoPath?.substring(videoPath.lastIndexOf("/") + 1)
        if (video.duration > maxDuration * 1000) {
            CommonVquVideoCropActivity.startActivityForResult(
                this@DynamicTantaPublishVideoActivity,
                videoPathParent,
                videoFileName,
                maxDuration * 1000,
                EDIT_VIDEO_CODE
            )
        } else {
            vquVideo()
        }
    }

    fun vquSelectVideo() {
        PictureSelector.create(this@DynamicTantaPublishVideoActivity)//进页面就进行选择
            .openGallery(SelectMimeType.ofVideo())
            .isDisplayCamera(!UserManager.isVideo)
            .setMaxSelectNum(1)
            .setSandboxFileEngine(object : SandboxFileEngine {
                override fun onStartSandboxFileTransform(
                    context: Context?,
                    isOriginalImage: Boolean,
                    index: Int,
                    media: LocalMedia?,
                    listener: OnCallbackIndexListener<LocalMedia>?,
                ) {
                    SandboxTransformUtils.copyPathToSandbox(context,
                        media?.realPath,
                        media?.mimeType)
                }
            })
            .setImageEngine(GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    if (!result.isNullOrEmpty()) {
                        var video = result!![0]!!
                        videoPath = video.realPath
                        val videoPathParent = videoPath?.substring(
                            0,
                            videoPath.lastIndexOf("/")
                        ) + File.separator
                        val videoFileName =
                            videoPath?.substring(videoPath.lastIndexOf("/") + 1)
                        if (video.duration > maxDuration * 1000) {
                            CommonVquVideoCropActivity.startActivityForResult(
                                this@DynamicTantaPublishVideoActivity,
                                videoPathParent,
                                videoFileName,
                                maxDuration * 1000,
                                EDIT_VIDEO_CODE
                            )
                        } else {
                            vquVideo()
                        }
                    }
                    changeState()

                }

                override fun onCancel() {
                    finish()
                }
            })
    }

    fun showTipDialog() {
        val messageDialog = MessageDialog()
        messageDialog.setTitle(R.string.dynamic_publish_tip_title)
        messageDialog.setContent(R.string.dynamic_publish_tip)
        messageDialog.setSingleText(R.string.dynamic_publish_tip_know)
        messageDialog.setIsSingleButton(true)
        messageDialog.setOnButtonClickListener(object :
            MessageDialog.OnSingleButtonClickListener {
            override fun onClick(): Boolean {
                messageDialog.dismiss()
                SpUtils.putBoolean("isFirstPublish", false)
                return true
            }

        })
        messageDialog.show(supportFragmentManager, "")
    }

    private fun vquPublishVideo() {
        if (content.isNullOrEmpty()) {
            "动态内容不能为空".toast()
            return
        }
        if (videoPath.isNullOrEmpty()) {
            "视频不能为空".toast()
            return
        }
        UmUtils.setUmEvent(this@DynamicTantaPublishVideoActivity, UmUtils.CLICKTOPUBLISH)
        var name = videoPath.substring(videoPath.lastIndexOf("/") + 1);
        mViewModel.vquPublishVideo(content!!, 1, name)
    }

    private fun vquDelVideo() {
        videoPath = ""
        mBinding.ivVideo.gone()
        mBinding.ivPlay.gone()
        mBinding.ivDel.gone()
        mBinding.ivAdd.visible()
        changeState()
    }

    fun changeState() {
        if (!content.isNullOrEmpty() && !videoPath.isNullOrEmpty()) {
            rightBtn?.isClickable = true
            rightBtn?.isEnabled = true
        } else {
            rightBtn?.isEnabled = false
        }
    }


    override fun initObserve() {
        mViewModel.vquUploadVideoData.observe(this) {
            vquStartUpLoad(it.data)
        }
    }

    private fun vquStartUpLoad(data: DynamicPostBean) {
        if (data == null) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog =
                DownProgressDialog(this)
        } else {
            mProgressDialog?.setProgress(0)
        }
        mProgressDialog?.show()
        val rv: Int = compose!!.uploadVideoWithVod(videoPath,
            data.uploadAddress,
            data.uploadAuth,
            object : AliyunIVodCompose.AliyunIVodUploadCallBack {
                override fun onUploadFailed(code: String, message: String) {
                    "上传失败".toast()
                }

                override fun onUploadProgress(uploadedSize: Long, totalSize: Long) {
                    runOnUiThread {
                        if (mProgressDialog != null) {
                            mProgressDialog!!.setProgress((uploadedSize.toInt() / totalSize * 100).toInt())
                        }
                    }
                }

                override fun onUploadRetry(code: String, message: String) {
                }

                override fun onUploadRetryResume() {
                }

                override fun onUploadSucceed() {
                    runOnUiThread {
                        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
                            mProgressDialog!!.setProgress(100)
                            mProgressDialog!!.dismiss()
                        }
                    }
                    "发布动态成功".toast()
                    EventBus.getDefault().post("refreshDynamic")
                    finish()
                }

                override fun onUploadTokenExpired() {
                }
            })
        if (rv < 0) {
            "参数错误".toast()
        }

    }

    override fun initRequestData() {
    }

    override val mViewModel: DynamicPublishViewModel by viewModels()
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
//                PictureConfig.CHOOSE_REQUEST -> {
//                    var selectPicList = PictureSelector.obtainSelectorList(data)
//                    videoPath = selectPicList[0].realPath
//                    vquVideo()
//                }
                EDIT_VIDEO_CODE -> {
                    val videoPathParent =
                        data?.getStringExtra(CommonVquVideoCropActivity.KEY_CROP_SAVE_VIDEO_PATH)
                    val videoFileName =
                        data?.getStringExtra(CommonVquVideoCropActivity.KEY_CROP_SAVE_VIDEO_NAME)
                    videoPath = videoPathParent + videoFileName
                    vquVideo()
                }
            }
        }
    }

    fun vquVideo() {
        mBinding.ivVideo.vquLoadRoundImage(
            Uri.fromFile(File(videoPath)),
            10
        )
        mBinding.ivVideo.visible()
        mBinding.ivPlay.visible()
        mBinding.ivDel.visible()
        mBinding.ivAdd.gone()
        mBinding.ivVideo.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Common.CommonVquVideoActivity)
                .withString("video_url", videoPath)
                .withBoolean("del", false)
                .navigation()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.dynamic_vqu_publish_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.iv_tip) {
//            val messageDialog = MessageDialog()
//            messageDialog.setTitle(R.string.dynamic_publish_tip_title)
//            messageDialog.setContent(R.string.dynamic_publish_tip)
//            messageDialog.setSingleText(R.string.dynamic_publish_tip_know)
//            messageDialog.setIsSingleButton(true)
//            messageDialog.setOnButtonClickListener(object :
//                MessageDialog.OnSingleButtonClickListener {
//                override fun onClick(): Boolean {
//                    messageDialog.dismiss()
//                    return true
//                }
//
//            })
//            messageDialog.show(supportFragmentManager, "")
//        }
//        return super.onOptionsItemSelected(item)
//    }
}