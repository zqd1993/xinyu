package com.live.module.info.activity

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.ellison.glide.translibrary.ImageUtils
import com.ellison.glide.translibrary.base.LoaderBuilder
import com.gyf.immersionbar.ImmersionBar
import com.live.module.info.R
import com.live.module.info.adapter.InfoVquGiftListAdapter
import com.live.module.info.bean.GiftBean
import com.live.module.info.databinding.InfoTantaActivityGiftBinding
import com.live.module.info.vm.InfoVquViewModel
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UiUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Tany
 * date: 2022/4/27
 * description:礼物墙
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Info.InfoVquGiftListActivity)
class InfoVquGiftListActivity : BaseActivity<InfoTantaActivityGiftBinding, InfoVquViewModel>() {
    @Autowired(name = RouteKey.USERID)
    @JvmField
    var userId = 0

    override val mViewModel: InfoVquViewModel by viewModels()

    override fun InfoTantaActivityGiftBinding.initView() {
        ImmersionBar.with(this@InfoVquGiftListActivity).transparentStatusBar()
            .fitsSystemWindows(false).init()
        mBinding.ivBack.setViewClickListener { finish() }
    }

    override fun initObserve() {
        mViewModel.vquGiftData.observe(this, Observer {
            mBinding.tvHas.text="已点亮"+it.data.userGiftTotal
            mBinding.tvTotal.text="/"+it.data.giftTotal
            vquInitList(it.data.allList)
            val builderConfig = LoaderBuilder()
                .circle(true)
                .width(UiUtils.dip2px(this,73f))
                .height(UiUtils.dip2px(this,73f))
                .borderColor(ContextCompat.getColor(this, R.color.color_FFFFFF))
                .borderWidth(UiUtils.dip2px(this,1f))
                .load(NetBaseUrlConstant.IMAGE_URL + it.data.avatar)
            ImageUtils.getInstance().bind(mBinding.ivHead, builderConfig)
            mBinding.tvName.text=it.data.nickname
        })
    }

    private fun vquInitList(allList: MutableList<GiftBean>) {
        mBinding.rvGift.adapter = InfoVquGiftListAdapter(allList)
    }

    override fun initRequestData() {
        mViewModel.vquGetGiftList(userId)
    }
}