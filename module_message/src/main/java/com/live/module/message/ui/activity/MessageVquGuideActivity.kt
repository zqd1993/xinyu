package com.live.module.message.ui.activity

import android.os.CountDownTimer
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.message.adapter.MessageVquGuideAdapter
import com.live.module.message.bean.GuideBean
import com.live.module.message.bean.GuideData
import com.live.module.message.databinding.MessageTantaActivityGuideBinding
import com.live.module.message.vm.MessageVquGuideViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.invisible
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


/**
 * FileName: com.live.module.message.ui.activity
 * Date: 2022/5/7 15:24
 * Description:
 * History:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquGuideActivity)
class MessageVquGuideActivity :
    BaseActivity<MessageTantaActivityGuideBinding, MessageVquGuideViewModel>() {
    private var currentAddPos = 0
    private var isGuideEnd = false

    private val mData: List<GuideBean> = ArrayList()

    private lateinit var mAdapter: MessageVquGuideAdapter

    companion object {
        const val STEP_TIME: Long = 500
    }

    override val mViewModel: MessageVquGuideViewModel by viewModels()

    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun MessageTantaActivityGuideBinding.initView() {
        if (GuideData.getInstance().allData.isEmpty()) {
            GuideData.getInstance().initData()
        }
        includeTitle.toolbar.initClose("官方客服") {}
        mAdapter = MessageVquGuideAdapter(mData)
        rvList.layoutManager = LinearLayoutManager(this@MessageVquGuideActivity)
        OverScrollDecoratorHelper.setUpOverScroll(
            rvList,
            OverScrollDecoratorHelper.ORIENTATION_VERTICAL
        )
        rvList.adapter = mAdapter

        tvSend.setViewClickListener {
            addData()
            if (mAdapter.data.size < GuideData.getInstance().allData.size) {
                startTimer()
            }
        }
        tvBack.setViewClickListener {
            SpUtils.putBoolean(SpKey.msg_guide_is_showed, true)
            finish()
        }
    }

    private fun showGuideEndLayout() {
        mBinding.tvSend.gone()
        mBinding.tvContent.gone()
        mBinding.ivGuide1.invisible()
        mBinding.ivGuide2.visible()
        mBinding.ivGuideFinger.visible()
        mBinding.tvBack.visible()
    }

    private fun startTimer() {
        mBinding.tvSend.isEnabled = false
        object : CountDownTimer(STEP_TIME * 3, STEP_TIME) {
            override fun onTick(p0: Long) {
                addData()
            }

            override fun onFinish() {
                mBinding.tvSend.isEnabled = true
            }
        }.start()
    }

    override fun onResume() {
        super.onResume()
        if (currentAddPos == 0) {
            startTimer()
        }
    }


    /**
     * 添加数据
     */
    private fun addData() {
        if (currentAddPos == 3) {
            for (i in mAdapter.data.indices) {
                val bean = mAdapter.data[i]
                bean.coinHint = "+0.65元"
                mAdapter.setData(i, bean)
            }
        }
        mAdapter.addData(GuideData.getInstance().allData[currentAddPos])
        mBinding.rvList.scrollToPosition(mAdapter.itemCount - 1)
        currentAddPos++
        if (mAdapter.data.size == GuideData.getInstance().allData.size) {
            showGuideEndLayout()
            mBinding.tvSend.isEnabled = false
            isGuideEnd = true
        }
    }

    override fun onBackPressed() {
        if (isGuideEnd) {
            SpUtils.putBoolean(SpKey.msg_guide_is_showed, true)
            super.onBackPressed()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (isGuideEnd) {
                SpUtils.putBoolean(SpKey.msg_guide_is_showed, true)
                super.onKeyDown(keyCode, event)
            } else {
                false
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}