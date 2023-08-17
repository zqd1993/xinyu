package com.mshy.VInterestSpeed.uikit.business.session.viewholder;


import androidx.annotation.DimenRes;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;


/**
 * Created by hzchenkang on 2017/6/26.
 */

public class MsgViewHolderFateMatch extends MsgViewHolderBase {


    public MsgViewHolderFateMatch(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_tanta_nim_fate_match_custom_message;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {
    }


    private float getDimen(@DimenRes int dimen) {
        return context.getResources().getDimension(dimen);
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        return false;
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    @Override
    protected boolean shouldDisplayStatus() {
        return false;
    }

    @Override
    protected void onItemClick() {

    }
}
