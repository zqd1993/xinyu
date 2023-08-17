package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.live.vquonline.base.ktx.SizeUnitKtxKt;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;
import com.mshy.VInterestSpeed.common.utils.JumpUtils;
import com.mshy.VInterestSpeed.uikit.attchment.SysInfoDoubleImageAttachment;
import com.mshy.VInterestSpeed.uikit.bean.NIMVquSysInfoImage;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by zhangbin on 2018/12/5.
 * 自定义多图文系统通知
 */

public class MsgViewHolderSysInfoDoubleImage extends MsgViewHolderBase {

    private ImageView mSv_double_image;
    private LinearLayout mLl_double_inflate;
    private SysInfoDoubleImageAttachment mSysInfoDoubleImageAttachment;
    private TextView mTv_double_title;
    private View mImageView;

    public MsgViewHolderSysInfoDoubleImage(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_vqu_nim_sys_info_double_image;
    }

    @Override
    protected void inflateContentView() {
        mSv_double_image = view.findViewById(R.id.sv_double_image);
        mTv_double_title = view.findViewById(R.id.tv_double_title);
        mLl_double_inflate = view.findViewById(R.id.ll_double_inflate);
    }

    /**
     * 防止滑到底部刷新重复添加动态布局
     *
     * @param holder
     * @param data
     * @param position
     * @param isScrolling
     */
    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
//        super.convert(holder, data, position, isScrolling);

        if (view == null) {
            view = holder.getConvertView();
            context = holder.getContext();
            message = data;

            inflate();
            refresh();
            bindHolder(holder);
        }
    }

    @Override
    protected void bindContentView() {
        if (message.getAttachment() == null) {
            return;
        }
        mSysInfoDoubleImageAttachment = (SysInfoDoubleImageAttachment) message.getAttachment();

        switch (mSysInfoDoubleImageAttachment.getType()) {
            case 13:
                final List<NIMVquSysInfoImage> imageList = mSysInfoDoubleImageAttachment.getImageList();
                if (null != imageList) {
                    ImageExtKt.vquLoadRoundImage(mSv_double_image, imageList.get(0).getImage(), SizeUnitKtxKt.dp2px(context, 8f));
                    mTv_double_title.setText(imageList.get(0).getTitle());

                    mSv_double_image.setOnClickListener(view -> JumpUtils.jump(imageList.get(0).getLink_type(), imageList.get(0).getLink_url(), context));

                    if (imageList.size() > 0) {
                        for (int i = 1; i < imageList.size(); i++) {
                            final NIMVquSysInfoImage sysInfoImage = imageList.get(i);
                            mImageView = View.inflate(context, R.layout.common_vqu_nim_item_sys_info_double_iamge, null);
                            ImageView childPhoto = mImageView.findViewById(R.id.sv_item_double_image);
                            TextView childTitle = mImageView.findViewById(R.id.tv_item_double_title);
                            ImageExtKt.vquLoadRoundImage(childPhoto, imageList.get(0).getImage(), SizeUnitKtxKt.dp2px(context, 5f));
                            childTitle.setText(sysInfoImage.getTitle());
                            mLl_double_inflate.addView(mImageView);

                            mImageView.setOnClickListener(view -> JumpUtils.jump(sysInfoImage.getLink_type(), sysInfoImage.getLink_url(), context));

                        }
                    }
                }
                break;
        }
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
