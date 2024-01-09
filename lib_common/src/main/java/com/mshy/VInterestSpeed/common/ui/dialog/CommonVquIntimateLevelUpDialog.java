package com.mshy.VInterestSpeed.common.ui.dialog;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView;
import com.mshy.VInterestSpeed.uikit.bean.NIMVquIntimateLeveUpBean;


/**
 * 亲密升级提示弹窗
 */
public class CommonVquIntimateLevelUpDialog extends BaseDialogFragment implements View.OnClickListener {

    private ShapeTextView mTvConfirm;
    private TextView mTvContent;
    private TextView mTvDes;
    private ImageView mIvClose;
    private ImageView ivLevel;
    private ImageView ivLeft;
    private ImageView ivRight;
    private NIMVquIntimateLeveUpBean.DataBean mData;
    String IMAGE_URL = "https://asset.whzhenban.top/";

    @Override
    protected void initView(View view) {
        mTvConfirm = view.findViewById(R.id.tv_confirm);
        mTvContent = view.findViewById(R.id.tv_content);
        mTvDes = view.findViewById(R.id.tv_des);
        ivLevel = view.findViewById(R.id.iv_level);
        ivLeft = view.findViewById(R.id.iv_left);
        ivRight = view.findViewById(R.id.iv_right);
        mIvClose = view.findViewById(R.id.iv_close);
        this.setCancelable(true);
        mIvClose.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        notifyView();
    }

    private void notifyView() {
        if (mData != null) {
//            VquUserInfo userInfo = UserSpUtils.INSTANCE.getUserBean();
//            if (userInfo != null) {
//                String content;
//                if (Integer.parseInt(userInfo.getUserId()) == mData.getFromUid()) {
//                    content = String.format(getResources().getString(R.string.str_intimate_level_up_start), mData.getToNickname(), mData.getCurrentSign());
//                } else {
//                    content = String.format(getResources().getString(R.string.str_intimate_level_up_start), mData.getFromNickname(), mData.getCurrentSign());
//                }
//                SpannableString spannableString = new SpannableString(content + mData.getDesNextScore() + "℃" +
//                        String.format(getResources().getString(R.string.str_intimate_level_up_end), mData.getNextSign()));
//                ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.color_FE66A4));
//                spannableString.setSpan(colorSpan, content.length(), content.length() + (mData.getDesNextScore() + "℃").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTvContent.setText(mData.getDes());
                mTvDes.setText(mData.getMsg());
                Glide.with(BaseApplication.context)
                        .load(IMAGE_URL + mData.getAvatar())
                        .placeholder(R.mipmap.ic_common_head_def)
                        .circleCrop()
                        .into(ivRight);
                Glide.with(BaseApplication.context)
                        .load(IMAGE_URL + mData.getToAvatar())
                        .placeholder(R.mipmap.ic_common_head_def)
                        .circleCrop()
                        .into(ivLeft);
                Glide.with(BaseApplication.context)
                        .load(IMAGE_URL + mData.getGradeImg())
                        .into(ivLevel);

            }

//        }
    }

    public CommonVquIntimateLevelUpDialog setData(NIMVquIntimateLeveUpBean.DataBean data) {
        this.mData = data;
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_vqu_dialog_intimate;
    }

    @Override
    protected int getWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected int getHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public void onClick(View v) {
        if (v == mIvClose) {
            dismissAllowingStateLoss();
        } else if (v == mTvConfirm) {
//            ARouterHelperKt.startARouterActivity(RouteUrl.Main.CommonVquMainAcitvity,
//                    RouteKey.IS_INTIMATE_UP, true);
            dismissAllowingStateLoss();
        }
    }
}
