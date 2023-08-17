package com.mshy.VInterestSpeed.common.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.Transport
import com.live.vquonline.base.mvvm.v.BaseFrameFragment
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R

/**
 * Fragment基类
 *
 * ""
 * @since 8/27/20
 */
abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFrameFragment<VB, VM>(){

    protected var mLoadService: LoadService<*>? = null

    /**
     * 注册LoadSir
     *
     * @param view 替换视图
     */
    open fun setLoadSir(view: View?) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault()
                .register(view,
                    Callback.OnReloadListener {
                        onRetryBtnClick()
                    })
        }
    }

    /**
     * 失败重试,重新加载事件
     */
    open fun onRetryBtnClick(){

    }

    private var isShowedContent = false

    fun showContent() {
        if (mLoadService != null) {
            isShowedContent = true
            mLoadService?.showSuccess()
        }
    }

    fun showEmpty(mesage: String = getString(R.string.base_fine_no_data), defaultIcon: Int = R.mipmap.ic_tanta_base_empty) {
        if (null != mLoadService) {
            var emptyCallback= com.mshy.VInterestSpeed.common.ui.loadsir.EmptyCallback::class.java
            mLoadService!!.setCallBack(
                emptyCallback,
                Transport { context, view ->
                    val mTvEmpty: TextView = view.findViewById<View>(R.id.tv_loadsir_empty) as TextView
                    val mIvImage : ImageView = view.findViewById<View>(R.id.iv_loadsir_empty) as ImageView
                    mTvEmpty.setText(mesage)
                    mIvImage.setImageResource(defaultIcon)
                })
            mLoadService?.showCallback(emptyCallback)
        }
    }

    fun showFailure(mesage: String = getString(R.string.base_empty_tips_netword), defaultIcon: Int = R.mipmap.ic_base_network_error) {
        if (null != mLoadService) {
            if (!isShowedContent)
            {
                mLoadService!!.setCallBack(
                    com.mshy.VInterestSpeed.common.ui.loadsir.ErrorCallback::class.java,
                    Transport { context, view ->
                        val mTvTextError: TextView = view.findViewById<View>(R.id.tv_loadsir_error) as TextView
                        val mIvImageError : ImageView = view.findViewById<View>(R.id.iv_loadsir_error) as ImageView
                        mTvTextError.setText(mesage)
                        mIvImageError.setImageResource(defaultIcon)
                    })
            }
            else
            {
                toast(mesage,1000)
            }

        }
    }

    fun showLoading() {
        if (null != mLoadService) {
            mLoadService!!.showCallback(com.mshy.VInterestSpeed.common.ui.loadsir.LoadingCallback::class.java)
        }
    }
}
