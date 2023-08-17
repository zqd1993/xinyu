package com.mshy.VInterestSpeed.common.ui.view.imagepreview

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister

import com.mshy.VInterestSpeed.common.databinding.InfoVquFragmentPreviewVideoBinding
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.ui.vm.CommonVquMainViewModel
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import org.greenrobot.eventbus.Subscribe

/**
 * author: Tany
 * date: 2022/5/7
 * description:
 */
@EventBusRegister
class InfoVquPreviewVideoFragment :
    BaseFragment<InfoVquFragmentPreviewVideoBinding, CommonVquMainViewModel>() {
    override val mViewModel: CommonVquMainViewModel by viewModels()
    protected var isViewCreated = false
    protected var isFront = false

    override fun initObserve() {

    }

    companion object {
        fun newInstance(album: String, info: com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.IThumbViewInfo?): InfoVquPreviewVideoFragment {
            val args = Bundle()
            args.putString("album", album)
            args.putParcelable("info", info)
            val fragment = InfoVquPreviewVideoFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun initRequestData() {

    }

    override fun InfoVquFragmentPreviewVideoBinding.initView() {
        isViewCreated = true
        var album = arguments?.getString("album")
        var info: com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.IThumbViewInfo? = arguments?.getParcelable("info")
        mBinding.rlDrag?.setTransitionsView(info?.bounds)
        mBinding.rlDrag?.startTransitions()
        mBinding.rlDrag.setOnoDragListener(object : com.mshy.VInterestSpeed.common.ui.view.photoDrag.OnDragListener() {
            override fun onStartDrag() {
                super.onStartDrag()
                video?.onVideoPause()
            }

            override fun onStartEnter(outOfBound: Boolean) {
                super.onStartEnter(outOfBound)
            }

            override fun onRelease(isResume: Boolean) {
                super.onRelease(isResume)
            }

            override fun onEndExit() {
                super.onEndExit()
                if (activity is GPreviewVideoImgActivity) {
                    (activity as GPreviewVideoImgActivity?)!!.transformOut()
                }
            }

            override fun onEndEnter() {
                super.onEndEnter()
            }

            override fun onStartExit(outOfBound: Boolean) {
                super.onStartExit(outOfBound)
            }

            override fun onEndResume() {
                super.onEndResume()
                video?.onVideoResume()
            }
        })
        ivPlay.visible()
        video.visible()
        video?.isLooping = true
        ivPlay.gone()
        video?.setUp(album, true, "")
        video?.startPlayLogic()
        video.setVideoAllCallBack(object : VideoAllCallBack {
            override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onClickResume(url: String?, vararg objects: Any?) {
                ivPlay?.visible()

            }

            override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onStartPrepared(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartIcon(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartThumb(url: String?, vararg objects: Any?) {
                ivPlay?.gone()
            }

            override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
            }

            override fun onClickStartError(url: String?, vararg objects: Any?) {
            }

            override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onPrepared(url: String?, vararg objects: Any?) {
                GSYVideoManager.instance().isNeedMute = UserManager.isVideo
                if (!isFront) {
                    video.onVideoPause()
                }
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
            }

            override fun onComplete(url: String?, vararg objects: Any?) {
            }

            override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
            }

            override fun onClickBlank(url: String?, vararg objects: Any?) {
            }

            override fun onClickStop(url: String?, vararg objects: Any?) {
            }

            override fun onClickSeekbar(url: String?, vararg objects: Any?) {
            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
            }

            override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
            }

            override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
            }


        })
        video?.currentPlayer?.setGSYStateUiListener {
            when (it) {
                GSYVideoView.CURRENT_STATE_PLAYING -> {
                    ivPlay?.gone()
                }
                GSYVideoView.CURRENT_STATE_PAUSE -> {
                    ivPlay?.visible()
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isFront = hidden
        if (hidden) {
            mBinding.video.onVideoPause()
        } else {
            mBinding.video.onVideoResume()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isFront = isVisibleToUser
    }

    @Subscribe
    fun onEventMainThread(event: com.mshy.VInterestSpeed.common.event.VideoPlayEvent?) {
        if (event!!.isPlay) {
            mBinding.video.onVideoResume()
        } else {
            mBinding.video.onVideoPause()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.video.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        mBinding.video.onVideoPause()
    }

    override fun onDestroyView() {
        mBinding.video.release()
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}