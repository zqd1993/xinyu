package com.mshy.VInterestSpeed.common.ui.dialog

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.FirstRechargeGiftBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.databinding.CommonDialogFirstRechargeBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.BaseVMDialogFragment
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel

/**
 * author: Lau
 * date: 2022/6/6
 * description:
 */
class CommonFirstRechargeDialog :
    BaseVMDialogFragment<CommonDialogFirstRechargeBinding, CommonPayViewModel>() {

    private var mVquListener: (() -> Boolean)? = null
    private var mVquShowClose = true

    private var mVquList: MutableList<FirstRechargeGiftBean> = mutableListOf()

    private lateinit var mAdapter: GiftAdapter

    override fun CommonDialogFirstRechargeBinding.initView() {
        mBinding.ivClose.visibility = if (mVquShowClose) View.VISIBLE else View.GONE

//        mBinding.rvCommonFirstRechargeList.addItemDecoration(HorizontalItemDecoration(3, 5))
//        mBinding.rvCommonFirstRechargeList.adapter = mAdapter

        var spanCount = 3
        spanCount = if (mVquList.size > 3) {
            3
        } else {
            mVquList.size
        }

        val gm = GridLayoutManager(requireActivity(), spanCount)

        mBinding.rvCommonFirstRechargeList.layoutManager = gm

        mBinding.rvCommonFirstRechargeList.adapter = mAdapter

        mAdapter.setNewInstance(mVquList)

        mBinding.tvCommonFirstRechargeSubmit.setViewClickListener {
            if (mVquListener?.invoke() != true) {
                val rechargeDialog = CommonRechargeDialog()
                rechargeDialog.show(childFragmentManager, "")
            }
        }


    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    override val mViewModel: CommonPayViewModel by viewModels()

    /**
     * 由于在DialogFragment中弹出DialogFragment，不能Dismiss当前DialogFragment
     * 所以如果需要在弹出后关闭当前DialogFragment的，请重写接听方法，并且返回true
     * 在回调中去调用想要前往的DialogFragment
     * 此处跳转[CommonRechargeDialog]
     */
    fun onVquRechargeClickListener(listener: (() -> Boolean)?) {
        mVquListener = listener
    }

    fun showClose(showClose: Boolean) {
        mVquShowClose = showClose
    }

    /**
     * 设置是否可以关闭
     */
    fun setCancelAble(cancelAble: Boolean): CommonFirstRechargeDialog {
        mCancelAble = cancelAble
        return this
    }

    fun setList(list: MutableList<FirstRechargeGiftBean>?) {
        mVquList.clear()
        if (list != null) {
            mVquList.addAll(list)

            mAdapter = if (mVquList.size >= 3) {
                GiftAdapter(R.layout.common_item_first_recharge_grid)
            } else {
                GiftAdapter(R.layout.common_item_first_recharge_grid_103)
            }

        }
    }


    private inner class GiftAdapter(layoutId: Int) :
        BaseQuickAdapter<FirstRechargeGiftBean, BaseViewHolder>(R.layout.common_item_first_recharge_grid) {
        override fun convert(holder: BaseViewHolder, item: FirstRechargeGiftBean) {
            holder.setText(R.id.tv_item_first_recharge_name, item.name)

            if (item.type == 1) {
                holder.setText(R.id.stv_item_first_recharge_grid_num, item.number + "个")
                holder.setGone(R.id.stv_item_first_recharge_grid_num, false)
            } else {
                holder.setGone(R.id.stv_item_first_recharge_grid_num, true)
            }
            holder.setText(R.id.tv_item_first_recharge_price, item.money + "金币")

            val ivIcon = holder.getView<ImageView>(R.id.iv_item_first_recharge_grid_icon)
            ivIcon.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + item.img)
        }
    }
}