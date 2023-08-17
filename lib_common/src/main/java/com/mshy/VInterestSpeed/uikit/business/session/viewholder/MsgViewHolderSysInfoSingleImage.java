package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.live.vquonline.base.ktx.SizeUnitKtxKt;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;
import com.mshy.VInterestSpeed.common.utils.JumpUtils;
import com.mshy.VInterestSpeed.uikit.attchment.SysInfoSingleImageAttachment;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;



/**
 * Created by zhangbin on 2018/12/5.
 * 自定义当图文系统通知
 */

public class MsgViewHolderSysInfoSingleImage extends MsgViewHolderBase {

    private ImageView mSv_single_image;
    private TextView mTv_single_title;
    private TextView mTv_single_context;
    private SysInfoSingleImageAttachment mSysInfoSingleImageAttachment;
    private TextView mTv_single_category;

    public MsgViewHolderSysInfoSingleImage(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_vqu_nim_sys_info_single_image;
    }

    @Override
    protected void inflateContentView() {
        mSv_single_image = view.findViewById(R.id.sv_single_image);
        mTv_single_title = view.findViewById(R.id.tv_single_title);
        mTv_single_context = view.findViewById(R.id.tv_single_context);
        mTv_single_category = view.findViewById(R.id.tv_single_category);
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mSysInfoSingleImageAttachment = (SysInfoSingleImageAttachment) message.getAttachment();
        ViewGroup.LayoutParams imageParams = mSv_single_image.getLayoutParams();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        try {
            imageParams.width = wm.getDefaultDisplay().getWidth();
            imageParams.height = wm.getDefaultDisplay().getWidth() * 9 / 16;
            mSv_single_image.setLayoutParams(imageParams);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        switch (mSysInfoSingleImageAttachment.getType()) {
            case 12:
                ImageExtKt.vquLoadRoundImage(mSv_single_image,mSysInfoSingleImageAttachment.getImage(), SizeUnitKtxKt.dp2px(context,8f));

                if (mSysInfoSingleImageAttachment.getAct_type() == 1) {
                    mTv_single_title.setVisibility(View.GONE);
                    mTv_single_context.setVisibility(View.GONE);
                }else if (mSysInfoSingleImageAttachment.getAct_type() == 2) {
                    mTv_single_title.setVisibility(View.VISIBLE);
                    mTv_single_title.setText(mSysInfoSingleImageAttachment.getTitle());
                    if (!TextUtils.isEmpty(mSysInfoSingleImageAttachment.getTxt())) {
                        mTv_single_context.setVisibility(View.VISIBLE);
                        mTv_single_context.setText(mSysInfoSingleImageAttachment.getTxt());
                    }else {
                        mTv_single_context.setVisibility(View.GONE);
                    }
                }

                if (TextUtils.isEmpty(mSysInfoSingleImageAttachment.getAct_string())) {
                    mTv_single_category.setVisibility(View.GONE);
                }else {
                    mTv_single_category.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onItemClick() {
        JumpUtils.jump(mSysInfoSingleImageAttachment.getLink_type(), mSysInfoSingleImageAttachment.getLink_url(), context);
    }

    /**
     * 禁止长按
     *
     * @return
     */
    @Override
    protected boolean onItemLongClick() {
        return true;
    }

    /**
     * 居中显示
     *
     * @return
     */
    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    /**
     * 不显示头像
     *
     * @return
     */
    @Override
    protected boolean isShowHeadImage() {
        return false;
    }

    /**
     * 不显示气泡
     *
     * @return
     */
    @Override
    protected boolean isShowBubble() {
        return false;
    }

    /**
     * 不显示已读
     *
     * @return
     */
    @Override
    protected boolean shouldDisplayReceipt() {
        return false;
    }
}
