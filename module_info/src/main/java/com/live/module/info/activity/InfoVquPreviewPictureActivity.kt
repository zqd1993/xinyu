package com.live.module.info.activity

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.live.module.info.R
import com.live.module.info.adapter.PicturePaperAdapter
import com.live.module.info.databinding.InfoTantaActivityPreviewpictureBinding
import com.live.module.info.vm.InfoEditViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.DelImgEvent
import com.mshy.VInterestSpeed.common.ext.logI
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.view.photoview.PhotoView
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/4/7
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Info.InfoVquPreviewPictureActivity)
class InfoVquPreviewPictureActivity :
    BaseActivity<InfoTantaActivityPreviewpictureBinding, InfoEditViewModel>() {

    private val views: ArrayList<View> = ArrayList()
    private var urls: ArrayList<String>? = null
    private var position = 0
    private var showDel: Boolean = false
    override val mViewModel: InfoEditViewModel by viewModels()
    var adapter: PicturePaperAdapter? = null
    override fun InfoTantaActivityPreviewpictureBinding.initView() {
        urls = intent.getStringArrayListExtra("urls")
        if (urls == null) {
            return
        }
        urls?.logI()
        position = intent.getIntExtra("position", 0)
        showDel = intent.getBooleanExtra("del", false)
        mBinding.tvPosition.text = (position + 1).toString() + "/" + urls?.size.toString()
        initImgs()
        mBinding.ivBack.setOnClickListener { finish() }
        mBinding.tvDel.setOnClickListener {
            CommonHintDialog()
                .setTitle(getString(R.string.setting_tip))
                .setContent("是否删除这张照片?")
                .setLeftText("取消")
                .setRightText("删除")
                .setContentSize(15)
                .setContentGravity(Gravity.CENTER)
                .setOnClickListener(object : CommonHintDialog.OnClickListener {
                    override fun onLeft(dialogFragment: DialogFragment) {}
                    override fun onRight(dialogFragment: DialogFragment) {
                        EventBus.getDefault().post(DelImgEvent(
                            mBinding.viewpager.currentItem))
                        views?.removeAt(mBinding.viewpager.currentItem)
                        urls?.removeAt(mBinding.viewpager.currentItem)
                        adapter?.notifyDataSetChanged()
                        mBinding.tvPosition.text =
                            (mBinding.viewpager.currentItem + 1).toString() + "/" + urls?.size.toString()
                        if (urls.isNullOrEmpty()) {
                            finish()
                        }
                    }
                })
                .show(supportFragmentManager)

        }
    }

    private fun initImgs() {
        mBinding.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                this@InfoVquPreviewPictureActivity.position = position
                mBinding.tvPosition.setText((position + 1).toString() + "/" + urls?.size.toString())
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        views.clear()
        var view: View? = null
        for (i in urls!!.indices) {
            view = buildImageView(urls!![i])
            views.add(view!!)
        }
        adapter = PicturePaperAdapter(views)
        mBinding.viewpager.setAdapter(adapter)
        mBinding.viewpager.setCurrentItem(position)
        if (showDel) {
            mBinding.tvDel.visible()
        } else {
            mBinding.tvDel.gone()
        }

    }

    private fun buildImageView(url: String): View? {
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.info_tanta_layout_previewpicture, null)
        val photoView: PhotoView = view.findViewById(R.id.photoview)
        photoView.scale
        photoView.setOnClickListener(View.OnClickListener { finish() })
        photoView.vquLoadImage(url)

//        Glide.with(this).asBitmap().load(url).into(object : SimpleTarget<Bitmap?>() {
//            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
//                photoView.setImageBitmap(resource)
//                runOnUiThread { photoView.setScale(1f) }
//            }
//        })
        return view
    }

    override fun setStatusBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.color_1C1D23)
            .statusBarDarkFont(false)
            .navigationBarColor(R.color.color_1C1D23).init()
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }
}