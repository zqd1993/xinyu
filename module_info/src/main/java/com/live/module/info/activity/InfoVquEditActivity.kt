package com.live.module.info.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.CountDownTimer
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.live.module.info.R
import com.live.module.info.adapter.InfoEditImgAdapter
import com.live.module.info.bean.Album
import com.live.module.info.bean.InfoVquUserBean
import com.live.module.info.bean.Tag
import com.live.module.info.databinding.InfoTantaActivityEditBinding
import com.live.module.info.fragment.AuthCommitAgainDialog
import com.live.module.info.vm.InfoEditViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.City
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.*
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.activity.CommonVquVideoCropActivity
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.TantaCitySelector
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectMimeType.ofImage
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.DateUtils
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.mshy.VInterestSpeed.common.bean.UserInCallEvent
import com.mshy.VInterestSpeed.common.event.DelImgEvent
import com.mshy.VInterestSpeed.common.event.DelVideoEvent
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView
import com.mshy.VInterestSpeed.common.ui.view.dragrecyclerview.MyItemTouchCallback
import com.mshy.VInterestSpeed.common.ui.view.dragrecyclerview.OnRecyclerItemClickListener
import com.mshy.VInterestSpeed.common.utils.GlideEngine
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*


/**
 * author: Tany
 * date: 2022/4/6
 * description:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Info.InfoVquEditActivity)
class InfoVquEditActivity : BaseActivity<InfoTantaActivityEditBinding, InfoEditViewModel>(),
    View.OnClickListener, MyItemTouchCallback.OnDragListener {
    private var selectPicList: MutableList<LocalMedia?>? = mutableListOf()
    private val EDIT_NICK_NAME_CODE = 10001
    private val EDIT_VIDEO_CODE = 10006
    var imgAdapter: InfoEditImgAdapter? = null
    var heightList: ArrayList<String>? = ArrayList()
    var weightList: ArrayList<String>? = ArrayList()
    var incomeList: ArrayList<String>? = ArrayList()
    var ageList: ArrayList<String>? = ArrayList()
    var educationList: ArrayList<String>? = ArrayList()
    var marriageList: ArrayList<String>? = ArrayList()
    var occupation1: ArrayList<String>? = ArrayList()
    var occupation2: ArrayList<MutableList<String>>? = ArrayList()
    var provinceList: ArrayList<String>? = ArrayList()
    var cityList: ArrayList<MutableList<String>>? = ArrayList()
    var type: Int = 0//0 身高 1年收入 2 学历 3婚姻状态 4体重 5职业6年龄7省市
    private var vquPicker: OptionsPickerView<String>? = null
    private var vquAgePicker: TimePickerView? = null
    var intentActivityResultLauncher: ActivityResultLauncher<Intent>? = null
    var isAuth: Int = 0
    var gender: Int = 0//1女2男
    var avatarState = 0//0是系统头像 1自定义头像
    var height = ""
    var weight = ""
    var job = ""
    var income = ""
    var education = ""
    var marriage = ""
    var vquCity = ""
    var age = ""
    var sign = ""
    var startSign = ""
    var nickname = ""
    var startName = ""
    var avatar = ""
    var startAvatar = ""
    var voiceTime: Int = 0
    var userTag = ArrayList<Tag>()
    var voiceUrl = ""
    var startVoiceUrl = ""//初始声音
    var videoUrl = ""
    var startVideoUrl = ""//初始视频
    var count = 0
    var label = ""
    var videoPath: String = ""
    var photoUrls = ""
    var startPhotoUrls = ""//初始相册数据
    var loadingDialog: LoadingDialog? = null
    var clickAuth = false
    var isEdit: Boolean = false
    var screenshotUrl: String = "?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto"


    private val mCompressDialog: LoadingDialog by lazy {
        LoadingDialog(this, "压缩中")
    }
    override val mViewModel: InfoEditViewModel by viewModels()
    override fun InfoTantaActivityEditBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(
            "编辑资料",
            "保存",
            R.color.color_7C69FE,
            R.mipmap.ic_back,
            {
                if (isEdit) {//编辑过了 弹框
                    showEditDialog()
                } else {
                    finish()
                }

            },
            {
                saveUserInfo()
            })
        initImg()
        setClickListener()
        vquInitPicker()
//        vquInitAgePicker()
        initResult()
    }

    private fun showEditDialog() {
        CommonHintDialog()
            .setTitle(getString(R.string.setting_tip))
            .setContent("返回上一页将放弃本次修改，是否确认返回?")
            .setLeftText("取消")
            .setRightText("确认返回")
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {}
                override fun onRight(dialogFragment: DialogFragment) {
                    finish()
                }
            })
            .show(supportFragmentManager)


    }

    private fun saveUserInfo() {
        var cityId = TantaCitySelector.getCityId(vquCity)
        if (isSelectImg()) {//如果选择了图片 则调用上传图片
            loadingDialog = LoadingDialog(
                this@InfoVquEditActivity,
                "上传中..."
            )
            loadingDialog?.show()
            mViewModel.vquUploadImg(selectPicList!!, "album")
        } else {
            photoUrls = getLastUrl()
            mViewModel.vquSaveUserInfo(
                age,
                height,
                weight,
                if (vquCity.isNullOrEmpty()) {
                    "0"
                } else {
                    cityId ?: ""
                },
                job,
                income,
                education,
                marriage,
                getTagIds(),
                avatar,
                startAvatar,
                nickname,
                startName,
                photoUrls,
                startPhotoUrls,
                sign,
                startSign,
                voiceUrl,
                startVoiceUrl,
                voiceTime,
                videoUrl,
                startVideoUrl
            )
        }

    }

    fun getAlbumsStr(albumsList: List<Album>): String {
        var str = ""
        albumsList.map {
            if (!it.url.isNullOrEmpty()) {
                str = str + it.url + ","
            }
        }
        if (!str.isNullOrEmpty()) {
            str = str.substring(0, str.length - 1)
        }
        return str
    }

    private fun initResult() {
        intentActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            @Override
            //此处是跳转的result回调方法
            if (it.data != null && it.resultCode == RESULT_OK) {
                var n = it.data?.getStringExtra("nickName")
                if (!n.isNullOrEmpty()) {
                    nickname = n
                    mBinding.tvName.text = nickname
                }
                var s = it.data?.getStringExtra("sign")
                if (s != null) {
                    sign = s
                    mBinding.tvSign.text = sign
                    mBinding.tvInfo.gone()
                }

                var obj = it.data?.getSerializableExtra("userTag")
                if (obj != null) {
                    var tag = it.data?.getSerializableExtra("userTag") as ArrayList<Tag>
                    initTag(tag)
                }
                var myVoiceUrl = it.data?.getStringExtra("voice")
                if (!myVoiceUrl.isNullOrEmpty()) {
                    voiceUrl = myVoiceUrl
                    voiceTime = it.data?.getIntExtra("time", 0)!!
                    if (!voiceUrl.isNullOrEmpty()) {
                        mBinding.tvRecord.hint = "重新录制"
                        setVoiceInfo()
                    }
                }

            }
        }

    }

    private fun setClickListener() {
        mBinding.clHead.setOnClickListener(this)
        mBinding.clAge.setOnClickListener(this)
        mBinding.clHeight.setOnClickListener(this)
        mBinding.clJob.setOnClickListener(this)
        mBinding.clIncome.setOnClickListener(this)
        mBinding.clEducation.setOnClickListener(this)
        mBinding.clMarriage.setOnClickListener(this)
        mBinding.clSign.setOnClickListener(this)
        mBinding.clTag.setOnClickListener(this)
        mBinding.clName.setOnClickListener(this)
        mBinding.clVoice.setOnClickListener(this)
        mBinding.clWeight.setOnClickListener(this)
        mBinding.clCity.setOnClickListener(this)
        mBinding.rlVideo.setOnClickListener(this)
    }

    fun isSelectImg(): Boolean {//判断是否选择图片了
        var isSel = false
        selectPicList?.map {
            if (PictureMimeType.isHasHttp(it?.path)) {

            } else {
                isSel = true
            }
        }
        return isSel
    }

    private fun initImg() {
        imgAdapter = InfoEditImgAdapter(onAddPicClickListener)
        mBinding.rvPhoto.layoutManager = GridLayoutManager(this, 3)
        mBinding.rvPhoto.adapter = imgAdapter
        imgAdapter?.setOnItemClickListener(object : InfoEditImgAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPreviewPictureActivity)
                    .withInt("position", position)
                    .withStringArrayList("urls", getImgUrls())
                    .withBoolean("del", true)
                    .navigation()
            }
        })
        var itemTouchHelper =
            ItemTouchHelper(
                MyItemTouchCallback(
                    imgAdapter
                ).setOnDragListener(this)
            )
        itemTouchHelper.attachToRecyclerView(mBinding.rvPhoto)
        mBinding.rvPhoto.addOnItemTouchListener(object :
            OnRecyclerItemClickListener(mBinding.rvPhoto) {
            override fun onLongClick(vh: RecyclerView.ViewHolder?) {
                super.onLongClick(vh)

                if (vh?.layoutPosition != selectPicList?.size) {
                    itemTouchHelper.startDrag(vh!!)
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: DelImgEvent) {//删除相册图片
        selectPicList?.removeAt(event.position)
        imgAdapter?.notifyDataSetChanged()
        notifyCount()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: DelVideoEvent) {//删除视频
        mBinding.ivVideo.gone()
        videoUrl = ""
        videoPath = ""
        mBinding.ivVideo.setOnClickListener(null)
        mBinding.tvStateVideo.gone()
    }

    fun getImgUrls(): ArrayList<String> {
        var urls = ArrayList<String>()
        selectPicList?.map {
            urls.add(it!!.path)

        }
        return urls
    }

    override fun initObserve() {
        mViewModel.vquGetSelectData.observe(this, Observer {
            var result = it.data
            heightList = result?.height
            incomeList = result?.annual_income
            educationList = result?.education
            marriageList = result?.is_marriage
            weightList = result?.weight
            occupation1?.clear()
            occupation2?.clear()
            result?.occupation?.map {
                occupation1?.add(it.key)
                var o2 = mutableListOf<String>()
                it.`val`.map {
                    o2.add(it)
                }
                occupation2?.add(o2)
            }
        })
        mViewModel.vquUserInfoData.observe(this, Observer {
            initUserInfo(it.data)

        })
        mViewModel.vquVideoUrlData.observe(this, Observer {
            loadingDialog?.dismiss()
            videoUrl = it
            "上传成功".toast()
            videoUrl.logI()
        })
        mViewModel.vquUrlData.observe(this, Observer {
            photoUrls = it
            photoUrls.logI()
            mViewModel.vquSaveUserInfo(
                age,
                height,
                weight,
                if (vquCity.isNullOrEmpty()) {
                    "0"
                } else {
                    TantaCitySelector.getCityId(vquCity) ?: "0"
                },
                job,
                income,
                education,
                marriage,
                getTagIds(),
                avatar,
                startAvatar,
                nickname,
                startName,
                photoUrls,
                startPhotoUrls,
                sign,
                startSign,
                voiceUrl,
                startVoiceUrl,
                voiceTime,
                videoUrl,
                startVideoUrl
            )
        })
        mViewModel.vquUpLoadErrData.observe(this, Observer {
            loadingDialog?.dismiss()
            it.toast()
        })
        mViewModel.vquSaveUserData.observe(this, Observer {
            if (it.code == 0) {
                loadingDialog?.dismiss()
                "提交成功".toast()
                finish()
            } else {
                loadingDialog?.dismiss()
                it.message.toast()
            }

        })
        mViewModel.vquAvatarUrlData.observe(
            this, Observer {
                loadingDialog?.dismiss()
//                if(isAuth==0&&gender==2){//这里改了  不管男女都要比对
//                }else{
                mViewModel.vquCompareFacesAvatar(it)
//                }
                avatar = it
            }
        )
        mViewModel.vquCompareFacesAvatarData.observe(this, Observer {
            it.logI()
            when (it.code) {
                0 -> {//成功
                    avatar = ""//比对通过不用上传给服务器审核
                }

                1007 -> {//未认证
                    mBinding.ivHead.loadAvatar(startAvatar)
                    avatar = ""
                    CommonHintDialog()
                        .setContent("为保证头像真实性，需要认证后才可以上传头像（认证还有新手奖励哦）")
                        .setContentSize(18)
                        .setContentGravity(Gravity.CENTER)
                        .setRightText("去认证")
                        .setClickCancelable(true)
                        .setBackCanceledOnTouchOutside(true)
                        .setOnClickListener(object : CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {
                                dialogFragment.dismiss()
                            }

                            override fun onRight(dialogFragment: DialogFragment) {
                                clickAuth = true
                                ARouter.getInstance().build(RouteUrl.Auth.AuthVquAvatarActivity)
                                    .navigation()
                                dialogFragment.dismiss()
                            }
                        }).show(supportFragmentManager)
                }

                1006 -> {//比对失败
//                    mBinding.ivHead.loadAvatar(startAvatar)
                    var authCommitAgainDialog = AuthCommitAgainDialog()
                    authCommitAgainDialog.setOnSelectListener(object :
                        AuthCommitAgainDialog.SelectListener {
                        override fun again() {//重新上传
                            changeHead()
                        }

                        override fun commit() {//提交人工审核

                        }

                        override fun cancel() {
                            avatar = ""
                            mBinding.ivHead.loadAvatar(startAvatar)
                        }

                    })
                    authCommitAgainDialog.show(supportFragmentManager)
                }

                else -> {
                }
            }
        })
        mViewModel.vquGetCityData.observe(this, Observer {
            provinceList?.clear()
            cityList?.clear()
            provinceList?.add("保密")
            var defCities = mutableListOf<String>()
            defCities.add("保密")
            cityList?.add(defCities)
            it.data.map {
                provinceList?.add(it.name)
                var cities = mutableListOf<String>()
                it.child?.map {
                    cities.add(it.name)
                }
                cityList?.add(cities)
            }
        })
    }

    private fun initUserInfo(info: InfoVquUserBean) {
        isAuth = info.isRpAuth
        gender = info.gender
        avatarState = info.avatarState
        vquCity = info.basicInfoDetail.city
        if (vquCity.isNullOrEmpty()) {
            mBinding.tvCity.text = "保密"
        } else {
            mBinding.tvCity.text = vquCity
        }

        age = info.basicInfoDetail.age
        mBinding.tvAge.text = age
        height = if ("0cm" == info.basicInfoDetail.height) {
            ""
        } else {
            info.basicInfoDetail.height
        }
        mBinding.tvHeight.text = height
        weight = if ("0kg" == info.basicInfoDetail.weight) {
            ""
        } else {
            info.basicInfoDetail.weight
        }
        mBinding.tvLocation.text = info.location + ""
        mBinding.tvWeight.text = weight
        job = info.basicInfoDetail.occupation
        mBinding.tvJob.text = job
        income = info.basicInfoDetail.annualIncome
        mBinding.tvIncome.text = income
        education = info.basicInfoDetail.education
        mBinding.tvEducation.text = education
        marriage = info.basicInfoDetail.isMarriage
        mBinding.tvMarriage.text = marriage
        sign = info.sign
        startSign = info.sign

        if (!sign.isNullOrEmpty()) {
            if (sign == "这个人很懒，什么都没有留下…") {
                sign = ""
                startSign = ""
                mBinding.tvInfo.visible()
            } else {
                mBinding.tvInfo.gone()
            }

        } else {
            mBinding.tvInfo.visible()
        }
        mBinding.tvSign.text = sign
        nickname = info!!.nickname
        startName = info!!.nickname
        mBinding.tvName.text = nickname
        avatar = info!!.avatar


        //认证头像
        if (info.avatarAuthStatus == 1) {
            startAvatar = info!!.avatar
            mBinding.ivHead.loadAvatar(avatar)
            mBinding.tvState.gone()
        } else {
            startAvatar = info!!.avatarAuth
            mBinding.ivHead.loadAvatar(info.avatarAuth)
            mBinding.tvState.visible()
        }
        initTag(info.label)
        if (!info.voice.voice.isNullOrEmpty()) {
            voiceUrl = info.voice.voice
            voiceTime = info.voice.voiceTime
            startVoiceUrl = info.voice.voice
            mBinding.tvRecord.hint = "重新录制"
            if (info.voice.voiceStatus == 1) {
                mBinding.tvStateVoice.gone()
            } else {
                mBinding.tvStateVoice.visible()
            }
            setVoiceInfo()
        } else {
            mBinding.llVoice.gone()
        }

        if (info.videoAuthStatus == 1) {
            videoUrl = info.video
            startVideoUrl = info.video
            mBinding.tvStateVideo.gone()
        } else {
            videoUrl = info.videoAuth
            startVideoUrl = info.videoAuth
            mBinding.tvStateVideo.visible()
        }

        initPhoto(info.albumsList)
        notifyRate()
        if (info?.task != null) {
            if (info?.task.overTask.isShow == 1) {
                mBinding.tvHintPb.text = info?.task.overTask.des
                mBinding.tvHintPb.visible()
            } else {
                mBinding.tvHintPb.gone()
            }
            if (info?.task.avatarTask.isShow == 1) {
                mBinding.tvHintHead.text = info?.task.avatarTask.des
                mBinding.tvHintHead.visible()
            } else {
                mBinding.tvHintHead.gone()
            }
            if (info?.task.albumTask.isShow == 1) {
                mBinding.tvHintPhoto.text = info?.task.albumTask.des
                mBinding.tvHintPhoto.visible()
            } else {
                mBinding.tvHintPhoto.gone()
            }
            if (info?.task.videoTask.isShow == 1) {
                mBinding.tvHintVideo.text = info?.task.videoTask.des
                mBinding.tvHintVideo.visible()
            } else {
                mBinding.tvHintVideo.gone()
            }
            if (info?.task.signTask.isShow == 1) {
                mBinding.tvHintInfo.text = info?.task.signTask.des
                mBinding.tvHintInfo.visible()
            } else {
                mBinding.tvHintInfo.gone()
            }
//            if (!videoUrl.isNullOrEmpty()) {//视频  先隐藏
//                mBinding.ivVideo.visible()
//                mBinding.ivPlay.visible()
//                mBinding.ivVideo.vquLoadRoundImage( NetBaseUrlConstant.IMAGE_URL+videoUrl+screenshotUrl, 10)
//                mBinding.ivVideo.setOnClickListener {
//                    ARouter.getInstance()
//                        .build(RouteUrl.Common.CommonVquVideoActivity)
//                        .withString("video_url",  NetBaseUrlConstant.IMAGE_URL+videoUrl)
//                        .withBoolean("del", true)
//                        .navigation()
//                }
//            }
        }

    }

    fun setVoiceInfo() {
        mBinding.llVoice.visible()
        mBinding.tvVoiceDuration.text = voiceTime.toString() + "\""
        mBinding.llVoice.setViewClickListener {
            var userInCall: UserInCallEvent? =
                SpUtils.getBean(SpKey.user_in_call, UserInCallEvent::class.java)
            if (userInCall?.isUserInCall == true) {
                val typeAudio = if (userInCall?.isAudio) "语音" else "视频"
                toast("正在" + typeAudio + "通话中，请稍后再试...")
                return@setViewClickListener
            }
            startVoice()
        }
    }

    private fun initPhoto(albumsList: List<Album>) {
        if (!albumsList.isNullOrEmpty()) {
            startPhotoUrls = getAlbumsStr(albumsList)
            photoUrls = getAlbumsStr(albumsList)
            albumsList.map {
                var localMedia = LocalMedia()
                localMedia.customData = if (it.status == 1) {
                    ""
                } else {
                    "sh"
                }
                localMedia.path = NetBaseUrlConstant.IMAGE_URL + it.url
                selectPicList?.add(localMedia)
            }
            var size = 0
            if (!albumsList.isNullOrEmpty()) {
                size = albumsList.size
            }
            count = size
            mBinding.tvCount.text = "展示照片（$size/6）"
            imgAdapter?.setList(selectPicList!!)
            imgAdapter?.notifyDataSetChanged()
        }
    }

    fun getLastUrl(): String {
        var str = ""
        selectPicList?.map {

            if (!it?.path.isNullOrEmpty()) {
                var url = it?.path?.replace(NetBaseUrlConstant.IMAGE_URL, "")
                str = "$str$url,"
            }
        }
        if (!str.isNullOrEmpty()) {
            str = str.substring(0, str.length - 1)
        }
        return str
    }

    fun notifyCount() {
        count = 0
        selectPicList?.map {
            if (!it?.path.isNullOrEmpty()) {
                count += 1
            }
        }
        mBinding.tvCount.text = "展示照片（$count/6）"
    }

    private fun initTag(tag: ArrayList<Tag>) {
        if (tag.isNullOrEmpty()) {
            mBinding.tvLabel.visible()
            mBinding.tvL.visible()
        } else {
            mBinding.tvLabel.gone()
            mBinding.tvL.gone()
        }
        userTag = tag
        var topAdapter = object : TagAdapter<Tag>(tag) {
            override fun getView(parent: FlowLayout, position: Int, bean: Tag): View {
                val view = layoutInflater.inflate(
                    R.layout.info_tanta_item_label,
                    mBinding.tagLayout, false
                ) as ConstraintLayout
                var tv = view.findViewById<ShapeTextView>(R.id.tv_tag)
                tv.text = bean.name
                var colorList = bean.color.split(",")
                tv.solidColor = Color.parseColor("#F5F5F5")
                tv.setTextColor(Color.parseColor("#222222"))
                return view
            }

            override fun onSelected(position: Int, view: View) {
                super.onSelected(position, view)
            }

            override fun unSelected(position: Int, view: View) {
                super.unSelected(position, view)
            }
        }
        mBinding.tagLayout.adapter = topAdapter

    }

    override fun initRequestData() {
        mViewModel.vquGetUserInfo()
        mViewModel.vquGetSelectData()
        mViewModel.vquGetProvinceList()
    }

    private val onAddPicClickListener =
        object : InfoEditImgAdapter.OnAddPicClickListener {
            override fun onAddPicClick() {
                isEdit = true
                PermissionUtils.storageUpdatePermission(
                    this@InfoVquEditActivity,
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    requestCallback = { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            PictureSelector.create(this@InfoVquEditActivity)
                                .openGallery(ofImage())
                                .isDisplayCamera(!UserManager.isVideo)
                                .setImageEngine(GlideEngine.createGlideEngine())
//                    .setSandboxFileEngine(object : SandboxFileEngine {
//                        override fun onStartSandboxFileTransform(
//                            context: Context?,
//                            isOriginalImage: Boolean,
//                            index: Int,
//                            media: LocalMedia?,
//                            listener: OnCallbackIndexListener<LocalMedia>?,
//                        ) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                media?.sandboxPath = SandboxTransformUtils.copyPathToSandbox(
//                                    context,
//                                    media?.realPath,
//                                    media?.mimeType
//                                )
//                                listener?.onCall(media, index)
//                            }
//                        }
//                    })
                                .setCompressEngine { _, list, listener ->
                                    if (!list.isNullOrEmpty()) {
                                        list.map {
                                            if (PictureMimeType.isHasHttp(it?.path)) {
                                            } else {
                                                Luban.with(this@InfoVquEditActivity)
                                                    .load(it.realPath)
                                                    .ignoreBy(200)
                                                    .setCompressListener(object :
                                                        OnCompressListener {
                                                        override fun onStart() {
                                                            mCompressDialog.show()
                                                        }

                                                        override fun onSuccess(
                                                            index: Int,
                                                            compressFile: File?,
                                                        ) {
                                                            mCompressDialog.dismiss()
                                                            it.compressPath =
                                                                compressFile?.absolutePath
                                                            listener?.onCall(list)
                                                        }

                                                        override fun onError(
                                                            index: Int,
                                                            e: Throwable?
                                                        ) {
                                                            mCompressDialog.dismiss()
                                                        }
                                                    }).launch()
                                            }
                                        }

                                    }
                                }
                                .setSelectionMode(SelectModeConfig.MULTIPLE)
                                .isPageStrategy(true, 20, true)
                                .setMaxSelectNum(6)
                                .setSelectedData(imgAdapter?.getData())
                                .forResult(PictureConfig.CHOOSE_REQUEST)
                        } else {
                            toast("无法获取储存权限")
                        }
                    })
            }
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
                }

                TantaCitySelector.REQUEST_CODE -> {
                    val city = data?.getParcelableExtra<City>(TantaCitySelector.CITY_RESULT)
                    if (city != null) {
                        vquCity = city.name
                        mBinding.tvCity.text = city.name
                    }
                }

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
        mBinding.ivVideo.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Common.CommonVquVideoActivity)
                .withString("video_url", videoPath)
                .withBoolean("del", true)
                .navigation()
        }
        loadingDialog =
            LoadingDialog(
                this@InfoVquEditActivity,
                "上传中"
            )
        loadingDialog?.show()
        mViewModel.vquUploadVideo(videoPath, "video")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_head -> {//换头像
                isEdit = true
                PermissionUtils.storageUpdatePermission(
                    this@InfoVquEditActivity,
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    requestCallback = { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            changeHead()
                        } else {
                            toast("无法获取储存权限")
                        }
                    })
            }

            R.id.cl_voice -> {//声音展示
                isEdit = true
                var intent = Intent(this@InfoVquEditActivity, InfoVquVoiceEditActivity::class.java)
                intentActivityResultLauncher?.launch(intent)
            }

            R.id.cl_name -> {//昵称
                isEdit = true
                var intent = Intent(this@InfoVquEditActivity, InfoVquNicknameActivity::class.java)
                intent.putExtra("nickName", mBinding.tvName.text.toString())
                intentActivityResultLauncher?.launch(intent)

            }

            R.id.cl_age -> {//生日
                isEdit = true
                type = 6
                ageList?.clear()
                for (index in 18..60) {
                    ageList?.add(index.toString() + "岁")
                }
                vquPicker?.setPicker(ageList)
                vquPicker?.setSelectOptions(ageList!!.indexOf("35岁"))
                vquPicker?.show()
            }

            R.id.cl_height -> {//身高
                isEdit = true
                type = 0
                vquPicker?.setPicker(heightList)
                vquPicker?.setSelectOptions(heightList!!.indexOf("170cm"))
                vquPicker?.show()
            }

            R.id.cl_income -> {//收入
                isEdit = true
                type = 1
                vquPicker?.setPicker(incomeList)
                vquPicker?.setSelectOptions(0)
                vquPicker?.show()
            }

            R.id.cl_education -> {//学历
                isEdit = true
                type = 2
                vquPicker?.setPicker(educationList)
                vquPicker?.setSelectOptions(0)
                vquPicker?.show()
            }

            R.id.cl_marriage -> {//婚姻状态
                isEdit = true
                type = 3
                vquPicker?.setPicker(marriageList)
                vquPicker?.setSelectOptions(0)
                vquPicker?.show()
            }

            R.id.cl_weight -> {//体重
                isEdit = true
                type = 4
                vquPicker?.setPicker(weightList)
                vquPicker?.setSelectOptions(weightList!!.indexOf("50kg"))
                vquPicker?.show()
            }

            R.id.cl_job -> {//职业
                isEdit = true
                type = 5
                vquPicker?.setSelectOptions(0, 0)
                vquPicker?.setPicker(occupation1, occupation2)
                vquPicker?.show()
            }

            R.id.cl_sign -> {//自我介绍
                isEdit = true
                var intent = Intent(this@InfoVquEditActivity, InfoVquIntroduceActivity::class.java)
                intent.putExtra("sign", mBinding.tvSign.text.toString())
                intentActivityResultLauncher?.launch(intent)
            }

            R.id.cl_city -> {//所在地
                if (provinceList.isNullOrEmpty()) {
                    return
                }
                if (cityList.isNullOrEmpty()) {
                    return
                }
                isEdit = true
                type = 7
                vquPicker?.setSelectOptions(0, 0)
                vquPicker?.setPicker(provinceList, cityList)
                vquPicker?.show()

//                VquCitySelector.start(this)
            }

            R.id.rl_video -> {//选择视频
                isEdit = true
                PictureSelector.create(this@InfoVquEditActivity)//进页面就进行选择
                    .openGallery(SelectMimeType.ofVideo())
                    .setMaxSelectNum(1)
                    .isDisplayCamera(!UserManager.isVideo)
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
                                if (video.duration / 1000 > 15) {
                                    CommonVquVideoCropActivity.startActivityForResult(
                                        this@InfoVquEditActivity,
                                        videoPathParent,
                                        videoFileName,
                                        15 * 1000,
                                        EDIT_VIDEO_CODE
                                    )
                                } else {
                                    vquVideo()
                                }
                            }


                        }

                        override fun onCancel() {
                        }
                    })
            }

            R.id.cl_tag -> {//我的标签
                isEdit = true
                var intent = Intent(this@InfoVquEditActivity, InfoVquLabelEditActivity::class.java)
                intentActivityResultLauncher?.launch(intent)
            }
        }
    }


    //更换头像
    private fun changeHead() {
        PictureSelector.create(this@InfoVquEditActivity)
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
                val dataCropSource = ArrayList<String>()
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
                uCrop.start(this@InfoVquEditActivity, fragment, requestCode)

            }
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    if (result.isNullOrEmpty()) {
                        return
                    }
                    var avatarPath = result!![0]?.cutPath
                    mBinding.ivHead.loadAvatar(avatarPath, true)
                    loadingDialog =
                        LoadingDialog(
                            this@InfoVquEditActivity,
                            "上传中..."
                        )
                    loadingDialog?.show()
                    mViewModel.vquUploadAvatar(result!![0])

                }

                override fun onCancel() {
                }
            })


    }

    override fun onResume() {
        super.onResume()
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
                this@InfoVquEditActivity,
                R.color.ps_color_grey
            )
        )
        options.setToolbarColor(
            ContextCompat.getColor(
                this@InfoVquEditActivity,
                R.color.ps_color_grey
            )
        )
        options.setToolbarWidgetColor(
            ContextCompat.getColor(
                this@InfoVquEditActivity,
                R.color.ps_color_white
            )
        )
        return options
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private fun getSandboxPath(): String? {
        val externalFilesDir: File = this@InfoVquEditActivity.getExternalFilesDir("")!!
        val customFile = File(externalFilesDir.absolutePath, "Sandbox")
        if (!customFile.exists()) {
            customFile.mkdirs()
        }
        return customFile.absolutePath + File.separator
    }

//    private fun vquInitAgePicker() {
//        val endCalendar = Calendar.getInstance()
//        endCalendar[endCalendar[Calendar.YEAR] - 18, endCalendar[Calendar.MONTH]] =
//            endCalendar[Calendar.DAY_OF_MONTH]
//        val startCalendar = Calendar.getInstance()
//        startCalendar[1940, 1] = 1
//        vquAgePicker = TimePickerBuilder(this, OnTimeSelectListener { date, v ->
//            val calendar = Calendar.getInstance()
//            calendar.time = date
//            val month = calendar[Calendar.MONTH] + 1
//            val day = calendar[Calendar.DATE]
//            val year = calendar[Calendar.YEAR]
//            mBinding.tvAge.text = "$year-$month-$day"
//            birthday="$year-$month-$day"
//        }).setBgColor(android.R.color.transparent)
//            .setTitleBgColor(android.R.color.transparent)
//            .setCancelColor(Color.parseColor("#A3AABE"))
//            .setSubmitColor(Color.parseColor("#273145"))
//            .setTextColorCenter(resources.getColor(R.color.color_273145))
//            .setRangDate(startCalendar, endCalendar).build()
//        val group: ViewGroup = vquAgePicker!!.getDialogContainerLayout()
//        group.setBackgroundResource(R.drawable.shape_white_top_8_bg)
//        vquAgePicker!!.setDate(endCalendar)
//    }

    private fun vquInitPicker() {
        vquPicker =
            OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
                when (type) {
                    0 -> {
                        height = heightList!![options1]
                        mBinding.tvHeight.text = height
                    }

                    1 -> {
                        income = incomeList!![options1]
                        mBinding.tvIncome.text = income
                    }

                    2 -> {
                        education = educationList!![options1]
                        mBinding.tvEducation.text = education
                    }

                    3 -> {
                        marriage = marriageList!![options1]
                        mBinding.tvMarriage.text = marriage
                    }

                    4 -> {
                        weight = weightList!![options1]
                        mBinding.tvWeight.text = weight
                    }

                    5 -> {
                        job = occupation2!![options1][options2]
                        mBinding.tvJob.text = job
                    }

                    6 -> {
                        age = ageList!![options1]
                        mBinding.tvAge.text = age
                    }

                    7 -> {
                        vquCity = cityList!![options1][options2]
                        mBinding.tvCity.text = vquCity
                    }
                }

            }).isDialog(true).setLayoutRes(R.layout.common_vqu_layout_picker_rates) { v ->

                val btnSubmit = v.findViewById<Button>(R.id.btnSubmit)
                btnSubmit.setOnClickListener {
                    vquPicker?.returnData()
                    vquPicker?.dismiss()
//                notifyRate()
                }
                v.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                    vquPicker?.dismiss()
                }
            }.setDividerColor(0x00000000).build<String>()
        val layoutParams =
            vquPicker?.getDialogContainerLayout()?.getLayoutParams() as FrameLayout.LayoutParams
        layoutParams.leftMargin = 0
        layoutParams.rightMargin = 0
        vquPicker?.getDialogContainerLayout()?.setLayoutParams(layoutParams)
        val parentLayout = vquPicker?.getDialogContainerLayout()?.getParent() as FrameLayout
        val parentParams = parentLayout.layoutParams as FrameLayout.LayoutParams
        parentLayout.setBackgroundResource(R.drawable.bg_radius_white_19)
        parentParams.leftMargin = 0
        parentParams.rightMargin = 0
        parentParams.bottomMargin = 0
        parentParams.width = WindowManager.LayoutParams.MATCH_PARENT
        val window: Window = vquPicker?.getDialog()?.getWindow()!!
        if (window != null) {
            val attributes = window.attributes
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            window.attributes = attributes
            window.setWindowAnimations(R.style.picker_view_slide_anim)
            window.attributes.gravity = Gravity.BOTTOM
        }
    }

    override fun onFinishDrag() {

    }

    override fun onBackPressed() {
        if (isEdit) {//编辑过了 弹框
            showEditDialog()
        } else {
            finish()
        }
    }

    fun notifyRate() {
        var num = 0
        if (!vquCity.isNullOrEmpty()) {
            num += 1
        }
        if (!age.isNullOrEmpty()) {
            num += 1
        }
        if (!height.isNullOrEmpty()) {
            num += 1
        }
        if (!weight.isNullOrEmpty()) {
            num += 1
        }
        if (!job.isNullOrEmpty()) {
            num += 1
        }
        if (!income.isNullOrEmpty()) {
            num += 1
        }
        if (!education.isNullOrEmpty()) {
            num += 1
        }
        if (!marriage.isNullOrEmpty()) {
            num += 1
        }
        if (!sign.isNullOrEmpty()) {
            num += 1
        }
        if (!nickname.isNullOrEmpty()) {
            num += 1
        }
        if (!avatar.isNullOrEmpty()) {
            if (avatarState == 1) {
                num += 1
            }
        }
//        if (!videoUrl.isNullOrEmpty()) {
//            num += 1
//        }
        if (!voiceUrl.isNullOrEmpty()) {
            num += 1
        }

        if (!userTag.isNullOrEmpty()) {
            num += 1
        }
        if (count > 0) {
            num += 1
        }

        mBinding.progressbar.progress = num
        mBinding.tvRate.text = (num * 100 / 14).toInt().toString() + "%"
        val lastTxtParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lastTxtParams.setMargins(
            ((ScreenUtil.screenWidth - ScreenUtil.dip2px(60.0f)) * (num / 14.0)).toInt(),
            20,
            0,
            0
        )
        mBinding.tvRate.layoutParams = lastTxtParams
        mBinding.tvRate.invalidate()
    }

    fun getTagIds(): String {
        label = ""
        userTag.map {
            label = label + it.id + ","
        }
        if (!label.isNullOrEmpty()) {
            label = label.substring(0, label.length - 1)
        }
        return label

    }

    private var playType = 0
    var countDownTimer: CountDownTimer? = null
    private fun startVoice() {
        if (playType == 1) { //正在播放中
            playType = 0
            stop()
            mBinding.tvVoiceDuration.setText("$voiceTime\"")
            mBinding.ivPlayVoice.setImageResource(R.mipmap.ic_tanta_info_stop)
            mBinding.lvWave.pauseAnimation()
        } else {
            mBinding.lvWave.playAnimation()
            countDownTimer =
                object : CountDownTimer(voiceTime * 1000.toLong(), 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        playType = 1
                        mBinding.tvVoiceDuration.text =
                            (millisUntilFinished / 1000).toString() + "\""
                    }

                    override fun onFinish() {
                        playType = 0
                        mBinding.ivPlayVoice.setImageResource(R.mipmap.ic_tanta_info_stop)
                        mBinding.tvVoiceDuration.text = "$voiceTime\""
                        mBinding.lvWave.pauseAnimation()
                    }
                }
            countDownTimer?.start()
            play(voiceUrl)
            mBinding.ivPlayVoice.setImageResource(R.mipmap.ic_info_tanta_playing)
        }
    }

    var mediaPlayer: MediaPlayer? = null

    /**
     * 148.148      * 停止播放
     * 149.149       */
    private fun stop() {
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer!!.onFinish()
        }
    }

    fun play(url: String) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            mediaPlayer?.reset()
            // 设置指定的流媒体地址
            mediaPlayer?.setDataSource(NetBaseUrlConstant.IMAGE_URL + url)
            // 设置音频流的类型
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            // 通过异步的方式装载媒体资源
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                // 装载完毕 开始播放流媒体
                mediaPlayer?.start()
                // 避免重复播放，把播放按钮设置为不可用
            }

            // 设置循环播放
            // mediaPlayer.setLooping(true);
            mediaPlayer?.setOnCompletionListener { // 在播放完毕被回调
                stop()
            }
            mediaPlayer?.setOnErrorListener { mp, what, extra -> // 如果发生错误，重新播放
                // ToastUtil.showToast(getActivity(),"网络异常，播放失败");
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "网络异常，播放失败".toast()
        }
    }

    override fun onPause() {
        super.onPause()
        stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }
}

