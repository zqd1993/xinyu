package com.live.module.info.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.live.module.info.bean.Album
import com.live.module.info.databinding.InfoTantaFragmentPreviewImgBinding
import com.live.module.info.vm.InfoVquViewModel
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.BaseFragment

/**
 * author: Tany
 * date: 2022/5/7
 * description:
 */
class InfoVquPreviewImgFragment :
    BaseFragment<InfoTantaFragmentPreviewImgBinding, InfoVquViewModel>() {
    override val mViewModel: InfoVquViewModel by viewModels()

    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun InfoTantaFragmentPreviewImgBinding.initView() {
        var album = arguments?.getSerializable("album") as Album
        ivImg.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + album?.url)
    }

    companion object {
        fun newInstance(album: Album): InfoVquPreviewImgFragment {
            val args = Bundle()
            args.putSerializable("album", album)
            val fragment = InfoVquPreviewImgFragment()
            fragment.arguments = args
            return fragment
        }
    }
}