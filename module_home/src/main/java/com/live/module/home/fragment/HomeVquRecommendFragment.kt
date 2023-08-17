package com.live.module.home.fragment


import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.apkfuns.logutils.LogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.live.module.home.adapter.HomeVquRecommendAdapter
import com.live.module.home.databinding.HomeTantaFragmentMainHomeRecommendBinding
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.EventBusUtils
import com.live.vquonline.base.utils.GsonUtil
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.CommonVquAnchorBean
import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.event.HomeVquShowBottomEvent
import com.mshy.VInterestSpeed.common.ext.addOnPreloadListener
import com.mshy.VInterestSpeed.common.ext.setNbOnItemClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.quyunshuo.module.home.fragment.HomeVquFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.tencent.mmkv.MMKV
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/4/11 11:07
 *
 */
@EventBusRegister
@AndroidEntryPoint
class HomeVquRecommendFragment :
    BaseFragment<HomeTantaFragmentMainHomeRecommendBinding, HomeVquFragmentViewModel>(),
    OnRefreshListener, OnLoadMoreListener {

    private var vquType: Int = 0
    private var vquPage: Int = 1
    private var vquRecomendAdapter = HomeVquRecommendAdapter()
    private val mPayViewModel: CommonPayViewModel by viewModels()


    @SuppressLint("Range")
    override fun HomeTantaFragmentMainHomeRecommendBinding.initView() {
        vquType = arguments?.getInt("type")!!
        mBinding.vquSmartRecommend.setOnRefreshListener(this@HomeVquRecommendFragment)
        mBinding.vquSmartRecommend.setOnLoadMoreListener(this@HomeVquRecommendFragment)
        mBinding.vquSmartRecommend.setEnableAutoLoadMore(true)
        mBinding.vquSmartRecommend.setFooterTriggerRate(2.toFloat())

        mBinding.vquRvRecommend.layoutManager = LinearLayoutManager(context)

        if (UserManager.userInfo == null) {
            UserManager.userInfo = UserSpUtils.getBean(SpKey.userInfo, VquUserInfo::class.java)
            vquRecomendAdapter.gender = UserManager.userInfo!!.gender
            //DeviceManager.getInstance().token = UserManager.userInfo?.token
        } else {
            vquRecomendAdapter.gender = UserManager.userInfo!!.gender
        }

        vquRvRecommend.adapter = vquRecomendAdapter
        (mBinding.vquRvRecommend.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        mBinding.vquRvRecommend.addOnPreloadListener(20) {
            vquPage += 1
            mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        }

        vquRecomendAdapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
//                R.id.fl_voice -> {
//                    if (oldItemPosition == (position)) { //  点自己暂停重置
//                        if (vquRecomendAdapter.data[position].playType != 0) {
//                            oldItemPosition = position
//                            stop()
//                            vquRecomendAdapter.data[position].playType = 0 // 重置
//                            vquRecomendAdapter.data[position].isPlaying = false
//                            vquRecomendAdapter.notifyDataSetChanged()
//                            return@setOnItemChildClickListener
//                        }
//                    }
//                    if (oldItemPosition != -1) {//设置上一个播放暂停
//                        vquRecomendAdapter.data[oldItemPosition].playType = 0
//                        vquRecomendAdapter.data[oldItemPosition].isPlaying = false
//                    }
//                    vquRecomendAdapter.data[position].playType = 1 // 播放中
//                    vquRecomendAdapter.notifyDataSetChanged()
//                    stop()
//                    play(vquRecomendAdapter.data[position].mp3)
//                    oldItemPosition = position
//                }
//                R.id.fl_end -> {
//
//                    if (UserManager.userInfo != null) {
//                        if (UserManager.userInfo?.gender == 2 || UserManager.userInfo?.isRpAuth == 1) {
//                            if (!vquRecomendAdapter.data[position].isIs_beckon) {
//                                UmUtils.setUmEvent(activity, UmUtils.CLICKTOCHAT)
//                                vquSetBeckon(
//                                    vquRecomendAdapter.data[position].userid,
//                                    position
//                                )
//                            } else {
//                               UmUtils.setUmEvent(activity, UmUtils.INITIATEPRIVATECHAT)
//                                NimUIKit.startP2PSession(
//                                    activity,
//                                    vquRecomendAdapter.data[position].userid.toString()
//                                )
//                            }
//                        } else {
//                            val messageDialog = MessageDialog()
//                            messageDialog.setTitle(R.string.common_vqu_go_to_real_auth)
//                            messageDialog.setContent(R.string.common_vqu_content_auth)
//                            messageDialog.setLeftText(R.string.common_vqu_go_to_no_auth)
//                            messageDialog.setRightText(R.string.common_vqu_go_to_auth)
//                            messageDialog.setOnButtonClickListener(object :
//                                MessageDialog.OnButtonClickListener {
//                                override fun onLeftClick(): Boolean {
//
//                                    return false
//                                }
//
//                                override fun onRightClick(): Boolean {
//                                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
//                                        .navigation()
//                                    return false
//                                }
//                            })
//                            messageDialog.show(childFragmentManager, "")
//                        }
//                    }
//                }
            }
        }

        vquRecomendAdapter.setNbOnItemClickListener { _, _, position ->
//            ARouter.getInstance()
//                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
//                .withInt(RouteKey.USERID, vquRecomendAdapter.data[position].userid)
//                .navigation()
        }
        setLoadSir(vquSmartRecommend)

        val vquCacheString =
            MMKV.defaultMMKV()?.getString(UserManager.userInfo?.userId ?: "1", "")
        LogUtils.d("userId--》$vquCacheString")
        if (!TextUtils.isEmpty(vquCacheString)) {
            val listType = object : TypeToken<ArrayList<CommonVquAnchorBean>>() {}.type
            val list: ArrayList<CommonVquAnchorBean> = Gson().fromJson(vquCacheString, listType)
//            vquRecomendAdapter.setList(list)
            mBinding.vquSmartRecommend.autoRefresh(1000)
            mBinding.vquSmartRecommend.finishRefresh(3000)
        } else {
            mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        }
    }

    /**
     * 搭讪和心动
     */
    private fun vquSetBeckon(vquUserId: Int, vquPosition: Int) {
//        vquRecomendAdapter.data[vquPosition].isIs_beckon = true
        vquRecomendAdapter.notifyItemChanged(vquPosition)
        mViewModel.vquSendBeckon("[${vquUserId}]", vquPosition,0,0)
    }

    override fun onRetryBtnClick() {
        vquPage = 1
        mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
    }

    companion object {
        fun newInstance(type: Int): HomeVquRecommendFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = HomeVquRecommendFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val mViewModel: HomeVquFragmentViewModel by viewModels()

    override fun initObserve() {

        //更新心动搭讪
        mViewModel.vquBeckonInt.observe(this) {
            if (it > vquRecomendAdapter.data.size - 1) {
                return@observe
            }
//            vquRecomendAdapter.data[it].isIs_beckon = false
            vquRecomendAdapter.notifyItemChanged(it)
        }

        mViewModel.vquNoDiamond.observe(this) {
            "余额不足，请先充值".toast()
            mPayViewModel.showRechargeDialog(childFragmentManager)
        }

        if (vquType == 0) {
            mViewModel.vquGetRecommendAnchors.observe(this) {
                if (it.activity != null) {
                    EventBusUtils.postEvent(HomeVquShowBottomEvent(
                        !it.activity.isIs_info))
                }
                if (vquPage == 1) {
                    if (it.list.list.isEmpty()) {
                        showEmpty("暂无数据")
                    } else {
                        showContent()
                    }
                }

                if (null != it.list) {
                    if (null != it.list.list) {
                        if (it.list.list.isNotEmpty()) {
                            if (vquPage == 1) {
                                vquRecomendAdapter.data.clear()
                            }
                            val mNewData: ArrayList<CommonVquAnchorBean> = it.list.list
//                            mNewData.removeAll(vquRecomendAdapter.data)

//                            if (vquPage == 1) {
//                                vquRecomendAdapter.setList(mNewData)
//                            } else {
//                                vquRecomendAdapter.addData(mNewData)
//                            }

                        }
                    }
                }
                finishLoad()
            }

        }


    }

    override fun initRequestData() {
    }

    private fun finishLoad() {
        mBinding.vquSmartRecommend.finishRefresh()
        mBinding.vquSmartRecommend.finishLoadMore()
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        vquPage = 1
        mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        mBinding.vquSmartRecommend.finishRefresh(3000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        vquPage += 1
        mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        mBinding.vquSmartRecommend.finishLoadMore(3000)
    }

    private var mediaPlayer: MediaPlayer? = null
    private var oldItemPosition = -1
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (!isVisibleToUser) {
            if (oldItemPosition != -1) {
                stop()
//                if (vquRecomendAdapter.data.size >= oldItemPosition) {
//                    vquRecomendAdapter.data[oldItemPosition].playType = 0 // 重置
//                    vquRecomendAdapter.data[oldItemPosition].isPlaying = false
//                    vquRecomendAdapter.notifyItemChanged(oldItemPosition)
//                    oldItemPosition = -1
//                }
            }
        }
        super.setUserVisibleHint(isVisibleToUser)
    }

    private fun play(url: String) {
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
            // mediaPlayer.setLooping(true)
            mediaPlayer?.setOnCompletionListener { // 在播放完毕被回调
                stop()
            }
            mediaPlayer?.setOnErrorListener { _, _, _ -> // 如果发生错误，重新播放
                // ToastUtil.showToast(getActivity(),"网络异常，播放失败")
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("网络异常，播放失败")
        }
    }


    /**
     * 148.148      * 停止播放
     * 149.149
     */
    protected fun stop() {
//        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
//            mediaPlayer!!.stop()
//            mediaPlayer!!.release()
//            mediaPlayer = null
//            vquRecomendAdapter.data[oldItemPosition].playType = 0
//            vquRecomendAdapter.notifyDataSetChanged()
//        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.vquGetUserInfo()
    }

    override fun onPause() {
        super.onPause()
        if (vquRecomendAdapter.data.size > 15) {
            if (UserManager.userInfo?.userId != null) {
                MMKV.defaultMMKV()?.putString(
                    UserManager.userInfo?.userId,
                    GsonUtil.GsonString(vquRecomendAdapter.data.subList(0, 15))
                )
//                LogUtils.d(
//                    "userId-->" + GsonUtil.GsonString(
//                        vquRecomendAdapter.data.subList(
//                            0,
//                            15
//                        )
//                    )
//                )
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: String) {
        if ("homeDoubleClick" == event) {
            mBinding.vquRvRecommend.scrollToPosition(0)
            mBinding.vquSmartRecommend.autoRefresh()
            mBinding.vquSmartRecommend.finishRefresh(3000)
        }
    }
}