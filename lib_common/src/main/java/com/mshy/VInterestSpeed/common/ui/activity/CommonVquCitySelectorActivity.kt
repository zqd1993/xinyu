package com.mshy.VInterestSpeed.common.ui.activity

import android.content.Intent
import android.os.Handler
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.vquonline.base.ktx.clickDelay
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.City
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonVquActivitCitySelectorBinding
import com.mshy.VInterestSpeed.common.ext.initSearch2
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.view.SideIndexBar
import com.mshy.VInterestSpeed.common.ui.view.decoration.SectionItemDecoration
import com.mshy.VInterestSpeed.common.ui.vm.CommonVquCitySelectorViewModel
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.common.utils.TantaCitySelector
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Common.CommonVquCitySelectorActivity)
class CommonVquCitySelectorActivity :
    BaseActivity<CommonVquActivitCitySelectorBinding, CommonVquCitySelectorViewModel>() {
    override val mViewModel: CommonVquCitySelectorViewModel by viewModels()

    private val mAdapter = CityAdapter()

    private var mCityId: String? = null

    private var mCities = mutableListOf<City>()

    override fun CommonVquActivitCitySelectorBinding.initView() {

        mBinding.tvCommonVquCitySelectorLocation.text = getString(
            R.string.common_vqu_current_location,
            getString(R.string.common_vqu_locating)
        )

        mBinding.tbCommonVquCitySelectorBar.toolbar.initSearch2(
            R.string.common_vqu_search_city_or_py,
            onSearch = {
                mViewModel.search(it)
            },
            onBack = {
                finish()
            }
        )

        mBinding.rvCommonVquCitySelectorList.addItemDecoration(
            SectionItemDecoration(
                this@CommonVquCitySelectorActivity,
                mCities
            )
        )
        mBinding.rvCommonVquCitySelectorList.adapter = mAdapter
        mAdapter.setNewInstance(mCities)

        mAdapter.setOnItemClickListener { _, _, position ->
            val city = mAdapter.getItem(position)
            val data = Intent()
            data.putExtra(TantaCitySelector.CITY_RESULT, city)
            setResult(RESULT_OK, data)
            finish()
        }

        mBinding.tvCommonVquCitySelectorRelocate.clickDelay {
            locate()
        }

//        mBinding.sibCommonVquCitySelectorIndex.setOverlayTextView()
        mBinding.sibCommonVquCitySelectorIndex.setOnIndexChangedListener(object :
            SideIndexBar.OnIndexTouchedChangedListener {
            override fun onIndexChanged(index: String?, position: Int) {
                mAdapter.scrollToSection(index)
            }
        })
    }

    override fun initObserve() {
        mViewModel.cities.observe(this) {
            mCities.clear()
            mCities.addAll(it)
            mAdapter.notifyDataSetChanged()
//            mAdapter.setNewInstance(it)
        }

        mViewModel.searchCities.observe(this) {
            mCities.clear()
            mCities.addAll(it)
            mAdapter.notifyDataSetChanged()
        }

        mViewModel.location.observe(this) {
            mCityId = TantaCitySelector.getCityId(it.city ?: "") ?: "110100"

            mBinding.tvCommonVquCitySelectorLocation.text =
                getString(R.string.common_vqu_current_location, it.city)
        }
    }

    override fun initRequestData() {
        mViewModel.getCityData()

        locate()
    }

    private fun locate() {
        PermissionUtils.location(this, requestCallback = { allGranted, _, _ ->
            if (allGranted) {
                mViewModel.locate()
            } else {
                mBinding.tvCommonVquCitySelectorLocation.text =
                    getString(
                        R.string.common_vqu_current_location,
                        getString(R.string.common_vqu_locat_error)
                    )
            }
        })
    }

    private inner class CityAdapter :
        BaseQuickAdapter<City, BaseViewHolder>(R.layout.common_vqu_item_city_text) {
        override fun convert(holder: BaseViewHolder, item: City) {
            holder.setText(R.id.tv_common_vqu_item_city_text, item.name)
        }


        /**
         * 滚动RecyclerView到索引位置
         * @param index
         */
        fun scrollToSection(index: String?) {
            if (data.isNullOrEmpty()) return
            if (index.isNullOrEmpty()) return
            for (i in 0 until data.size) {
                if (index.substring(0, 1) == data[i].getSection()?.substring(0, 1)
                ) {
                    if (recyclerView.layoutManager != null) {
                        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager

                        linearLayoutManager.scrollToPositionWithOffset(i, 0)
                        if (index.substring(0, 1) == "定") {
                            //防止滚动时进行刷新
                            Handler().postDelayed(
                                Runnable { notifyItemChanged(0) },
                                1000
                            )
                        }
                        return
                    }
                }
            }
        }
    }


}