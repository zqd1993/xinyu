package com.mshy.VInterestSpeed.uikit.business.session.viewholder;

import android.app.Activity;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DimenRes;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.live.vquonline.base.ktx.SizeUnitKtxKt;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.VquUserInfo;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.constant.RouteKey;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewBuilder;
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.UserViewInfo;
import com.mshy.VInterestSpeed.common.utils.UserManager;
import com.mshy.VInterestSpeed.uikit.attchment.MessageVquMsgInfoAttachment;
import com.mshy.VInterestSpeed.uikit.bean.ChatInfoBean;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzchenkang on 2017/6/26.
 */

public class MsgViewHolderCardInfo extends MsgViewHolderBase {

    private LinearLayout mLlAlbum;
    private LinearLayout mlLlParent;
    private TextView mTvCity;
    private LinearLayout llCity;
    private TextView mTvInfo;
    private ImageView mIvRealAuth;
    private ImageView mIvNameAuth;
    private int uid;
    private final List<UserViewInfo> mListPhoto = new ArrayList<>();

    public MsgViewHolderCardInfo(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.common_tanta_nim_info_custom_message;
    }

    @Override
    protected void inflateContentView() {
        mLlAlbum = findViewById(R.id.ll_album);
        mlLlParent = findViewById(R.id.ll_parent);
        mTvCity = findViewById(R.id.tv_city);
        mTvInfo = findViewById(R.id.tv_info);
        mIvRealAuth = findViewById(R.id.iv_real_auth);
        mIvNameAuth = findViewById(R.id.iv_name_auth);
        llCity = findViewById(R.id.ll_city);
    }

    @Override
    protected void bindContentView() {
        if (message != null) {
            if (message.getAttachment() instanceof MessageVquMsgInfoAttachment) {
                Gson gson = new Gson();
                ChatInfoBean data = gson
                        .fromJson(((MessageVquMsgInfoAttachment) message.getAttachment()).getJsonData().toJSONString(), ChatInfoBean.class);
                uid = data.getUser_id();
                mIvNameAuth.setImageResource(data.getAuth().getAuth_status() == 0
                        ? R.mipmap.resources_vqu_nim_ic_msg_info_un_name_auth : R.mipmap.resources_tanta_nim_ic_msg_info_name_auth);
                mIvRealAuth.setImageResource(data.getAuth().getReal_auth_status() == 0
                        ? R.mipmap.resources_vqu_nim_ic_msg_info_un_real_auth : R.mipmap.resources_tanta_nim_ic_msg_info_real_auth);
                mTvInfo.setText(data.getBase_info());
                VquUserInfo userinfo = UserManager.INSTANCE.getUserInfo();

                if (userinfo == null) {
                    llCity.setVisibility(View.GONE);
                } else {
//                    if (userinfo.getGender() == 1) {
                        if (TextUtils.isEmpty(data.getLogin_ip_addr())) {
                            llCity.setVisibility(View.GONE);
                        } else {
                            llCity.setVisibility(View.VISIBLE);
                            mTvCity.setText(data.getLogin_ip_addr());
                        }
//                    } else {
//                        llCity.setVisibility(View.GONE);
//                    }
                }


                //设置内容宽高
                setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        , contentContainer);
                setLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        , view.findViewById(R.id.message_item_body));
                if (data.getAlbumList() != null && !data.getAlbumList().isEmpty()) {
                    mlLlParent.setVisibility(View.VISIBLE);
                    mLlAlbum.removeAllViews();
                    mListPhoto.clear();
                    for (int i = 0; i < data.getAlbumList().size(); i++) {
                        addPhotoView(data.getAlbumList().get(i), i,
                                mLlAlbum, SizeUnitKtxKt.dp2px(context, 49f),
                                SizeUnitKtxKt.dp2px(context, 49f));
                    }
                } else {
                    mlLlParent.setVisibility(View.GONE);
                }
            }
        }
    }

    private void addPhotoView(ChatInfoBean.AlbumListBean listBean,
                              final int pos, ViewGroup viewGroup, int width, int height) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = SizeUnitKtxKt.dp2px(context, 4f);
        ImageExtKt.vquLoadRoundImage(imageView,
                NetBaseUrlConstant.INSTANCE.getIMAGE_URL()
                        + listBean.getUrl(), SizeUnitKtxKt.dp2px(context, 10f));
        imageView.setOnClickListener(v -> GPreviewBuilder.from((Activity) context)
                .setData(mListPhoto)
                .setCurrentIndex(pos)
                .setType(GPreviewBuilder.IndicatorType.Number)
                .start());
        imageView.setLayoutParams(params);
        //添加图片
        Rect bounds = new Rect();
        UserViewInfo info = new UserViewInfo(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + listBean.getUrl());
        info.setBounds(bounds);
        mListPhoto.add(info);
        imageView.getGlobalVisibleRect(bounds);
        viewGroup.addView(imageView);
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
        if (uid != 0) {
            ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                            RouteKey.USERID,
                            uid
                    )
                    .withBoolean(RouteKey.FROM_CHAT, true)
                    .navigation();
        }

    }
}
