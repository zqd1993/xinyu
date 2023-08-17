package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;
import com.mshy.VInterestSpeed.common.utils.UserManager;
import com.mshy.VInterestSpeed.uikit.attchment.MessageVquGiftAttachment;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;


/**
 * Created by zhangbin on 2018/12/6.
 */

public class MsgViewHolderGift extends MsgViewHolderBase {

    private TextView mTv_gift_name;
    private ImageView mSv_gift_logo;
    private TextView mTv_gift_name_left;
    private ImageView mSv_gift_logo_left;
    private LinearLayout mLl_left;
    private LinearLayout mLl_right;
    private MessageVquGiftAttachment mGiftAttachment;

    public MsgViewHolderGift(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_tanta_nim_gift_custom_message;
    }

    @Override
    protected void inflateContentView() {
        mTv_gift_name = view.findViewById(R.id.tv_gift_name);
        mSv_gift_logo = view.findViewById(R.id.sv_gift_logo);
        mTv_gift_name_left = view.findViewById(R.id.tv_gift_name_left);
        mSv_gift_logo_left = view.findViewById(R.id.sv_gift_logo_left);
        mLl_left = view.findViewById(R.id.ll_left);
        mLl_right = view.findViewById(R.id.ll_right);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mGiftAttachment = (MessageVquGiftAttachment) message.getAttachment();
        if (mGiftAttachment.getType() == 14) {
            mTv_gift_name.setText(mGiftAttachment.getGift_name() + "x" + mGiftAttachment.getGift_count());
            ImageExtKt.vquLoadImage(mSv_gift_logo, NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + mGiftAttachment.getGift_url());
            mTv_gift_name_left.setText(mGiftAttachment.getGift_name() + "x" + mGiftAttachment.getGift_count());
            ImageExtKt.vquLoadImage(mSv_gift_logo_left, NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + mGiftAttachment.getGift_url());
            if (UserManager.INSTANCE.getUserInfo() != null && String.valueOf(mGiftAttachment.getFrom_uid()).equals(UserManager.INSTANCE.getUserInfo().getUserId())) {
                mLl_left.setVisibility(View.GONE);
                mLl_right.setVisibility(View.VISIBLE);
            } else {
                mLl_left.setVisibility(View.VISIBLE);
                mLl_right.setVisibility(View.GONE);
            }
        }

        timeTextView.setVisibility(View.GONE);

    }

    @Override
    protected int leftBackground() {
//        return R.drawable.shape_news_gift_left_background;
        return 0;
    }

    @Override
    protected int rightBackground() {
//        return R.drawable.shape_news_gift_right_background;
        return 0;
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        return true;
    }

    @Override
    protected boolean isMiddleAvatarItem() {
        return true;
    }
}
