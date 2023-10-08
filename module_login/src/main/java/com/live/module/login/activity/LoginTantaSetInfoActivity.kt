package com.live.module.login.activity

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.IGetter
import com.live.module.login.R
import com.live.module.login.bean.LoginTantaNicknameBean
import com.live.module.login.bean.Nickname
import com.live.module.login.databinding.LoginTantaActivitySetInfoBinding
import com.live.module.login.vm.LoginTantaSetInfoViewModel
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.*
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.DateUtils
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.*
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File


/**
 * author: Lau
 * date: 2022/4/8
 * description:注册填写资料
 */
@AndroidEntryPoint
@EventBusRegister
@Route(path = RouteUrl.Login.LoginTantaSetInfoActivity)
class LoginTantaSetInfoActivity :
    BaseActivity<LoginTantaActivitySetInfoBinding, LoginTantaSetInfoViewModel>(){
    override val mViewModel: LoginTantaSetInfoViewModel by viewModels()

    private var mLoginTantaNickname: Nickname? = null

    private var mLoginTantaAvatar: String? = null

    private var mLoginTantaNicknameBean: LoginTantaNicknameBean? = null


    private lateinit var mAgePicker: OptionsPickerView<Int>

    private var mTantaLoginGender: Int = 0

    private var mTantaInviteCode: String? = null

    private var mTantaAge = "35"


    private var mUploadImagePath: String? = null

    private var mBoyName = ""
    private var mGirlName = ""

    @Autowired(name = RouteKey.SET_INFO_IS_ONE_KEY_PASS)
    @JvmField
    var mSetInfoIsOneKeyPass = false

    private val mLoadDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "提交中")
    }

    private val mCompressDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "压缩中")
    }


    override fun LoginTantaActivitySetInfoBinding.initView() {
        com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
            this@LoginTantaSetInfoActivity,
            com.mshy.VInterestSpeed.common.utils.UmUtils.ENTERREGISTRATION
        )
        mBinding.tbTantaSetInfoBar.toolbar.initClose(backImg = R.mipmap.ic_back) {
//            finish()
            clickClose()
        }
        tantaGetDeviceId()
        mBinding.sivLoginTantaSetInfoAvatar.vquLoadCircleImage(
            R.mipmap.ic_login_tanta_avatar_bg
        )

        mTantaInviteCode = getClipboardMessage()

        if (!mTantaInviteCode.isNullOrEmpty()) {
            mBinding.tvLoginTantaSetInfoInviteCode.setText(mTantaInviteCode)
            mBinding.tvLoginTantaSetInfoInviteCode.isEnabled = false
        }

        initEvent()

        initAgeView()

        mBinding.tvLoginTantaSetInfoBirthday.text = mTantaAge

        val loginEvent = LoginEvent()
        loginEvent.type = 2
        EventBus.getDefault().post(loginEvent)

    }

    private fun clickClose() {
        val message = MessageDialog();
        message.setContent("是否放弃注册？")
        message.setRightText("放弃")
        message.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {
                message.dismiss()
                finish()
                return false
            }

        })
        message.show(supportFragmentManager, "")
    }

    private fun initEvent() {
        mBinding.rgLoginTantaSetInfoGenderParent.setOnCheckedChangeListener { _, checkedId ->
            mTantaLoginGender = when (checkedId) {
                R.id.cirb_login_tanta_set_info_gender_girl -> 1
                R.id.cirb_login_tanta_set_info_gender_boy -> 2
                else -> 0
            }
            randomNicknameAndAvatar()
        }

        mBinding.clLoginTantaSetInfoBirthdayParent.clickDelay {
            mAgePicker.show()
        }

        mBinding.stvLoginTantaSetInfoNext.clickDelay {
            clickNext()
        }

        mBinding.sivLoginTantaSetInfoAvatar.setViewClickListener {
            clickAvatar()
        }

        mBinding.tvLoginTantaSetInfoNickname.addTextChangedListener {
            refreshCommit()
        }

        mBinding.ivLoginTantaSetInfoDiceNickname.setViewClickListener(0) {
            randomNicknameAndAvatar(needNickname = true, needAvatar = false, isClickDice = true)
        }
    }

    private fun clickAvatar() {
        PermissionUtils.cameraPermission(
            this,
            requestCallback = { allGranted, _, _ ->
                if (allGranted) {
                    changeHead()
                } else {
                    toast("上传头像需要使用摄像头拍照和读取相册权限")
                }
            })
    }

    private fun clickSelectAvatar() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .isDisplayCamera(!UserManager.isVideo)
            .setSelectionMode(SelectModeConfig.SINGLE)
            .isGif(false)
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
                    Luban.with(this@LoginTantaSetInfoActivity).load(localMedia.realPath)
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
                        val avatarPath = if (localMedia.compressPath.isNullOrEmpty()) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                localMedia.sandboxPath
//                            } else {
                            localMedia.realPath
//                            }
                        } else {
                            localMedia.compressPath
                        }



                        if (avatarPath.endsWith(".gif") || avatarPath.endsWith("webp")) {
                            toast("不支持的图片格式")
                            return
                        }


                        mLoginTantaAvatar = avatarPath
                        mUploadImagePath = mLoginTantaAvatar
                        com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("avatarPath0000=$avatarPath")
                        mBinding.sivLoginTantaSetInfoAvatar.vquLoadCircleImage(
                            mLoginTantaAvatar
                        )

                        refreshCommit()

//                        mBinding.sivLoginTantaSetInfoAvatar.vquLoadImage(mVquAvatar)
                    }
                }

                override fun onCancel() {
                }
            })
    }

    private fun changeHead() {
        PictureSelector.create(this@LoginTantaSetInfoActivity)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(com.mshy.VInterestSpeed.common.utils.GlideEngine.createGlideEngine())
            .setSandboxFileEngine(object : SandboxFileEngine {
                override fun onStartSandboxFileTransform(
                    context: Context?,
                    isOriginalImage: Boolean,
                    index: Int,
                    media: LocalMedia?,
                    listener: OnCallbackIndexListener<LocalMedia>?,
                ) {
                    SandboxTransformUtils.copyPathToSandbox(
                        context,
                        media?.realPath,
                        media?.mimeType
                    )
                }
            })
            .setMaxSelectNum(1)
            .setCropEngine { fragment, currentLocalMedia, dataSource, requestCode ->
                val currentCropPath = currentLocalMedia.availablePath
                val inputUri: Uri
                inputUri =
                    if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(
                            currentCropPath
                        )
                    ) {
                        Uri.parse(currentCropPath)
                    } else {
                        Uri.fromFile(File(currentCropPath))
                    }
                val fileName = DateUtils.getCreateFileName("CROP_") + ".jpg"
                val destinationUri = Uri.fromFile(File(getSandboxPath(), fileName))
                val dataCropSource = java.util.ArrayList<String>()
                for (i in dataSource.indices) {
                    val media = dataSource[i]
                    dataCropSource.add(media.availablePath)
                }
                val options = buildOptions()
                val uCrop = UCrop.of(inputUri, destinationUri, dataCropSource)
                //options.setMultipleCropAspectRatio(buildAspectRatios(dataSource.size()));
                uCrop.withOptions(options!!)
                uCrop.setImageEngine(object : UCropImageEngine {
                    override fun loadImage(context: Context, url: String, imageView: ImageView) {
                        Glide.with(context).load(url).override(180, 180).into(imageView)
                    }

                    override fun loadImage(
                        context: Context,
                        url: Uri,
                        maxWidth: Int,
                        maxHeight: Int,
                        call: UCropImageEngine.OnCallbackListener<Bitmap>,
                    ) {
                        Glide.with(context).asBitmap().override(maxWidth, maxHeight).load(url)
                            .into(object : CustomTarget<Bitmap?>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap?>?,
                                ) {
                                    if (call != null) {
                                        call.onCall(resource)
                                    }
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    if (call != null) {
                                        call.onCall(null)
                                    }
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}

                            })
                    }
                })
                uCrop.start(this@LoginTantaSetInfoActivity, fragment, requestCode)

            }
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: java.util.ArrayList<LocalMedia?>?) {
                    if (result.isNullOrEmpty()) {
                        return
                    }
                    var avatarPath = result!![0]?.cutPath
//                    if (avatarPath.endsWith(".gif") || avatarPath.endsWith("webp")) {
//                        toast("不支持的图片格式")
//                        return
//                    }
                    com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("avatarPath=$avatarPath")
                    mLoginTantaAvatar = avatarPath
                    mUploadImagePath = mLoginTantaAvatar
                    com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("avatarPath0000=$avatarPath")
                    mBinding.sivLoginTantaSetInfoAvatar.vquLoadCircleImage(
                        mLoginTantaAvatar
                    )

                    refreshCommit()

                }

                override fun onCancel() {
                }
            })


    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private fun getSandboxPath(): String? {
        val externalFilesDir: File = this@LoginTantaSetInfoActivity.getExternalFilesDir("")!!
        val customFile = File(externalFilesDir.absolutePath, "Sandbox")
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    private fun buildOptions(): UCrop.Options? {
        val options = UCrop.Options()
        options.setHideBottomControls(true)
        options.setFreeStyleCropEnabled(false)
        options.setShowCropFrame(false)
        options.setShowCropGrid(false)
        options.setCircleDimmedLayer(false)
        options.withAspectRatio(1f, 1f)
        options.setCropOutputPathDir(getSandboxPath()!!)
        options.isCropDragSmoothToCenter(false)
        options.isUseCustomLoaderBitmap(false)
        options.isForbidSkipMultipleCrop(false)
        options.setMaxScaleMultiplier(100f)
        options.setStatusBarColor(
            ContextCompat.getColor(
                this@LoginTantaSetInfoActivity,
                R.color.ps_color_grey
            )
        )
        options.setToolbarColor(
            ContextCompat.getColor(
                this@LoginTantaSetInfoActivity,
                R.color.ps_color_grey
            )
        )
        options.setToolbarWidgetColor(
            ContextCompat.getColor(
                this@LoginTantaSetInfoActivity,
                R.color.ps_color_white
            )
        )
        return options
    }

    private fun tantaGetDeviceId() {

        DeviceID.getOAID(this, object : IGetter {
            override fun onOAIDGetComplete(result: String) {
                // 不同厂商的OAID/AAID格式是不一样的，可进行MD5、SHA1之类的哈希运算统一
                DeviceManager.getInstance().oaid = result
            }

            override fun onOAIDGetError(error: Exception) {
                // 获取OAID/AAID失败
            }
        })
//        val demoHelper = com.mshy.VInterestSpeed.common.helper.OaidHelper(this)
//        demoHelper.getDeviceIds(this)

    }

    private fun clickNext() {
        val inviteCode = mBinding.tvLoginTantaSetInfoInviteCode.text.toString()

        if (mTantaInviteCode.isNullOrEmpty()) {
            mTantaInviteCode = inviteCode
        }

        val nickname = mBinding.tvLoginTantaSetInfoNickname.text.toString()
        var key = ""
        if (mTantaLoginGender == 2 && nickname == mLoginTantaNickname?.name) {
            key = mLoginTantaNickname?.key ?: ""
        }
        com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
            this@LoginTantaSetInfoActivity,
            com.mshy.VInterestSpeed.common.utils.UmUtils.COMPLETEREGISTRATION
        )
        mViewModel.tantaProfileReg(
            nickname,
            mLoginTantaAvatar,
            mUploadImagePath,
            mTantaLoginGender,
            key,
            mTantaAge,
            mTantaInviteCode,
            supportFragmentManager
        )
        RiskControlUtil.getToken(this@LoginTantaSetInfoActivity,3)
    }

    private val mAgePickerList = mutableListOf<Int>()

    private fun initAgeView() {

        for (i in 18..60) {
            mAgePickerList.add(i)
        }

        mAgePicker = OptionsPickerBuilder(
            this
        ) { options1, _, _, _ ->

            if (options1 < 0 || options1 >= mAgePickerList.size) {
                return@OptionsPickerBuilder
            }

            mTantaAge = mAgePickerList[options1].toString()
            mBinding.tvLoginTantaSetInfoBirthday.text = mTantaAge

        }.setBgColor(android.R.color.transparent)
            .setTitleBgColor(android.R.color.transparent)
            .setDividerColor(ResUtils.getColor(android.R.color.transparent))
            .setCancelColor(ResUtils.getColor(R.color.color_A3AABE))
            .setSubmitColor(ResUtils.getColor(R.color.color_273145))
            .setTextColorCenter(ResUtils.getColor(R.color.color_273145))
            .setSelectOptions(17)
            .build()

        val group: ViewGroup = mAgePicker.dialogContainerLayout
        group.setBackgroundResource(R.drawable.shape_bg_picker_view)
        mAgePicker.setPicker(mAgePickerList)

    }

    private fun clickCity() {
        TantaCitySelector.start(this)
    }

    override fun initObserve() {
        mViewModel.mSetInfoIsOneKeyPass = mSetInfoIsOneKeyPass

        mViewModel.loginTantaNicknameData.observe(this) {
            mLoginTantaNicknameBean = it

            mBinding.cirbLoginTantaSetInfoGenderBoy.isChecked = true
//            randomNicknameAndAvatar()
        }

        mViewModel.loginTantaNickname.observe(this) {
            mLoginTantaNickname = it
            mBinding.tvLoginTantaSetInfoNickname.setText(it.name)
            refreshCommit()
        }

        mViewModel.loginTantaAvatar.observe(this) {
            mLoginTantaAvatar = it
            mBinding.sivLoginTantaSetInfoAvatar.vquLoadCircleImage(
                NetBaseUrlConstant.IMAGE_URL + it
            )
            refreshCommit()
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

    private fun randomNicknameAndAvatar(
        needNickname: Boolean = false,
        needAvatar: Boolean = true,
        isClickDice: Boolean = false,
    ) {
        when (mBinding.rgLoginTantaSetInfoGenderParent.checkedRadioButtonId) {
            R.id.cirb_login_tanta_set_info_gender_boy -> {
                //男用户
                mBinding.ivLoginTantaSetInfoDiceNickname.visibility = View.VISIBLE
                if (!isClickDice) {
                    mGirlName = mBinding.tvLoginTantaSetInfoNickname.text.toString()
                }
                mBinding.tvLoginTantaSetInfoNickname.setText(mBoyName)
                mViewModel.randomNicknameAndAvatar(
                    mLoginTantaNicknameBean?.male,
                    mBoyName.isBlank() || needNickname,
                    false
                )

                mViewModel.randomNicknameAndAvatar(
                    mLoginTantaNicknameBean?.male_default,
                    false,
                    mUploadImagePath.isNullOrEmpty()
                )
            }

            R.id.cirb_login_tanta_set_info_gender_girl -> {       //女用户
                mBoyName = mBinding.tvLoginTantaSetInfoNickname.text.toString()
                mBinding.ivLoginTantaSetInfoDiceNickname.visibility = View.GONE
                mBinding.tvLoginTantaSetInfoNickname.setText(mGirlName)
                mViewModel.randomNicknameAndAvatar(
                    mLoginTantaNicknameBean?.female_default,
                    false,
                    mUploadImagePath.isNullOrEmpty()
                )
            }
            else -> {
                toast(R.string.tanta_login_gender_hint)
            }
        }
    }

    override fun initRequestData() {
        mViewModel.tantaNickname()

        mViewModel.openInstall()

    }

    private fun refreshCommit() {
        if (mTantaLoginGender != 0 && mTantaAge.isNotEmpty()) {
            val nickname = mBinding.tvLoginTantaSetInfoNickname.text?.toString()
            val avatar = if (mTantaLoginGender == 1) {
                if (DeviceManager.getInstance().channel == "308") {
                    mLoginTantaAvatar ?: ""
                } else {
                    mUploadImagePath ?: ""
                }
            } else {
                mLoginTantaAvatar ?: ""
            }

            if (!nickname.isNullOrBlank() && avatar.isNotEmpty()) {
                mBinding.stvLoginTantaSetInfoNext.setStartColor(
                    ResUtils.getColor(R.color.color_6BBFFD),
                    ResUtils.getColor(R.color.color_4CB6FF)
                )
            } else {
                mBinding.stvLoginTantaSetInfoNext.setStartColor(
                    ResUtils.getColor(R.color.color_D3D1D7),
                    ResUtils.getColor(R.color.color_D3D1D7)
                )
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: LoginEvent) {
        when (event.type) {
            0 -> {
                finish()
            }
        }
    }


    override fun onBackPressed() {

//        super.onBackPressed()
        clickClose()
    }
}