package com.mshy.VInterestSpeed.common.ui.dialog;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.mshy.VInterestSpeed.common.utils.JumpUtils;


/**
 * 通用广告弹窗
 */
public class CommonAdDialog extends BaseDialogFragment {

    private CommonVquAdBean.ListBean bean;

    @Override
    protected void initView(View view) {
        ImageView ivAd = view.findViewById(R.id.iv_ad);
        if (bean != null) {
            Glide.with(getContext()).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + bean.getImage()).into(ivAd);
        }
        ivAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean != null) {
                    JumpUtils.jump(bean.getLink_type(), bean.getLink_url(), getContext());
                }
            }
        });

        view.findViewById(R.id.iv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) ivAd.getLayoutParams();
        params.height = (int) (UIUtil.getScreenWidth(getContext()) * 0.8);
        params.width = (int) (UIUtil.getScreenWidth(getContext()) * 0.8);
        ivAd.setLayoutParams(params);
    }


    public CommonAdDialog setAdBean(CommonVquAdBean.ListBean bean) {
        this.bean = bean;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_common_ad;
    }

    @Override
    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }
}
