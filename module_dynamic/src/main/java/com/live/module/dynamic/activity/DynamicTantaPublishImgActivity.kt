package com.live.module.dynamic.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.live.module.dynamic.R
import com.live.module.dynamic.adapter.DynamicTantaAddImgAdapter
import com.live.module.dynamic.databinding.DynamicTantaActivityPublishImgBinding
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.StateLayoutEnum
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.logI
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.mshy.VInterestSpeed.common.bean.StsInfoBean
import com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog
import com.mshy.VInterestSpeed.common.utils.GlideEngine
import com.mshy.VInterestSpeed.common.utils.TimeZoneUtils
import com.mshy.VInterestSpeed.common.utils.UmUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.io.Serializable


/**
 * author: Tany
 * date: 2022/3/31
 * description:动态发图片
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquPublishImgActivity)
class DynamicTantaPublishImgActivity :
    BaseActivity<DynamicTantaActivityPublishImgBinding, DynamicPublishViewModel>() {
    var maxNum = 9
    var content: String? = ""
    private var selectPicList: MutableList<LocalMedia?>? = null
    var imgAdapter: DynamicTantaAddImgAdapter? = null
    override val mViewModel: DynamicPublishViewModel by viewModels()
    private var mSelectPath: ArrayList<String> = ArrayList()
    var loadingDialog: LoadingDialog? = null
    private var mImages: StringBuilder? = null
    var oss: OSS? = null
    var STS: StsInfoBean? = null
    var submitCount = 0
    var images: String? = ""
    var rightBtn: Button? = null
    var isFirst: Boolean? = true
    private val mCompressDialog: LoadingDialog by lazy {
        LoadingDialog(this, "压缩中")
    }

    override fun DynamicTantaActivityPublishImgBinding.initView() {
        mBinding.includeTitle.toolbar.run {
            initClose("发布图片") {
                finish()
            }

        }
        mBinding.publishTipStv.setViewClickListener { showTipDialog() }
        rightBtn = mBinding.includeTitle.toolbar.findViewById(R.id.btn_right)
        rightBtn?.isClickable = false
        rightBtn?.text = "发布"
        rightBtn?.isEnabled = false
        rightBtn?.visible()
        isFirst = SpUtils.getBoolean("isFirstPublish", true)


//        PictureSelector.create(this@DynamicVquPublishImgActivity)//进页面就进行选择
//            .openGallery(SelectMimeType.ofImage())
//            .setImageEngine(GlideEngine.createGlideEngine())
//            .isDisplayCamera(!UserManager.isVideo)
//            .setSandboxFileEngine(object : SandboxFileEngine {
//                override fun onStartSandboxFileTransform(
//                    context: Context?,
//                    isOriginalImage: Boolean,
//                    index: Int,
//                    media: LocalMedia?,
//                    listener: OnCallbackIndexListener<LocalMedia>?
//                ) {
//                    SandboxTransformUtils.copyPathToSandbox(context,media?.realPath,media?.mimeType)
//                }
//            })
//            .setCompressEngine { _, list, listener ->
//                if (!list.isNullOrEmpty()) {
//                    list.map {
//                        Luban.with(this@DynamicVquPublishImgActivity).load(it.realPath)
//                            .ignoreBy(200)
//                            .setCompressListener(object : OnCompressListener {
//                                override fun onStart() {
//                                    mCompressDialog.show()
//                                }
//
//                                override fun onSuccess(index: Int, compressFile: File?) {
//                                    mCompressDialog.dismiss()
//                                    it.compressPath = compressFile?.absolutePath
//                                    listener?.onCall(list)
//                                }
//
//                                override fun onError(index: Int, e: Throwable?) {
//                                    mCompressDialog.dismiss()
//                                }
//                            }).launch()
//                    }
//
//                }
//            }
//            .setMaxSelectNum(maxNum)
//            .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                override fun onResult(result: ArrayList<LocalMedia?>?) {
//                    selectPicList = result
//                    imgAdapter?.setList(selectPicList!!)
//                    imgAdapter?.notifyDataSetChanged()
//                    if (isFirst!!) {
//                        showTipDialog()
//                    }
//                }
//
//                override fun onCancel() {
//                    finish()
//                }
//            })
        mBinding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                content = s.toString().trim()
                mBinding.tvHint.text = content?.length.toString() + "/100"
            }
        })
        initImg()
        getStsInfo()
        mBinding.tvPublish.setViewClickListener { vquPublishImg() }
        rightBtn?.setViewClickListener { vquPublishImg() }
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
        selectPicList = imgList as ArrayList<LocalMedia?>?
        imgAdapter?.setList(selectPicList!!)
        imgAdapter?.notifyDataSetChanged()
        if (isFirst!!) {
            showTipDialog()
        }

    }

    fun changeState() {
        mSelectPath = getImgUrls()
        if (!content.isNullOrEmpty() && !mSelectPath.isNullOrEmpty()) {
            rightBtn?.isClickable = true
            rightBtn?.isEnabled = true
        } else {
            rightBtn?.isEnabled = false
        }
    }

    private fun getStsInfo() {
        mViewModel.vquGetSts()
    }

    private fun vquPublishImg() {
        mSelectPath = getImgUrls()
        mSelectPath.logI()
        if (content.isNullOrEmpty()) {
            "动态内容不能为空".toast()
            return
        }
        if (mSelectPath.isNullOrEmpty()) {
            "图片不能为空".toast()
            return
        }
        UmUtils.setUmEvent(this@DynamicTantaPublishImgActivity, UmUtils.CLICKTOPUBLISH)
        loadingDialog =
            LoadingDialog(this@DynamicTantaPublishImgActivity,
                "上传中...")
        loadingDialog?.show()
        Thread { compressAndUpload() }.start()

    }

    private fun compressAndUpload() {
        mImages = StringBuilder()
        if (mSelectPath != null) {
            //    Iterator<String> i = mSelectPath.iterator();
            runOnUiThread { uploadImage(mSelectPath[0], content!!) }
        } else {
//            progressDialog!!.dismiss()
        }
    }

    private fun uploadImage(uploadFiles: String, content: String) {
        //MultipartUploadRequest request = new MultipartUploadRequest(STS.getBucketName(), STS.getDir()+TimeZoneUtils.getCurrentYearMonthDay(), file);
        // 构造上传请求
        val put = PutObjectRequest(
            STS?.bucketName,
            STS?.dir + TimeZoneUtils.getCurrentYearMonthDayJpg(),
            uploadFiles
        )
        if (oss == null) {//解决空指针bug
            return
        }
        val task: OSSAsyncTask<*> = oss!!.asyncPutObject(
            put,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult?> {
                override fun onSuccess(request: PutObjectRequest, result: PutObjectResult?) {
                    submitCount++
                    mImages?.append(request.objectKey.toString() + ",")
                    if (mSelectPath != null && mSelectPath.size > 0) {
                        if (mSelectPath[mSelectPath.size - 1] == "") {
                            if (submitCount == mSelectPath.size - 1) {
                                runOnUiThread {
                                    postDynamics(
                                        content,
                                        mImages!!.substring(0, mImages!!.length - 1)
                                    )
                                }
                            } else {
                                runOnUiThread {
                                    uploadImage(
                                        mSelectPath[submitCount],
                                        content
                                    )
                                }
                            }
                        } else {
                            if (submitCount == mSelectPath.size) {
                                runOnUiThread {
                                    postDynamics(
                                        content,
                                        mImages!!.substring(0, mImages!!.length - 1)
                                    )
                                }
                            } else {
                                runOnUiThread {
                                    uploadImage(
                                        mSelectPath[submitCount],
                                        content
                                    )
                                }
                            }
                        }
                    }
                }

                override fun onFailure(
                    request: PutObjectRequest,
                    clientExcepion: ClientException,
                    serviceException: ServiceException,
                ) {
                    dismissLoadingDialog()
                    submitCount = 0
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace()
                    }
                    if (serviceException != null) {
                        // 服务异常
                    }
                }
            })
    }

    fun postDynamics(content: String, image: String) {
        images = image
        mViewModel.vquPublishImg(content, 0)
    }

    fun dismissLoadingDialog() {
        submitCount = 0
        loadingDialog?.dismiss()
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

    private val onAddPicClickListener =
        object : DynamicTantaAddImgAdapter.OnAddPicClickListener {
            override fun onAddPicClick() {
                PictureSelector.create(this@DynamicTantaPublishImgActivity)
                    .openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isDisplayCamera(!UserManager.isVideo)
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
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .isPageStrategy(true, 20, true)
                    .setMaxSelectNum(maxNum)
                    .setSelectedData(imgAdapter?.getData())
                    .forResult(PictureConfig.CHOOSE_REQUEST)
            }
        }

    fun getImgUrls(): ArrayList<String> {
        var urls = ArrayList<String>()
        selectPicList?.map {
            val realPath = if (it?.compressPath.isNullOrEmpty()) {
                it?.realPath
            } else {
                it?.compressPath
            }
            urls.add(realPath!!)
        }
        return urls
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectPicList = PictureSelector.obtainSelectorList(data)
                    imgAdapter?.setList(selectPicList!!)
                    imgAdapter?.notifyDataSetChanged()
                    changeState()
                }
            }
        }
    }

    override fun initObserve() {
        mViewModel.vquUploadImageData.observe(this) {
            if (null != images) {
                setImageUrl(it.data.dynamic_id, images!!);
            } else {
                dismissLoadingDialog()
                "动态上传成功".toast()
                setResult(RESULT_OK);
                EventBus.getDefault().post("refreshDynamic")
                finish();
            }
        }

        mViewModel.vquUploadImageUrlData.observe(this) {
            "动态上传成功".toast()
            EventBus.getDefault().post("refreshDynamic")
            dismissLoadingDialog()
            finish()
        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.HIDE -> {
                    dismissLoadingDialog()
                }
            }
        }
        mViewModel.vquStsData.observe(this) {
            STS = it.data

            val endpoint = STS?.endpoint
            //该配置类如果不设置，会有默认配置，具体可看该类
            //该配置类如果不设置，会有默认配置，具体可看该类
            val conf = ClientConfiguration()
            conf.setConnectionTimeout(15 * 1000) // 连接超时，默认15秒

            conf.setSocketTimeout(15 * 1000) // socket超时，默认15秒

            conf.setMaxConcurrentRequest(5) // 最大并发请求数，默认5个

            conf.setMaxErrorRetry(2) // 失败后最大重试次数，默认2次


            val credentialProvider: OSSCredentialProvider = OSSStsTokenCredentialProvider(
                it.data.getAccessKeyId(),
                it.data.getAccessKeySecret(),
                it.data.getSecurityToken()
            )
            oss = OSSClient(applicationContext, endpoint, credentialProvider, conf)
        }
    }

    fun setImageUrl(dynamicId: String, base64String: String) {
        mViewModel.vquPublishImgUrl(dynamicId, base64String)
    }

    override fun initRequestData() {
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {

    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.dynamic_vqu_publish_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.iv_tip) {
//
//        }
//        return super.onOptionsItemSelected(item)
//    }
}