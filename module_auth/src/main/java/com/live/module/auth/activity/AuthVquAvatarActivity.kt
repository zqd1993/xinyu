package com.live.module.auth.activity

import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.auth.databinding.AuthVquActivityAvatarBinding
import com.live.module.auth.vm.AuthVquAvatarViewModel
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.utils.StateLayoutEnum
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * author: Lau
 * date: 2022/5/19
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Auth.AuthVquAvatarActivity)
class AuthVquAvatarActivity : BaseActivity<AuthVquActivityAvatarBinding, AuthVquAvatarViewModel>() {
    override val mViewModel: AuthVquAvatarViewModel by viewModels()


    private val mLoadDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "提交中")
    }

    private val mCompressDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "压缩中")
    }

    private var mVquAvatar: String? = ""
    private var mVquUploadPath: String? = ""

    override fun initObserve() {
        mViewModel.authResult.observe(this) {
            ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                .withInt(RouteKey.TYPE, if (it) 1 else 3)
                .navigation()
//
            finish()
        }

        mViewModel.verifyData.observe(this) {
            if (it == null) {
                return@observe
            }

            mViewModel.rpVerify(this, it)
        }

        mViewModel.verifyResultData.observe(this) {

            if (it == 1) {
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquAvatarActivity)
                    .navigation()
            } else {
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                    .withInt(RouteKey.TYPE, 3)
                    .navigation()
            }

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

    override fun AuthVquActivityAvatarBinding.initView() {

        mBinding.tbAuthVquAvatarBar.toolbar.initClose("真人认证") { finish() }


        mVquAvatar = UserManager.userInfo?.avatar ?: ""

        if (!mVquAvatar.isNullOrEmpty()) {
            mBinding.raivAuthVquAvatarImg.vquLoadRoundImage(
                NetBaseUrlConstant.IMAGE_URL + mVquAvatar,
                dp2px(10f)
            )

            mBinding.stvAuthVquAvatarChangeAvatar.visibility = View.VISIBLE
        }

        mBinding.sclAuthVquAvatarImgParent.setViewClickListener {

            PermissionUtils.cameraPermission(
                this@AuthVquAvatarActivity,
                requestCallback = { allGranted, _, _ ->
                    if (allGranted) {
                        clickSelectAvatar()
                    } else {
                        toast("上传头像需要使用摄像头拍照和读取相册权限")
                    }
                })
        }

        mBinding.stvAuthVquAvatarChangeAvatar.setViewClickListener {
            PermissionUtils.cameraPermission(
                this@AuthVquAvatarActivity,
                requestCallback = { allGranted, _, _ ->
                    if (allGranted) {
                        clickSelectAvatar()
                    } else {
                        toast("上传头像需要使用摄像头拍照和读取相册权限")
                    }
                })
        }

        mBinding.stvAuthVquAvatarCommit.setViewClickListener {
            PermissionUtils.cameraPermission(
                this@AuthVquAvatarActivity,
                requestCallback = { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        if (!mVquUploadPath.isNullOrEmpty()) {
                            mViewModel.uploadTask(mVquUploadPath, "album")
                        } else {
                            mViewModel.vquDescribeVerifyToken(mVquAvatar ?: "")
                        }
                    } else {
                        toast("真人认证使用活体检测，需要使用摄像头权限")
                    }
                })

//            mViewModel.uploadTask(mVquAvatar, "album")
        }

    }

    private fun clickSelectAvatar() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setSelectionMode(SelectModeConfig.SINGLE)
            .isDisplayCamera(!UserManager.isVideo)
//            .setFilterMaxFileSize(1024 * 2)
//            .setSandboxFileEngine { context, _, index, media, listener ->
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    media?.sandboxPath = SandboxTransformUtils.copyPathToSandbox(
//                        context,
//                        media?.realPath,
//                        media?.mimeType
//                    )
//                    listener.onCall(media, index)
//                }
//            }
            .setCompressEngine { _, list, listener ->
                if (!list.isNullOrEmpty()) {
                    val localMedia = list[0]
                        Luban.with(this@AuthVquAvatarActivity).load(localMedia.realPath)
                            .ignoreBy(200)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {
                                    mCompressDialog.show()
                                }

                                override fun onSuccess(index: Int, compressFile: File?) {
                                    mCompressDialog.dismiss()
                                    localMedia.compressPath = compressFile?.absolutePath
                                    listener?.onCall(list)
                                }

                                override fun onError(index: Int, e: Throwable?) {
                                    mCompressDialog.dismiss()
                                }
                            }).launch()
                }
            }
            .setImageEngine(com.mshy.VInterestSpeed.common.utils.GlideEngine.createGlideEngine())
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    if (!result.isNullOrEmpty()) {
                        val localMedia = result[0]
                        mVquAvatar = if (localMedia.compressPath.isNullOrEmpty()) {
                            localMedia.realPath
                        } else {
                            localMedia.compressPath
                        }
                        mVquUploadPath = mVquAvatar
                        mBinding.raivAuthVquAvatarImg.vquLoadRoundImage(mVquAvatar, dp2px(10f))
                        mBinding.ivAuthVquAvatarAddIcon.visibility = View.GONE
                        mBinding.stvAuthVquAvatarChangeAvatar.visibility = View.VISIBLE
                    }
                }

                override fun onCancel() {

                }
            })
    }
}