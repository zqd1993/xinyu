package com.mshy.VInterestSpeed.common.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import com.live.vquonline.base.mvvm.v.FrameView
import com.live.vquonline.base.utils.BindingReflex
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.EventBusUtils
import com.mshy.VInterestSpeed.common.ext.setViewClickListener

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment(), FrameView<VB> {

    private var _binding: VB? = null

    protected val mBinding get() = _binding!!
    protected var mCancelAble: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = BindingReflex.reflexViewBinding(javaClass, layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setStyle(STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen)

        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.register(this)

        initWindow()

        mBinding.initView()

        view.setViewClickListener {
            if (mCancelAble) {
                dismiss()
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (manager.isDestroyed) return
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            //同一实例使用不同的tag会异常，这里捕获一下
            e.printStackTrace()
        }
    }

    //    override fun show(manager: FragmentManager, tag: String?) {
//        val ft: FragmentTransaction = manager.beginTransaction()
//        ft.add(this, tag)
//        ft.commitAllowingStateLoss()
//    }
    protected open fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(EventBusRegister::class.java)) EventBusUtils.unRegister(
            this
        )
        super.onDestroy()
    }

}

