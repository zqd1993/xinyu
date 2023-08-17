package com.mshy.VInterestSpeed.common.ui.loadsir;

import android.content.Context;
import android.view.View;

import com.kingja.loadsir.callback.Callback;
import com.mshy.VInterestSpeed.common.R;

/**
 * 应用模块:
 * <p>
 * 类描述: 等待加载
 * <p>
 *

 */
public class LoadingCallback extends Callback
{
    @Override
    protected int onCreateView()
    {
        return R.layout.common_base_layout_loading;
    }
    
    @Override
    public boolean getSuccessVisible()
    {
        return super.getSuccessVisible();
    }
    
    @Override
    protected boolean onReloadEvent(Context context, View view)
    {
        return true;
    }
}
