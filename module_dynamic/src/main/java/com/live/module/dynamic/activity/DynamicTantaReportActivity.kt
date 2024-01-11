package com.live.module.dynamic.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.dynamic.R
import com.live.module.dynamic.adapter.DynamicTantaAddImgAdapter
import com.live.module.dynamic.adapter.DynamicTantaReportAdapter
import com.live.module.dynamic.databinding.DynamicTantaActivityReportBinding
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.*
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.vm.UploadViewModel
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog
import com.mshy.VInterestSpeed.common.utils.GlideEngine
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Tany
 * date: 2022/4/21
 * description:动态举报页面
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquReportActivity)
class DynamicTantaReportActivity :
    BaseActivity<DynamicTantaActivityReportBinding, UploadViewModel>() {
    @Autowired(name = RouteKey.USERID)
    @JvmField
    var userId = 0

    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0

    var content: String? = ""

    var imgAdapter: DynamicTantaAddImgAdapter? = null
    var loadingDialog: LoadingDialog? = null
    private var selectPicList: MutableList<LocalMedia?>? = null
    var vquReportAdapter = DynamicTantaReportAdapter()
    override val mViewModel: UploadViewModel by viewModels()

    override fun DynamicTantaActivityReportBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.dynamic_report)) {
            finish()
        }
        mBinding.tvCommit.isEnabled = false
        mBinding.tvCommit.isClickable = false
        mBinding.rvReason.adapter = vquReportAdapter
        vquReportAdapter.setNbOnItemClickListener { adapter, view, position ->
            vquReportAdapter.setSel(position)
            mBinding.tvCommit.isEnabled = true
            mBinding.tvCommit.isClickable = true
            mBinding.tvCommit.setStartColor(
                resources.getColor(R.color.color_6BBFFD),
                resources.getColor(R.color.color_4CB6FF)
            )
        }
        initImg()
        mBinding.tvCommit.setViewClickListener {
            if (!isFastClick()) {
                content = mBinding.etContent.text.toString().trim()
                if (vquReportAdapter?.getSelId() == 0) {
                    "至少选择一项原因".toast()
                } else {
                    if (selectPicList.isNullOrEmpty()) {//没有上传图片
                        mViewModel.vquReport(
                            type,
                            vquReportAdapter?.getSelId(),
                            userId.toString(),
                            content,
                            ""
                        )
                    } else {
                        loadingDialog =
                            LoadingDialog(this@DynamicTantaReportActivity)
                        loadingDialog?.show()
                        mViewModel.vquUploadImg(selectPicList!!, "album")
                    }
                }
            }
        }
        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                mBinding.tvHint.text = s.toString().trim().length.toString() + "/100"
            }
        })
    }

    override fun initObserve() {
        mViewModel.vquReportCate()
    }

    override fun initRequestData() {
        mViewModel.vquUrlData.observe(this, Observer {
            loadingDialog?.dismiss()
            mViewModel.vquReport(type, vquReportAdapter?.getSelId(), userId.toString(), content, it)

        })
        mViewModel.vquReportCateData.observe(this, Observer {
            vquReportAdapter.setList(it.data)
        })
        mViewModel.vquReportData.observe(this, Observer {
            val messageDialog = MessageDialog()
            messageDialog.setTitle(R.string.dynamic_report_success)
            messageDialog.setContent(R.string.dynamic_report_success_hint)
            messageDialog.setLeftText(R.string.dynamic_report_urgency)
            messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
                override fun onLeftClick(): Boolean {
                    NimUIKit.startP2PSession(this@DynamicTantaReportActivity, "4");
                    finish()
                    return true
                }

                override fun onRightClick(): Boolean {
                    finish()
                    return false
                }

            })
            messageDialog.show(supportFragmentManager, "")

        })
    }

    private val onAddPicClickListener =
        object : DynamicTantaAddImgAdapter.OnAddPicClickListener {
            override fun onAddPicClick() {
                PermissionUtils.storageUpdatePermission(
                    this@DynamicTantaReportActivity,
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    requestCallback = { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            PictureSelector.create(this@DynamicTantaReportActivity)
                                .openGallery(SelectMimeType.ofImage())
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isDisplayCamera(!UserManager.isVideo)
                                .setSelectionMode(SelectModeConfig.MULTIPLE)
                                .setSandboxFileEngine(object : SandboxFileEngine {
                                    override fun onStartSandboxFileTransform(
                                        context: Context?,
                                        isOriginalImage: Boolean,
                                        index: Int,
                                        media: LocalMedia?,
                                        listener: OnCallbackIndexListener<LocalMedia>?
                                    ) {
                                        SandboxTransformUtils.copyPathToSandbox(
                                            context,
                                            media?.realPath,
                                            media?.mimeType
                                        )
                                    }
                                })
                                .isPageStrategy(true, 20, true)
                                .setMaxSelectNum(9)
                                .setSelectedData(imgAdapter?.getData())
                                .forResult(PictureConfig.CHOOSE_REQUEST)
                        } else {
                            toast("缺少摄像头权限")
                        }
                    })
            }
        }

    private fun initImg() {
        imgAdapter = DynamicTantaAddImgAdapter(onAddPicClickListener)
        mBinding.rvImg.layoutManager = GridLayoutManager(this, 3)
        mBinding.rvImg.adapter = imgAdapter
        imgAdapter?.setOnItemClickListener(object : DynamicTantaAddImgAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPreviewPictureActivity)
                    .withInt("position", position)
                    .withStringArrayList("urls", getImgUrls())
                    .withBoolean("del", false)
                    .navigation()
            }
        })

    }

    fun getImgUrls(): ArrayList<String> {
        var urls = ArrayList<String>()
        selectPicList?.map {
            urls.add(it!!.realPath)
        }
        return urls
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectPicList = PictureSelector.obtainSelectorList(data)
                    imgAdapter?.setList(selectPicList!!)
                    imgAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

}