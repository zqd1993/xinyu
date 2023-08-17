package com.live.module.message.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.message.R
import com.live.module.message.adapter.MessageVquCallRecordAdapter
import com.live.module.message.bean.CallRecordData
import com.live.module.message.databinding.MessageTantaCallRecordBinding
import com.live.module.message.vm.MessageVquCallRecordViewModel
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * FileName: com.live.module.message.ui.fragment
 * Date: 2022/5/20 14:38
 * Description:通话记录
 * History:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquCallRecordFragment)
class MessageVquCallRecordFragment :
    BaseLazyFrameFragment<MessageTantaCallRecordBinding, MessageVquCallRecordViewModel>() {

    private val mData = mutableListOf<CallRecordData.ListBean>()
    private lateinit var mAdapter: MessageVquCallRecordAdapter

    override val mViewModel: MessageVquCallRecordViewModel by viewModels()

    companion object {
        fun newInstance(): MessageVquCallRecordFragment {
            val args = Bundle()
            val fragment = MessageVquCallRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun MessageTantaCallRecordBinding.initView() {
        mAdapter = MessageVquCallRecordAdapter(mData)
        val empty = layoutInflater.inflate(R.layout.message_tanta_msg_empty, null, false)
        val tvEmpty = empty.findViewById<TextView>(R.id.tv_empty)
        empty.findViewById<ImageView>(R.id.iv_empty).setImageResource(R.mipmap.resources_tanta_call_empty)
        tvEmpty.text = "暂无通话记录"
        mAdapter.setEmptyView(empty)
        rvList.adapter = mAdapter
        srlRefresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.getCallRecords(false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.getCallRecords(true)
            }
        })
        mAdapter.setOnItemClickListener { _, _, position ->
            if (mAdapter.data[position].userid != 0) {
                NimUIKit.startP2PSession(activity, "${mAdapter.data[position].userid}")
            }
        }

        mAdapter.setOnItemLongClickListener { _, _, position ->
            showLongClickMenu(mAdapter.data[position], position)
            true
        }
    }

    override fun initObserve() {
        mViewModel.mCallRecords.observe(this) {
            finishLoad()
            if (mViewModel.mIsLoadMore) {
                mAdapter.addData(it)
            } else {
                mAdapter.setList(it)
            }
        }
        mViewModel.mIsDeletePosition.observe(this) {
            if (it != -1) {
                mAdapter.removeAt(it)
            }
        }
    }


    override fun initRequestData() {
        mViewModel.getCallRecords(false)
    }

    private fun finishLoad() {
        mBinding.srlRefresh.finishRefresh()
        mBinding.srlRefresh.finishLoadMore()
    }

    private fun showLongClickMenu(data: CallRecordData.ListBean, position: Int) {
        context?.let {
            BottomSelectiveDialog(it)
                .isAddLine(true)
                .addSelectButton(resources.getString(R.string.message_vqu_delete_chat)) { _, _ ->
                    deleteRecordDialog(data, position)
                }
                .show()
        }
    }

    private fun deleteRecordDialog(data: CallRecordData.ListBean, position: Int) {
        CommonHintDialog()
            .setTitle("提示")
            .setContent("是否删除通话记录")
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?) {}
                override fun onRight(dialogFragment: DialogFragment?) {
                    deleteRecord(data.id, position)
                }
            }).show(childFragmentManager)
    }

    private fun deleteRecord(id: Int, position: Int) {
        mViewModel.deleteRecord(id, position)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: String) {
        if ("CallRecordRefresh" == event) {
            mViewModel.getCallRecords(false)
        }
    }

}