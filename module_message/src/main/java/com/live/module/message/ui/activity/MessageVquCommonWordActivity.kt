package com.live.module.message.ui.activity

import android.annotation.SuppressLint
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.message.R
import com.live.module.message.adapter.MessageVquCommonWordAdapter
import com.live.module.message.databinding.MessageTantaActivityCommonWordBinding
import com.live.module.message.dialog.MessageVquBottomDeleteDialog
import com.live.module.message.vm.MessageVquCommonWordViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.InputCountDialog
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean
import com.mshy.VInterestSpeed.uikit.event.NotifyCommonWordEvent
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


/**
 * FileName: com.live.module.message.ui.activity
 * Date: 2022/5/5 16:35
 * Description:
 * History:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquCommonWordActivity)
class MessageVquCommonWordActivity :
    BaseActivity<MessageTantaActivityCommonWordBinding, MessageVquCommonWordViewModel>() {

    override val mViewModel: MessageVquCommonWordViewModel by viewModels()

    private val mData: ArrayList<NIMCommonWordBean> = arrayListOf()

    private lateinit var mAdapter: MessageVquCommonWordAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun initObserve() {
        mViewModel.commonWordList.observe(this) {
            mData.clear()
            mData.addAll(it.data)
            mAdapter.notifyDataSetChanged()
            EventBus.getDefault().post(
                NotifyCommonWordEvent(
                    mData
                )
            )
        }

        mAdapter.setOnItemLongClickListener { _, _, position ->
            val nimCommonWordBean =
                mAdapter.getItemOrNull(position) ?: return@setOnItemLongClickListener true

            if (nimCommonWordBean.is_system == 1) {
                return@setOnItemLongClickListener true
            }

            val dialog = MessageVquBottomDeleteDialog()
            dialog.setOnDeleteClickListener {
                mViewModel.vquDelCommonWord(nimCommonWordBean.id)
            }

            dialog.show(supportFragmentManager, "")

            true
        }
    }

    override fun initRequestData() {
        mViewModel.vquGetCommonWordData()
    }

    override fun MessageTantaActivityCommonWordBinding.initView() {
//        includeTitle.toolbar.initClose(getString(R.string.message_vqu_common_word_title),rightStr = "添加") {
//            finish()
//        }

        includeTitle.toolbar.initClose(
            getString(R.string.message_vqu_common_word_title),
            rightStr = "添加",
            rightColor = R.color.color_FF7AC2,
            onBack = {
                finish()
            },
            onClickRight = {
                val dialog = InputCountDialog()
                dialog.setHint("自定义常用语")
                dialog.setTitle("自定义常用语")
                dialog.setContentMaxLength(50)
                dialog.setOnButtonClickListener(object : InputCountDialog.OnButtonClickListener {
                    override fun onLeftClick(content: String?): Boolean {

                        return false
                    }

                    override fun onRightClick(content: String?): Boolean {

                        if (content.isNullOrEmpty()) {
                            toast("请输入自定义常用语")
                            return true
                        }

                        mViewModel.vquAddCommonWord(content)

                        return false
                    }

                })
                dialog.show(supportFragmentManager, "")
            })
        includeTitle.tvRight.visibility = View.GONE
        tvReplace.setViewClickListener {
            mViewModel.vquGetCommonWordData()
        }

        initSystemList()

    }

    private fun initSystemList() {
        mAdapter = MessageVquCommonWordAdapter(mData)
        mBinding.rvList.isNestedScrollingEnabled = true
        mBinding.rvList.layoutManager = LinearLayoutManager(this@MessageVquCommonWordActivity)
        mBinding.rvList.adapter = mAdapter
        mAdapter.setOnItemClickListener { _, _, position ->
            val bean = mData[position]
            intent.putExtra(SpKey.KEY_COMMON_WORD, bean.word)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}