package com.mshy.VInterestSpeed.common.ui

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.live.vquonline.base.mvvm.v.VMView
import com.live.vquonline.base.mvvm.vm.BaseViewModel

abstract class BaseVMDialogFragment<VB : ViewBinding, VM : BaseViewModel> :
    BaseDialogFragment<VB>(),VMView {

    protected abstract val mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
        initRequestData()
    }

}

