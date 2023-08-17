package com.live.module.anchor.activity

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.Gravity
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.anchor.R
import com.live.module.anchor.adapter.AnchorVquHelloSettingAdapter
import com.live.module.anchor.bean.AnchorVquHelloTemplateBean
import com.live.module.anchor.databinding.AnchorVquActivityHelloSettingBinding
import com.live.module.anchor.vm.AnchorVquHelloSettingViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.InputDialog
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.view.decoration.VerticalItemDecoration
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/25
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Anchor.AnchorVquHelloSettingActivity)
open class AnchorVquHelloSettingActivity :
    BaseActivity<AnchorVquActivityHelloSettingBinding, AnchorVquHelloSettingViewModel>() {
    override val mViewModel: AnchorVquHelloSettingViewModel by viewModels()

    private val mAdapter = AnchorVquHelloSettingAdapter()

    private var mOldVoicePosition = -1

    private var mVquMediaPlayer: MediaPlayer? = null

    private val NEW_TEMPLATE_REQUEST_CODE = 1030


    override fun AnchorVquActivityHelloSettingBinding.initView() {
        setLoadSir(mBinding.srlAnchorVquHelloSettingRefresh)

        mBinding.tbAnchorVquHelloSettingBar.toolbar.initClose(getString(R.string.contact_vqu_setting_hello)) {
            finish()
        }

        initRecycleView()

        initEvent()
    }

    private fun initEvent() {
        mAdapter.setOnItemChildClickListener { _, view, position ->
            val item =
                mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.iv_hello_template_edit -> clickItemEdit(item, position)
                R.id.iv_hello_template_delete -> clickItemDelete(item, position)
                R.id.cb_hello_template_use -> if (item.isDefault != 1) {
                    clickItemUse(item, position)
                }
                R.id.fl_hello_template_voice_parent -> clickItemVoice(item, position)
                R.id.fl_hello_template_video_parent -> clickItemVideo(item)
                R.id.riv_hello_template_img -> clickItemImg(item)
            }
        }


        mBinding.srlAnchorVquHelloSettingRefresh.setOnRefreshListener {
            mViewModel.vquListNew()
        }

        mBinding.stvAnchorVquHelloSettingNewTemplate.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Anchor.AnchorVquNewHelloTemplateActivity)
                .navigation(this, NEW_TEMPLATE_REQUEST_CODE)
        }

    }

    private fun clickItemImg(item: AnchorVquHelloTemplateBean) {
        val imageUrls = ArrayList<String>()
        imageUrls.add(NetBaseUrlConstant.IMAGE_URL + item.file)
        ARouter.getInstance()
            .build(RouteUrl.Info.InfoVquPreviewPictureActivity)
            .withInt("position", 0)
            .withStringArrayList("urls", imageUrls)
            .navigation()
    }

    private fun clickItemVideo(item: AnchorVquHelloTemplateBean) {
        ARouter.getInstance()
            .build(RouteUrl.Common.CommonVquVideoActivity)
            .withString("video_url", NetBaseUrlConstant.IMAGE_URL + item.videoFile)
            .navigation()
    }

    private fun clickItemVoice(item: AnchorVquHelloTemplateBean, position: Int) {
        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        if (mOldVoicePosition >= 0) {
            stop(mOldVoicePosition)
            if (mOldVoicePosition == position) {
                mOldVoicePosition = -1
                return
            }
        }

        mOldVoicePosition = position
        try {
            if (mVquMediaPlayer == null) {
                mVquMediaPlayer = MediaPlayer()
            }

            mVquMediaPlayer?.reset()

            // 设置指定的流媒体地址
            mVquMediaPlayer?.setDataSource(NetBaseUrlConstant.IMAGE_URL + item.voiceFile)
            // 设置音频流的类型
//            mVquMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mVquMediaPlayer?.setAudioAttributes(
                AudioAttributes.Builder()
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                    .build()
            )
            mVquMediaPlayer?.prepareAsync()

            // 设置循环播放
            // mediaPlayer.setLooping(true);
            mVquMediaPlayer?.setOnCompletionListener {
                // 在播放完毕被回调
                stop(position)
            }

            mVquMediaPlayer?.setOnErrorListener { _, _, _ -> // 如果发生错误，重新播放
                toast("网络异常，播放失败")
                false
            }

            mVquMediaPlayer?.setOnPreparedListener {
                item.playType = 1
                mVquMediaPlayer?.start()

                val viewHolder: RecyclerView.ViewHolder? =
                    mBinding.rvAnchorVquHelloSettingList.findViewHolderForAdapterPosition(position)
                if (viewHolder is BaseViewHolder) {
                    mAdapter.startDownTime(viewHolder, position)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("网络异常，播放失败")
        }
    }


    private fun stop(position: Int) {
        mOldVoicePosition = -1
        try {
            if (mVquMediaPlayer != null) {

                if (mVquMediaPlayer?.isPlaying == true) {
                    mVquMediaPlayer?.stop()
                    mVquMediaPlayer?.release()
                }
                mVquMediaPlayer = null

                val viewHolder: RecyclerView.ViewHolder? =
                    mBinding.rvAnchorVquHelloSettingList.findViewHolderForAdapterPosition(position)
                if (viewHolder is BaseViewHolder) {
                    mAdapter.stopDownTime(viewHolder, position)
                }
                mAdapter.data[position].playType = 0
                mAdapter.notifyItemChanged(position)

            } else {
                val viewHolder: RecyclerView.ViewHolder? =
                    mBinding.rvAnchorVquHelloSettingList.findViewHolderForAdapterPosition(position)
                if (viewHolder is BaseViewHolder) {
                    mAdapter.stopDownTime(viewHolder, position)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun clickItemUse(item: AnchorVquHelloTemplateBean, position: Int) {
        itemUse(item, position)
    }

    private fun itemUse(item: AnchorVquHelloTemplateBean, position: Int) {
        mViewModel.vquSetDefault(item, position)
    }

    private fun clickItemDelete(item: AnchorVquHelloTemplateBean, position: Int) {
        val dialog = MessageDialog()
        dialog.setTitle("确定删除当前模板？")
        dialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {
                itemDelete(item, position)
                return false
            }

        })
        dialog.show(supportFragmentManager, "")
    }

    private fun itemDelete(item: AnchorVquHelloTemplateBean, position: Int) {
        mViewModel.vquDeleteNew(item, position)
    }

    private fun clickItemEdit(item: AnchorVquHelloTemplateBean, position: Int) {
        val dialog = InputDialog()
        dialog.setTitle(R.string.anchor_vqu_template_mark_title)
        val content: String = if (item.name.isNullOrEmpty()) {
            "我的模板"
        } else {
            item.name ?: ""
        }
        dialog.setContent(content)
        dialog.setHint(R.string.anchor_vqu_template_mark_tips)
        dialog.setContentMaxLength(15)
        dialog.setHintColor(ResUtils.getColor(R.color.color_A3AABE))
        dialog.setContentGravity(Gravity.CENTER)
        dialog.setOnButtonClickListener(object : InputDialog.OnButtonClickListener {

            override fun onLeftClick(content: String?): Boolean {

                return false
            }

            override fun onRightClick(content: String?): Boolean {
                if (content.isNullOrEmpty()) {
                    toast(R.string.anchor_vqu_template_mark_tips)
                    return true
                }
                itemSetName(item, content, position)
                return false
            }

        })
        dialog.show(supportFragmentManager, "")
    }

    private fun itemSetName(item: AnchorVquHelloTemplateBean, content: String, position: Int) {
        mViewModel.vquSetName(item, content, position)
    }

    private fun initRecycleView() {

        mBinding.rvAnchorVquHelloSettingList.addItemDecoration(
            VerticalItemDecoration(
                this@AnchorVquHelloSettingActivity,
                8f, 8f, 123f
            )
        )
        val itemAnimator = mBinding.rvAnchorVquHelloSettingList.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
        mAdapter.setHasStableIds(true)

        mBinding.rvAnchorVquHelloSettingList.adapter = mAdapter
    }

    override fun initObserve() {
        mViewModel.helloListData.observe(this) {

            if (it.list.isNullOrEmpty()) {
                showEmpty()
            } else {
                mLoadService?.showSuccess()
            }

            mBinding.srlAnchorVquHelloSettingRefresh.finishRefresh()
            mAdapter.setNewInstance(it.list)
        }

        mViewModel.refreshPosition.observe(this) {
            mAdapter.notifyItemChanged(it)
        }

        mViewModel.deletePosition.observe(this) {

            mViewModel.vquListNew()
        }
    }

    override fun initRequestData() {
        mViewModel.vquListNew()
    }

    override fun onPause() {
        super.onPause()
        stop(mOldVoicePosition)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == NEW_TEMPLATE_REQUEST_CODE) {
                mViewModel.vquListNew()
            }
        }
    }
}