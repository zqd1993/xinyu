package com.live.module.relation.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.relation.R
import com.live.module.relation.adapter.RelationVquAdapter
import com.live.module.relation.databinding.RelationVquFragmentFriendBinding
import com.live.module.relation.vm.RelationViewModel
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/5/16
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Relation.RelationVquFriendFragment)
class RelationVquFriendFragment :
    BaseFragment<RelationVquFragmentFriendBinding, RelationViewModel>() {
    override val mViewModel: RelationViewModel by viewModels()

    private val mAdapter = RelationVquAdapter()


    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0

    override fun initObserve() {
        mViewModel.relationList.observe(this) {
            if (mViewModel.mPage == 1 && it.isNullOrEmpty()) {
                mBinding.srlRelationRefresh.finishRefresh()
                showEmpty("你还没有关注的人哦",R.mipmap.tanta_relation_empty)
            } else {
                val iteratorServerAll: MutableIterator<VquRelationBean> = it.iterator()
                while (iteratorServerAll.hasNext()) {
                    val vquRelationBean = iteratorServerAll.next() as VquRelationBean
                    vquRelationBean.itemType = RouteKey.RelationType.TRACK
                }

                mLoadService?.showSuccess()
                if (mViewModel.mPage == 1) {
                    mBinding.srlRelationRefresh.finishRefresh()
                    mAdapter.setNewInstance(it)
                } else {
                    mBinding.srlRelationRefresh.finishLoadMore()
                    mAdapter.addData(it)
                }
            }
        }

        mViewModel.followResult.observe(this) {
            val index = mAdapter.data.indexOf(it)
            if (index >= 0) {
                LogUtil.e("index=$index")
                mAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun initRequestData() {
        mViewModel.vquGetList(type, false)
    }

    override fun RelationVquFragmentFriendBinding.initView() {
        setLoadSir(mBinding.srlRelationRefresh)

        mBinding.rvRelationVquList.adapter = mAdapter
        mBinding.rvRelationVquList.animation = null
//        var itemAnimator = mBinding.rvRelationVquList.itemAnimator
//        if (itemAnimator is SimpleItemAnimator) {
//            itemAnimator.supportsChangeAnimations = false
//        }
//        itemAnimator?.changeDuration = 0
        var recycledViewPool = RecyclerView.RecycledViewPool()
        recycledViewPool.setMaxRecycledViews(0, 10)
        mBinding.rvRelationVquList.setRecycledViewPool(recycledViewPool)

        mAdapter.setOnItemClickListener { _, _, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            item.userid?.toInt()?.let {
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                        RouteKey.USERID,
                        it
                    )
                    .navigation()
            }
        }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.stv_relation_vqu_item_relation_list_focus -> {
                    vquFocus(item)
                }
            }
        }

        mBinding.srlRelationRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.vquGetList(type, false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.vquGetList(type, true)
            }
        })
    }

    private fun vquFocus(item: VquRelationBean) {
        mViewModel.vquFocus(item)
    }
}