package com.live.module.info.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.info.R
import com.live.module.info.adapter.*
import com.live.module.info.bean.*
import com.live.module.info.databinding.InfoTantaActivityInfoBinding
import com.live.module.info.vm.InfoVquViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.*
import com.mshy.VInterestSpeed.common.helper.CommonVquAddRankAndGradeViewHelper
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog.OnButtonSelectListener
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquCallDialog
import com.mshy.VInterestSpeed.common.ui.dialog.SetReMarkNameDialog
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewBuilder
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewVideoImgBuilder
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.UserViewInfo
import com.mshy.VInterestSpeed.common.utils.UmUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.listener.OnPageChangeListener
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Tany
 * date: 2022/4/26
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Info.InfoVquPersonalInfoActivity)
open class InfoVquPersonalInfoActivity :
    BaseActivity<InfoTantaActivityInfoBinding, InfoVquViewModel>() {
    @Autowired(name = RouteKey.USERID)
    @JvmField
    var userId = 0

    @Autowired(name = RouteKey.FROM_CHAT)
    @JvmField
    var isFromChat: Boolean = false
    var voiceUrl: String = ""
    var voiceTime: Int = 0
    private var playType = 0
    var black: Int = 0
    var countDownTimer: CountDownTimer? = null
    override val mViewModel: InfoVquViewModel by viewModels()//主页
    private var mSelectiveDialog: BottomSelectiveDialog? =
        null
    var myAdapter: InfoVquImgBannerAdapter? = null
    var isChat: Boolean = false
    var giftAdapter: InfoVquGiftAdapter? = null
    var remarkName: String = ""
    var nickName: String = ""
    var isInit = false
    var screenshotUrl: String = "?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto"

    @SuppressLint("NewApi")
    override fun InfoTantaActivityInfoBinding.initView() {
        UmUtils.setUmEvent(this@InfoVquPersonalInfoActivity,
            UmUtils.ENTERPERSONALCENTER)
        ImmersionBar.with(this@InfoVquPersonalInfoActivity).transparentStatusBar()
            .fitsSystemWindows(false).init()
        mBinding.viewBg.alpha = 0f
        mBinding.nes.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            mBinding.viewBg.alpha = scrollY / 600f
            if (scrollY > 300) {
                mBinding.ivBack.setImageResource(R.mipmap.ic_vqu_info_back_black)
                mBinding.ivMore.setImageResource(R.mipmap.ic_vqu_info_more_black)
            } else {
                mBinding.ivBack.setImageResource(R.mipmap.ic_vqu_info_back_white)
                mBinding.ivMore.setImageResource(R.mipmap.ic_vqu_info_more_white)
            }
        }
        mBinding.ivBack.setViewClickListener { finish() }
        mBinding.ivBackBlocked.setViewClickListener { finish() }
        mBinding.ivMore.setViewClickListener { showMenu() }

        mBinding.tvEdit.setOnClickListener {
            ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
        }
        mBinding.ivVideo.setViewClickListener { vquCall() }
        mBinding.clDynamic.setViewClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Dynamic.DynamicVquDynamicListActivity)
                .withInt(
                    RouteKey.USERID,
                    userId
                )
                .navigation()
        }
        mBinding.ivChat.setViewClickListener {
            UmUtils.setUmEvent(this@InfoVquPersonalInfoActivity,
                UmUtils.INITIATEPRIVATECHAT)
            if (UserManager.userInfo?.gender == 1) {
                isChat = true
                mViewModel.vquIsAuth()
            } else {
                vquChat()
            }

        }

        if (userId.toString() == UserManager.userInfo?.userId) {//是自己
            mBinding.llBottom.gone()
            mBinding.tvEdit.visible()
        } else {
            mBinding.llBottom.visible()
            mBinding.tvEdit.gone()
        }


    }


    private fun clickImage(images: MutableList<Album>, innerCount: Int) {
        val list: MutableList<UserViewInfo> =
            ArrayList()
        images.map {
            val bounds = Rect()
            mBinding.banner.getGlobalVisibleRect(bounds)
            val userViewInfo =
                UserViewInfo(
                    NetBaseUrlConstant.IMAGE_URL + it.url)
            userViewInfo.bounds = bounds
            list.add(userViewInfo)
        }
        if (list.isNullOrEmpty()) {
            return
        }
        GPreviewBuilder.from(this@InfoVquPersonalInfoActivity)
            .setData<UserViewInfo>(list)
            .setCurrentIndex(innerCount)
            .setType(GPreviewBuilder.IndicatorType.Number)
            .start()

    }

    private fun clickImageAndVideo(images: MutableList<Album>, innerCount: Int) {
        val list: MutableList<UserViewInfo> =
            ArrayList()
        images.map {
            val bounds = Rect()
            mBinding.banner.getGlobalVisibleRect(bounds)
            val userViewInfo = if (it.isVideo == 1) {
                UserViewInfo(
                    NetBaseUrlConstant.IMAGE_URL + it.url,
                    NetBaseUrlConstant.IMAGE_URL + it.url)
            } else {
                UserViewInfo(
                    NetBaseUrlConstant.IMAGE_URL + it.url)
            }
            userViewInfo.bounds = bounds
            list.add(userViewInfo)
        }

        GPreviewVideoImgBuilder.from(this@InfoVquPersonalInfoActivity)
            .setData<UserViewInfo>(list)
            .setCurrentIndex(innerCount)
            .setType(GPreviewVideoImgBuilder.IndicatorType.Number)
            .start()

    }

    private fun vquChat() {
        if (isFromChat) {
            finish()
        } else {
            NimUIKit.startP2PSessionFromInfo(this@InfoVquPersonalInfoActivity,
                userId.toString())
        }
    }

    private fun vquCall() {
        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        var callDialog = CommonVquCallDialog()
        val args = Bundle()
        args.putString("anchor_id", userId.toString())
        callDialog.arguments = args
        callDialog.setNoMoneyListener {
            mPayViewModel.showRechargeDialog(supportFragmentManager)
        }
        callDialog.show(supportFragmentManager, "call")
//        mViewModel.vquGetCallInfo(userId)
    }

    private fun showMenu() {
        mSelectiveDialog =
            BottomSelectiveDialog(this@InfoVquPersonalInfoActivity,
                R.style.SelectiveDialog)
        if (userId.toString() == UserManager.userInfo?.userId) {//是自己显示  编辑资料
            mSelectiveDialog?.addSelectButton("编辑资料",
                OnButtonSelectListener { view, index ->
                    ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
                })
        } else {
            mSelectiveDialog?.addSelectButton("修改备注名",
                OnButtonSelectListener { view, index ->
                   SetReMarkNameDialog()
                        .setTitle("设置备注名")
                        .setContent(
                            if (remarkName.isNullOrEmpty()) {
                                nickName
                            } else {
                                remarkName
                            }
                        )
                        .setOnClickListener(object :
                           SetReMarkNameDialog.OnClickListener {
                            override fun onCancel() {}
                            override fun onConfirm(content: String) {
                                remarkName = content
                                if (remarkName?.isNullOrEmpty()) {
                                    mViewModel.vquSaveRemarkName(userId.toString(), "")
                                } else {
                                    mViewModel.vquSaveRemarkName(userId.toString(), content)
                                }
                            }
                        }).show(supportFragmentManager)
                })
            mSelectiveDialog?.addSelectButton("举报",
                OnButtonSelectListener { view, index ->
                    ARouter.getInstance()
                        .build(RouteUrl.Dynamic.DynamicVquReportActivity)
                        .withInt(
                            RouteKey.USERID,
                            userId
                        )
                        .withInt(RouteKey.TYPE, 1)
                        .navigation()
                })

            mSelectiveDialog?.addSelectButton(if (black == 1) {
                "取消拉黑"
            } else {
                "拉黑"
            },
                OnButtonSelectListener { view, index ->
                    if (black == 1) {
                        mViewModel.vquBlack(userId)
                    } else {
                        CommonHintDialog()
                            .setContentSize(15)
                            .setContentGravity(Gravity.CENTER)
                            .setTitle("提示")
                            .setContent("拉黑后，你将不再收到对方的消息，并且你们互相看不到对方的动态更新。可以在“设置-黑名单”中解除。")
                            .setLeftText("取消")
                            .setRightText("确定")
                            .setOnClickListener(object :
                                CommonHintDialog.OnClickListener {
                                override fun onLeft(dialogFragment: DialogFragment) {
                                    dialogFragment.dismissAllowingStateLoss()
                                }

                                override fun onRight(dialogFragment: DialogFragment) {
                                    mViewModel.vquBlack(userId)
                                    dialogFragment.dismissAllowingStateLoss()
                                }
                            })
                            .show(supportFragmentManager)
                    }


                })
        }
        mSelectiveDialog?.show()

    }

    override fun initObserve() {
        mViewModel.vquUserInfoData.observe(this, Observer {
            when (it.code) {
                0 -> {
                    vquInitInfo(it.data)
                    mBinding.clBlocked.gone()
                }
                3001 -> {
                    mBinding.clBlocked.visible()
                    mBinding.clLoad.gone()
                }
                else -> {
                    it.message.toast()
                    mBinding.clBlocked.gone()
                }
            }

        })
        mViewModel.vquFollowData.observe(this, Observer {
            if ("add" == it.data.action) {//关注
                "关注成功".toast()
                mBinding.llAttention.solidColor = Color.parseColor("#CCCCCC")
                mBinding.tvAttention.text = "已关"
            } else {
                "已取消关注".toast()
                mBinding.llAttention.solidColor = Color.parseColor("#FF7AC2")
                mBinding.tvAttention.text = "关注"
            }
        })
        mViewModel.vquBlackData.observe(this, Observer {
            if ("add" == it.data.action) {//拉黑
                "成功加入黑名单".toast()
                black = 1
            } else {
                "取消拉黑".toast()
                black = 0
            }
        })
        mViewModel.vquSendData.observe(this, Observer {
            if (it.code == 0) {
                mBinding.ivChat.gone()
                mBinding.tvHeartText.text = "私聊"
                mBinding.ivHeart.setImageResource(R.mipmap.ic_tanta_info_chat_white)
                mBinding.llHeart.setViewClickListener {
                    UmUtils.setUmEvent(this@InfoVquPersonalInfoActivity,
                        UmUtils.INITIATEPRIVATECHAT)
                    if (UserManager.userInfo?.gender == 1) {
                        isChat = true
                        mViewModel.vquIsAuth()
                    } else {
                        vquChat()
                    }

                }
            } else if (it.code == 1002) {
                "余额不足，请先充值".toast()
                mPayViewModel.showRechargeDialog(supportFragmentManager)
//                CommonRechargeDialog().show(supportFragmentManager, "充值")
            } else {
                it.message?.toast()
            }

        })
        mViewModel.vquCallData.observe(this, Observer {
            when (it.code) {
                0 -> {//成功
                    ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                        .withString(RouteKey.SOCKET_URL, it?.data?.socket_url ?: "")
                        .withBoolean("isCaller", true)
                        .withParcelable("CallBean", it.data)
                        .navigation()
                }
                1003, 1002 -> {
                    CommonHintDialog()
                        .setTitle("提示")
                        .setContent("您的金币数量不足，请先充值")
                        .setLeftText("舍不得")
                        .setRightText("马上充值")
                        .setContentSize(15)
                        .setContentGravity(Gravity.CENTER)
                        .setOnClickListener(object :
                            CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {}
                            override fun onRight(dialogFragment: DialogFragment) {
                                ARouter.getInstance().build(RouteUrl.Bill.BillTantaRechargeActivity)
                                    .navigation();
                            }
                        })
                        .show(supportFragmentManager)
                }
                else -> {
                    it.message.toast()
                }

            }


        })
        mViewModel.vquAuthData.observe(this, Observer {
            if (it.data.isRpAuth == 1) {
                if (isChat) {
                    vquChat()
                } else {
                    mViewModel.vquSendBeckon("[$userId]")
                }

            } else {
              CommonHintDialog()
                    .setTitle("真人认证")
                    .setContent(resources.getString(R.string.common_vqu_auth))
                    .setLeftText("暂不认证")
                    .setRightText("去认证")
                    .setContentSize(15)
                    .setContentGravity(Gravity.CENTER)
                    .setOnClickListener(object :
                        CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {}
                        override fun onRight(dialogFragment: DialogFragment) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
                                .navigation()
//                            if (UserManager.userInfo?.gender == 1) {
//                                //女认证
//                                ARouter.getInstance()
//                                    .build(RouteUrl.Auth.AuthVquFaceIdentifyActivity).navigation()
//                            } else {
//                                //男认证
//                                ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity)
//                                    .navigation()
//                            }
                        }
                    })
                    .show(supportFragmentManager)
            }
        })
        mViewModel.vquSaveRemarkNameData.observe(this, Observer {
            if (remarkName.isNullOrEmpty()) {
                mBinding.tvRemark.gone()
            } else {
                mBinding.tvRemark.visible()
                mBinding.tvRemark.text = "备注:$remarkName"
            }
        })
    }

    private fun vquInitInfo(info: InfoVquInfoBean) {
        if (info.gender == UserManager.userInfo?.gender) {//同性别隐藏
            mBinding.llBottom.gone()
        } else {
            mBinding.llBottom.visible()
        }
        nickName = info.nickname
        if (info.userRemark.isNullOrEmpty()) {
            mBinding.tvRemark.gone()
        } else {
            mBinding.tvRemark.visible()
            mBinding.tvRemark.text = "备注:" + info.userRemark
            remarkName = info.userRemark
        }
        if (!isInit) {
            vquInitBanner(info.albums)
            isInit = true
        }
        mBinding.tvName.text = info.nickname
        if (info.vip > 0) {
            mBinding.ivVip.visibility = View.VISIBLE
            mBinding.tvName.setTextColor(resources.getColor(R.color.color_934800))
        } else {
            mBinding.ivVip.visibility = View.GONE
            mBinding.tvName.setTextColor(resources.getColor(R.color.color_273145))
        }

        black = info.isBlack
        CommonVquAddRankAndGradeViewHelper.addGender(
            this@InfoVquPersonalInfoActivity,
            mBinding.clName,
            info.gender,
            info.age.toString()
        )
        if (info.isRpAuth == 1) {
            mBinding.ivAuth.visible()
        } else {
            mBinding.ivAuth.gone()
        }
        if (info.isAuth == 1) {
            mBinding.ivReal.visible()
        } else {
            mBinding.ivReal.gone()
        }
        if (info.online.newStatus == 1) {
            mBinding.llState.visible()
        } else {
            mBinding.llState.gone()
        }
        //等级
//        CommonVquAddRankAndGradeViewHelper.addGradeView(
//            this@InfoVquPersonalInfoActivity,
//            mBinding.clName, info.gr.grade.imgInfo
//        )
        //关注
        if (userId.toString() == UserManager.userInfo?.userId) {//是自己  隐藏
            mBinding.llAttention.gone()
        } else {
            mBinding.llAttention.visible()
            if (info.isFollow == 1) {
                mBinding.llAttention.solidColor = Color.parseColor("#CCCCCC")
                mBinding.tvAttention.text = "已关"
            } else {
                mBinding.llAttention.solidColor = Color.parseColor("#FF7AC2")
                mBinding.tvAttention.text = "关注"
            }
            mBinding.llAttention.setViewClickListener { vquLike() }
        }
        //------------动态--------------
        if (info.dynamic.isNullOrEmpty()) {//隐藏动态
            mBinding.clDynamic.gone()
        } else {
            mBinding.clDynamic.visible()
            mBinding.tvCount.text = "(" + info.dynamicNum.toString() + ")"
            var dynamicAdapter = InfoVquSmallDynamicAdapter(info.dynamic)
            mBinding.rvDynamic.adapter = dynamicAdapter
            dynamicAdapter.setNbOnItemClickListener { adapter, view, position ->
                ARouter.getInstance()
                    .build(RouteUrl.Dynamic.DynamicVquDynamicListActivity)
                    .withInt(
                        RouteKey.USERID,
                        userId
                    )
                    .navigation()
            }
        }
        //-------------声音--------------
        if (info.voice != null && !info.voice.voice.isNullOrEmpty() && info.voice.voiceStatus == 1) {
            mBinding.llVoice.visible()
            voiceUrl = info.voice.voice
            voiceTime = info.voice.voiceTime
            mBinding.tvVoiceDuration.text = info.voice.voiceTime.toString() + "\""
            mBinding.llVoice.setViewClickListener {
                if (UserManager.isVideo) {
                    toast("正在语音/视频通话中，请稍后再试...")
                    return@setViewClickListener
                }
                startVoice()
            }
        } else {
            mBinding.llVoice.gone()
        }
        mBinding.tvSignContent.text = info.sign
        initVquInfoList(info.basicInfo)
        if (info.label.isNullOrEmpty()) {//隐藏标签
            mBinding.clLabel.gone()
        } else {
            mBinding.clLabel.visible()
            initVquTag(info.label)
        }
        if (info.gifts.isNullOrEmpty()) {
            mBinding.clGift.gone()
        } else {
            mBinding.clGift.visible()
            mBinding.clGift.setViewClickListener {
//                ARouter.getInstance()
//                    .build(RouteUrl.Info.InfoVquGiftListActivity)
//                    .withInt(
//                        RouteKey.USERID,
//                        userId
//                    )
//                    .navigation()
            }
            initVquGift(info.gifts)
        }

        if (info.isBeckon) {
            mBinding.ivChat.gone()
            mBinding.tvHeartText.text = "私聊"
            mBinding.ivHeart.setImageResource(R.mipmap.ic_tanta_info_chat_white)
            mBinding.llHeart.setViewClickListener {
                UmUtils.setUmEvent(this@InfoVquPersonalInfoActivity,
                    UmUtils.INITIATEPRIVATECHAT)
                if (UserManager.userInfo?.gender == 1) {
                    isChat = true
                    mViewModel.vquIsAuth()
                } else {
                    vquChat()
                }

            }
        } else {
            if (info.gender == 1) {
                mBinding.ivChat.visible()
            } else {
                mBinding.ivChat.gone()
            }
            if (UserManager.userInfo?.gender == 1) {
                mBinding.tvHeartText.text = "心动"
            } else {
                mBinding.tvHeartText.text = "搭讪"
            }
            mBinding.ivHeart.setImageResource(R.mipmap.ic_tanta_info_heart)
            mBinding.llHeart.setViewClickListener {
                UmUtils.setUmEvent(this@InfoVquPersonalInfoActivity,
                    UmUtils.CLICKTOCHAT)
                if (UserManager.userInfo?.gender == 1) {
                    isChat = false
                    mViewModel.vquIsAuth()
                } else {
                    mViewModel.vquSendBeckon("[$userId]")
                }
            }

        }
        mBinding.clLoad.gone()
    }

    private fun initVquGift(gifts: MutableList<GiftBean>) {
        if (giftAdapter == null) {
            giftAdapter = InfoVquGiftAdapter(gifts)
            mBinding.rvGift.adapter = giftAdapter
        } else {
            giftAdapter?.setList(gifts)
        }


    }

    private fun initVquTag(label: ArrayList<Tag>) {
        var labelAdapter = object : TagAdapter<Tag>(label) {
            override fun getView(parent: FlowLayout, position: Int, bean: Tag): View {
                val view = layoutInflater.inflate(
                    R.layout.info_tanta_item_label,
                    mBinding.flLabel, false
                ) as ConstraintLayout
                var tv =
                    view.findViewById<ShapeTextView>(R.id.tv_tag)
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
        mBinding.flLabel.adapter = labelAdapter

    }

    private fun initVquInfoList(basicInfo: MutableList<BasicInfo>) {
        var mAdapter = InfoVquInfoAdapter(basicInfo)
        mBinding.rvInfo.adapter = mAdapter
    }

    private fun vquLike() {
        mViewModel.vquFollow(userId)
    }

    private val images: MutableList<String> = mutableListOf()
    var bannerImageViewAdapter: BannerImageViewAdapter? = null
    private fun vquInitBanner(albums: ArrayList<Album>) {
        if (albums == null) {
            return
        }
        if (albums.size == 0) {
            return
        }
        var smallAdapter = InfoVquSmallImgAdapter(albums)
        mBinding.rvSmall.adapter = smallAdapter
        bannerImageViewAdapter = BannerImageViewAdapter(albums);
        mBinding.banner!!.addBannerLifecycleObserver(this)
            .setOrientation(Banner.HORIZONTAL)
            //  .setIndicator(null, false)
            .isAutoLoop(false)
//            .setAdapter(bannerImageViewAdapter)//暂时去掉视频
            .setAdapter(object : BannerImageAdapter<Album>(albums) {

                override fun onBindView(
                    holder: BannerImageHolder?,
                    data: Album?,
                    position: Int,
                    size: Int,
                ) {
                    holder?.imageView?.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + data?.url,
                        R.mipmap.bg_detail_default)
                }

            })
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                    smallAdapter.setSelect(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

            })
            .setOnBannerListener { data, position ->
//                var intent = Intent(this@InfoVquPersonalInfoActivity,
//                    InfoVquPreviewPicAndVideoActivity::class.java)
//                intent.putExtra("list", albums)
//                intent.putExtra("position", position)
//                startActivity(intent)
//                clickImageAndVideo(albums,position)
                clickImage(albums, position)
            }
        smallAdapter.setNbOnItemClickListener { adapter, view, position ->
            mBinding.banner.currentItem = position + 1
        }
    }

    private fun startVoice() {
        if (playType == 1) { //正在播放中
            playType = 0
            stop()
            mBinding.tvVoiceDuration.setText("$voiceTime\"")
            mBinding.ivPlay.setImageResource(R.mipmap.ic_tanta_info_stop)
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
                        mBinding.ivPlay.setImageResource(R.mipmap.ic_tanta_info_stop)
                        mBinding.tvVoiceDuration.text = "$voiceTime\""
                        mBinding.lvWave.pauseAnimation()
                    }
                }
            countDownTimer?.start()
            play(voiceUrl)
            mBinding.ivPlay.setImageResource(R.mipmap.ic_info_tanta_playing)
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

    override fun onResume() {
        super.onResume()
        mViewModel.vquGetUserInfo(userId)
        bannerImageViewAdapter?.getPlayer()?.startPlayLogic()

    }

    override fun initRequestData() {

    }

    override fun onPause() {
        super.onPause()
        stop()
        bannerImageViewAdapter?.getPlayer()?.onVideoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bannerImageViewAdapter?.getPlayer()?.release()
        stop()
    }


}