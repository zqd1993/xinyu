package com.live.module.agora.activity

import android.hardware.Camera
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.faceunity.core.entity.FUCameraConfig
import com.faceunity.core.entity.FURenderFrameData
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.enumeration.FUAITypeEnum
import com.faceunity.core.faceunity.FUAIKit
import com.faceunity.core.faceunity.FURenderKit
import com.faceunity.core.listener.OnGlRendererListener
import com.faceunity.core.renderer.CameraRenderer
import com.faceunity.core.utils.CameraUtils
import com.faceunity.core.utils.GlUtil
import com.faceunity.nama.DemoConfig
import com.faceunity.nama.data.FaceBeautyDataFactory
import com.faceunity.nama.utils.FuDeviceUtils
import com.gyf.immersionbar.ImmersionBar
import com.live.module.agora.R
import com.live.module.agora.databinding.AgoraTantaActivityBeautySettingBinding
import com.live.module.agora.vm.AgoraVquBeautySettingViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.isVisible
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/7/26
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Agora2.AgoraTantaBeautySettingActivity)
class AgoraTantaBeautySettingActivity :
    BaseActivity<AgoraTantaActivityBeautySettingBinding, AgoraVquBeautySettingViewModel>() {
    override val mViewModel: AgoraVquBeautySettingViewModel by viewModels()

    private var mCameraRenderer: CameraRenderer? = null

    private var mFrontCameraOrientation = 0

    private var mFaceBeautyDataFactory: FaceBeautyDataFactory? = null

    private var mFUAIKit = FUAIKit.getInstance()

    //endregion Activity OnCreate
    private var mFURenderKit = FURenderKit.getInstance()

    private var mIsEnableFaceBeauty = true

    override fun setStatusBar() {
        ImmersionBar.with(this@AgoraTantaBeautySettingActivity)
            .reset()
            .transparentStatusBar()
            .fitsSystemWindows(false)
            .statusBarDarkFont(false)
            .init()
    }

    override fun AgoraTantaActivityBeautySettingBinding.initView() {

        initUi()

        initEvent()
    }

    override fun onResume() {
        super.onResume()
        mCameraRenderer?.onResume()
    }

    private fun initUi() {

        mBinding.fuBaseGlSurface.setEGLContextClientVersion(GlUtil.getSupportGlVersion(this));
        mCameraRenderer =
            CameraRenderer(mBinding.fuBaseGlSurface, getCameraConfig(), mOnGlRendererListener)

        mFrontCameraOrientation =
            CameraUtils.getCameraOrientation(Camera.CameraInfo.CAMERA_FACING_FRONT)

        mFaceBeautyDataFactory = FaceBeautyDataFactory(mFaceBeautyListener)

        mIsEnableFaceBeauty = SpUtils.getBoolean(SpKey.FACE_BEAUTY_ENABLE, true) ?: true

        mFaceBeautyDataFactory?.enableFaceBeauty(mIsEnableFaceBeauty)

        mBinding.ivBeautyOff.isSelected = !mIsEnableFaceBeauty

        mBinding.fbcvAgoraVquBeautySettingControl.bindDataFactory(mFaceBeautyDataFactory)
        mBinding.fbcvAgoraVquBeautySettingControl.setCurrentId(R.id.beauty_radio_skin_beauty)

    }

    /**
     * 配置相机参数
     *
     * @return CameraBuilder
     */
    protected fun getCameraConfig(): FUCameraConfig {
        return FUCameraConfig()
    }

    private fun initEvent() {

        mBinding.ivBeautyOff.setViewClickListener {
            clickBeautyOff()
        }
//
        mBinding.ivBeautyShow.setViewClickListener {
            clickBeautyShow()
        }
//
        mBinding.ivCancel.setViewClickListener {
            onBackPressed()
        }
//
        mBinding.tvRecover.setViewClickListener {
            clickRecover()
        }
//
        mBinding.ivBeautySave.setViewClickListener {
            clickSave()
        }
//
        mBinding.ivBeautySwitch.setViewClickListener {
            clickSwitch()
        }

        mBinding.fuBaseGlSurface.setViewClickListener {
            clickSurface()
        }
    }

    private fun clickSurface() {
        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        }
    }

    private fun clickSwitch() {
        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        } else {
            mCameraRenderer?.switchCamera()
        }
    }

    private fun clickRecover() {
        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        } else {
            mIsEnableFaceBeauty = true
//            mFaceBeautyDataFactory?.reset()
            mFaceBeautyDataFactory?.enableFaceBeauty(mIsEnableFaceBeauty)
            mBinding.fbcvAgoraVquBeautySettingControl.recoverAllData()
        }
    }

    private fun clickBeautyShow() {
        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        } else {
            if (!mIsEnableFaceBeauty) {
                toast("请先打开美颜")
            } else {
                mBinding.fbcvAgoraVquBeautySettingControl.visibility = View.VISIBLE
            }
        }
    }

    private fun clickBeautyOff() {
        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        } else {
            mBinding.ivBeautyOff.isSelected = !mBinding.ivBeautyOff.isSelected
            mIsEnableFaceBeauty = !mBinding.ivBeautyOff.isSelected
            mFaceBeautyDataFactory?.enableFaceBeauty(mIsEnableFaceBeauty)
        }
    }

    private fun clickSave() {
        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        } else {
            SpUtils.putBoolean(SpKey.FACE_BEAUTY_ENABLE, mIsEnableFaceBeauty)
            mFaceBeautyDataFactory?.save()
            finish()
        }
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    /**
     * 特效配置
     */
    private fun configureFURenderKit() {
        mFUAIKit.loadAIProcessor(DemoConfig.BUNDLE_AI_FACE, FUAITypeEnum.FUAITYPE_FACEPROCESSOR)
        mFUAIKit.faceProcessorSetFaceLandmarkQuality(DemoConfig.DEVICE_LEVEL)
        //高端机开启小脸检测
        if (DemoConfig.DEVICE_LEVEL > FuDeviceUtils.DEVICE_LEVEL_MID) mFUAIKit.fuFaceProcessorSetDetectSmallFace(
            true
        )

        mFaceBeautyDataFactory?.bindCurrentRenderer()
    }

    private val mOnGlRendererListener = object : OnGlRendererListener {
        override fun onDrawFrameAfter() {

        }

        override fun onRenderAfter(outputData: FURenderOutputData, frameData: FURenderFrameData) {
        }

        override fun onRenderBefore(inputData: FURenderInputData?) {
        }

        override fun onSurfaceChanged(width: Int, height: Int) {
        }

        override fun onSurfaceCreated() {
            configureFURenderKit()
        }

        override fun onSurfaceDestroy() {
            mFURenderKit.release()
        }
    }

    private var mFaceBeautyListener: FaceBeautyDataFactory.FaceBeautyListener =
        object : FaceBeautyDataFactory.FaceBeautyListener {
            override fun onFilterSelected(res: Int) {
                toast(res)
            }

            override fun onFaceBeautyEnable(enable: Boolean) {
                mCameraRenderer?.setFURenderSwitch(enable)
            }

        }

    override fun onDestroy() {
        mCameraRenderer?.onDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mCameraRenderer?.onPause()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mBinding.fbcvAgoraVquBeautySettingControl.gone()
        return super.onTouchEvent(event)
    }

    override fun onBackPressed() {

        if (mBinding.fbcvAgoraVquBeautySettingControl.isVisible) {
            mBinding.fbcvAgoraVquBeautySettingControl.gone()
        } else {
            showLeaveMessage()
        }
//        super.onBackPressed()
    }

    private fun showLeaveMessage() {
        val messageDialog = MessageDialog()
        messageDialog.setTitle("是否保存")
        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                messageDialog.dismiss()
                finish()
                return true
            }

            override fun onRightClick(): Boolean {
                messageDialog.dismiss()
                clickSave()
                return true
            }
        })

        messageDialog.show(supportFragmentManager, "")
    }

}