package com.live.module.info.activity

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.gyf.immersionbar.ImmersionBar
import com.live.module.info.bean.Album
import com.live.module.info.databinding.InfoTantaActivityPreviewPicVideoBinding
import com.live.module.info.fragment.InfoVquPreviewImgFragment
import com.live.module.info.vm.InfoVquViewModel
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.event.VideoPlayEvent
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.InfoVquPreviewVideoFragment
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/5/5
 * description:
 */
class InfoVquPreviewPicAndVideoActivity :
    BaseActivity<InfoTantaActivityPreviewPicVideoBinding, InfoVquViewModel>() {
    private val mFragments: ArrayList<Fragment> = ArrayList()
    private var mViewPagerAdapter: CommonVquMainPageAdapter? = null
    override val mViewModel: InfoVquViewModel by viewModels()
    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun InfoTantaActivityPreviewPicVideoBinding.initView() {
        ImmersionBar.with(this@InfoVquPreviewPicAndVideoActivity).transparentStatusBar()
            .fitsSystemWindows(false).init()
        var obj = intent.getSerializableExtra("list")
        var pos = intent.getIntExtra("position", 0)
        if (obj != null) {
            var imgList = obj as ArrayList<Album>
            imgList.map {
                if (it.isVideo == 1) {
                    mFragments.add(InfoVquPreviewVideoFragment.newInstance(NetBaseUrlConstant.IMAGE_URL + it?.url ,null))
                } else {
                    mFragments.add(InfoVquPreviewImgFragment.newInstance(it))
                }
            }
            mViewPagerAdapter =
                CommonVquMainPageAdapter(
                    supportFragmentManager,
                    FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
                )
            mViewPagerAdapter?.setData(mFragments)
            vp.adapter = mViewPagerAdapter
            vp.currentItem = pos
            vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                    tvPosition.text = (position + 1).toString() + "/" + imgList.size
                    if (position == 0) {
                        if (imgList[0].isVideo == 1) {
                            EventBus.getDefault().post(VideoPlayEvent(
                                true))
                        } else {
                            EventBus.getDefault().post(VideoPlayEvent(
                                false))
                        }
                    } else {
                        EventBus.getDefault().post(VideoPlayEvent(
                            false))
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
            vp.offscreenPageLimit = 10
            ivBack.setViewClickListener { finish() }
            tvPosition.text = (pos + 1).toString() + "/" + imgList.size
        }
    }


}