package com.live.module.message.adapter;


import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.live.module.message.R;
import com.live.module.message.bean.GuideBean;
import com.live.vquonline.base.ktx.SizeUnitKtxKt;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * FileName: com.live.vquonline.view.news.adapter
 * Author: Reisen
 * Date: 2022/3/23 18:23
 * Description:
 * History:
 */
public class MessageVquGuideAdapter extends BaseQuickAdapter<GuideBean, BaseViewHolder> {

    public MessageVquGuideAdapter(@Nullable @org.jetbrains.annotations.Nullable List<GuideBean> data) {
        super(R.layout.message_tanta_item_guide, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder holder, GuideBean data) {
        holder.setGone(R.id.tv_content_right, data.isReceive())
                .setGone(R.id.sdv_right, data.isReceive())
                .setGone(R.id.tv_content_left, !data.isReceive())
                .setGone(R.id.sdv_left, !data.isReceive())
                .setGone(R.id.coin_text, !data.isReceive())
                .setText(R.id.coin_text, data.getCoinHint())
                .setText(R.id.tv_content_left, data.getContent())
                .setText(R.id.tv_content_right, data.getContent());
        ImageView sdvHead = holder.getView(R.id.sdv_right);
        ImageView sdvLeft = holder.getView(R.id.sdv_left);
        ImageExtKt.vquLoadRoundImage(sdvLeft,
                R.mipmap.resources_vqu_message_ic_guide_service, SizeUnitKtxKt.dp2px(getContext(), 10f));
        if (!data.isReceive()) {
            ImageExtKt.vquLoadRoundImage(sdvHead,
                    NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + data.getHeadUrl(), SizeUnitKtxKt.dp2px(getContext(), 10f));
        }
    }


}
