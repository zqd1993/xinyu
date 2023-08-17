package com.live.module.relation.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.relation.R
import com.live.module.relation.adapter.RelationVquPraiseAdapter
import com.live.module.relation.bean.VquRelationPraiseBean
import com.live.module.relation.databinding.RelationActivityRelationBinding
import com.live.module.relation.vm.RelationVquPraiseListViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/13
 * description:获赞列表
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Relation.RelationVquPraiseListActivity)
class RelationVquPraiseListActivity :
    BaseActivity<RelationActivityRelationBinding, RelationVquPraiseListViewModel>() {
    override val mViewModel: RelationVquPraiseListViewModel by viewModels()

    private val mAdapter = RelationVquPraiseAdapter()

    override fun RelationActivityRelationBinding.initView() {

        setLoadSir(mBinding.srlRelationRefresh)

        mBinding.tbRelationBar.toolbar.initClose(getString(R.string.mine_vqu_praise)) {
            finish()
        }

        mBinding.rvRelationVquList.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    item.fromUserId.toInt()
                )
                .navigation()

        }


        mAdapter.setOnItemChildClickListener { _, view, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.cl_relation_vqu_item_praise_list_dynamic_parent -> {
                    clickDynamicParent(item)
                }
            }
        }

        initEvent()
    }

    private fun initEvent() {
        mBinding.srlRelationRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                //刷新数据
                mViewModel.vquLikeList(false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                //加载更多数据
                mViewModel.vquLikeList(true)
            }
        })
    }

    private fun clickDynamicParent(item: VquRelationPraiseBean) {
        if (item.type == 0 || item.type == 2) {

            if (!item.fileUrl.isNullOrEmpty()) {
                //跳转视频播放
                ARouter.getInstance()
                    .build(RouteUrl.Common.CommonVquVideoActivity)
                    .withString("video_url", NetBaseUrlConstant.IMAGE_URL + item.fileUrl)
                    .navigation()
            } else {
                toast(R.string.relation_vqu_content_deleted)
            }
        } else if (item.type == 1) {
            if (!item.coverUrl.isNullOrEmpty()) {
                //跳转相册
                val imageUrls = ArrayList<String>()
                imageUrls.add(NetBaseUrlConstant.IMAGE_URL + item.coverUrl)
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPreviewPictureActivity)
                    .withInt("position", 0)
                    .withStringArrayList("urls", imageUrls)
                    .navigation()
            } else {
                toast(R.string.relation_vqu_content_deleted)
            }
        }
    }

    override fun initObserve() {

        //观察获赞列表LiveData
        mViewModel.praiseList.observe(this) {

            if (mViewModel.mPage == 1 && it.isNullOrEmpty()) {
                showEmpty()
            } else {
                mLoadService?.showSuccess()
                /*
                    判断页码是否第一页，如果是，则更新数据
                */
                if (mViewModel.mPage == 1) {
                    mBinding.srlRelationRefresh.finishRefresh()
                    mAdapter.setNewInstance(it)
                } else {
                    mBinding.srlRelationRefresh.finishLoadMore()
                    mAdapter.addData(it)
                }
            }
        }

        //观察是否加载更多的LiveData
        mViewModel.loadMoreAble.observe(this) {
            mBinding.srlRelationRefresh.setEnableLoadMore(it)
        }
    }

    override fun initRequestData() {

        //获取获赞列表
        mViewModel.vquLikeList(false)
    }
}