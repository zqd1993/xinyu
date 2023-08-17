package com.mshy.VInterestSpeed.common.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.kingja.loadsir.core.Transport
import com.live.vquonline.base.mvvm.v.FrameView
import com.live.vquonline.base.mvvm.v.VMView
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.BindingReflex
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.EventBusUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.CommonApplication
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.livedata.AppViewModel

/**
 * Fragment 懒加载基类
 *
 * ""
 * @since 8/27/20
 */
abstract class BaseLazyFrameFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment(),
    FrameView<VB>,VMView {
    val appViewModel: AppViewModel by lazy { CommonApplication.mCommonApplication.getAppViewModelProvider().get(AppViewModel::class.java) }
    /**
     * 私有的 ViewBinding 此写法来自 Google Android 官方
     */
    private var _binding: VB? = null

    protected val mBinding get() = _binding!!

    protected abstract val mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BindingReflex.reflexViewBinding(javaClass, layoutInflater)
        return _binding?.root
    }

    val mFragmentTag = javaClass.simpleName

    /**
     * 布局是否创建完成
     */
    protected var isViewCreated = false

    /**
     * 当前可见状态
     */
    protected var currentVisibleState = false

    /**
     * 是否第一次可见
     */
    protected var mIsFirstVisible = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 初始化的时候,判断当前Fragment可见状态
        // isHidden在使用FragmentTransaction的show/hidden时会调用,可见返回的是false

        // ARouter 依赖注入
        ARouter.getInstance().inject(this)
        // 注册EventBus
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) {EventBusUtils.register(this)}

        _binding?.initView()
        isViewCreated = true
        initObserve()
        initRequestData()
        if (!isHidden && userVisibleHint) {
            // 可见状态,进行事件分发
             dispatchUserVisibleHint(true)
        }
    }

    /**
     * 修改Fragment的可见性 setUserVisibleHint 被调用有两种情况：
     * 1）在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数，可以看log 2)
     * 对于之前已经调用过setUserVisibleHint方法的fragment后，让fragment从可见到不可见之间状态的变化
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d(mFragmentTag, "setUserVisibleHint: $isVisibleToUser")
        // 对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated) {
            // 对于情况2,需要分情况考虑,如果是不可见 -> 可见 2.1
            // 如果是可见 -> 不可见 2.2
            // 对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser
            // 为true）而且只有当可见状态进行改变的时候才需要切换，否则会出现反复调用的情况
            // 从而导致事件分发带来的多次更新
            if (isVisibleToUser && !currentVisibleState) {
                // 从不可见 -> 可见
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && currentVisibleState) {
                dispatchUserVisibleHint(false)
            }
        }
    }


    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     */
    override fun onHiddenChanged(hidden: Boolean) {
        Log.d(mFragmentTag, "onHiddenChanged: $hidden")
        super.onHiddenChanged(hidden)
        // 这里的可见返回为false
        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    /**
     * 统一处理用户可见事件分发
     */
      open fun dispatchUserVisibleHint(isVisible: Boolean) {
        Log.d(mFragmentTag, "dispatchUserVisibleHint: $isVisible")

        // 首先考虑一下fragment嵌套fragment的情况(只考虑2层嵌套)
        if (isVisible && isParentInvisible()) {
            // 父Fragmnet此时不可见,直接return不做处理
            return
        }
        // 为了代码严谨,如果当前状态与需要设置的状态本来就一致了,就不处理了
        if (currentVisibleState == isVisible) {
            return
        }
        currentVisibleState = isVisible
        if (isVisible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                // 第一次可见,进行全局初始化
                onFragmentFirstVisible()
            }
            onFragmentResume()
            // 分发事件给内嵌的Fragment
            dispatchChildVisibleState(true)
        } else {
            onFragmentPause()
            dispatchChildVisibleState(false)
        }
    }

    /**
     * 在双重ViewPager嵌套的情况下，第一次滑到Frgment 嵌套ViewPager(fragment)的场景的时候
     * 此时只会加载外层Fragment的数据，而不会加载内嵌viewPager中的fragment的数据，因此，我们
     * 需要在此增加一个当外层Fragment可见的时候，分发可见事件给自己内嵌的所有Fragment显示
     */
      open fun dispatchChildVisibleState(visible: Boolean) {
        Log.d(mFragmentTag, "dispatchChildVisibleState $visible")
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (null != fragments) {
            for (fragment in fragments) {
                if (fragment is BaseLazyFrameFragment<*, *> && !fragment.isHidden
                    && fragment.userVisibleHint
                ) {
                    (fragment as BaseLazyFrameFragment<*, *>).dispatchUserVisibleHint(
                        visible
                    )
                }
            }
        }
    }

    /**
     * Fragment真正的Pause,暂停一切网络耗时操作
     */
    protected open fun onFragmentPause() {
        Log.d(mFragmentTag, "onFragmentPause " + " 真正的Pause 结束相关操作耗时")
    }

    /**
     * Fragment真正的Resume,开始处理网络加载等耗时操作
     */
    protected open fun onFragmentResume() {
        Log.d(mFragmentTag, "onFragmentResume" + " 真正的Resume 开始相关操作耗时")
    }

    open fun isParentInvisible(): Boolean {
        val parentFragment = parentFragment
        if (parentFragment is BaseLazyFrameFragment<*, *>) {
            val fragment: BaseLazyFrameFragment<*, *>? =
                parentFragment as BaseLazyFrameFragment<*, *>?
            return fragment!!.isSupportVisible()
        }
        return false
    }

    open fun isSupportVisible(): Boolean {
        return currentVisibleState
    }

    /**
     * 在滑动或者跳转的过程中，第一次创建fragment的时候均会调用onResume方法
     */
    override fun onResume() {
        super.onResume()
        // 如果不是第一次可见
        if (!mIsFirstVisible) {
            // 如果此时进行Activity跳转,会将所有的缓存的fragment进行onResume生命周期的重复
            // 只需要对可见的fragment进行加载,
            if (!isHidden && !currentVisibleState && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint currentVisibleState &&
     * getUserVisibleHint() 能够限定是当前可见的 Fragment 当前 Fragment 包含子 Fragment 的时候
     * dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见 子 fragment 走到这里的时候自身又会调用一遍
     */
    override fun onPause() {
        super.onPause()
        if (currentVisibleState && userVisibleHint) {
            dispatchUserVisibleHint(false)
        }
    }


    override fun onDetach() {
        super.onDetach()
        Log.d(mFragmentTag, "onDetach")
    }

    /**
     * 第一次可见,根据业务进行初始化操作
     */
    protected open fun onFragmentFirstVisible() {
        Log.d(mFragmentTag, "onFragmentFirstVisible  第一次可见")
    }


    override fun onDestroyView() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(
            this
        )
        super.onDestroyView()
        isViewCreated = false
        Log.d(mFragmentTag, "onDestroyView")
    }

    override fun onDestroy() {
        Log.d(mFragmentTag, "onDestroy")
        super.onDestroy()
    }

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
    fun onRetryBtnClick(){

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
            mLoadService!!.setCallBack(
                com.mshy.VInterestSpeed.common.ui.loadsir.EmptyCallback::class.java,
                Transport { context, view ->
                    val mTvEmpty: TextView = view.findViewById<View>(R.id.tv_loadsir_empty) as TextView
                    val mIvImage : ImageView = view.findViewById<View>(R.id.iv_loadsir_empty) as ImageView
                    mTvEmpty.setText(mesage)
                    mIvImage.setImageResource(defaultIcon)
                })
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