package com.live.module.message.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.live.module.message.R;
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean;

import java.util.List;

/**
 * FileName: com.live.vquonline.im.uikit.business.session.adapter
 * Author: Reisen
 * Date: 2022/3/8 15:25
 * Description:
 * History:
 */
public class MessageVquCommonWordAdapter extends BaseQuickAdapter<NIMCommonWordBean, BaseViewHolder> {

    public MessageVquCommonWordAdapter(@Nullable @org.jetbrains.annotations.Nullable List<NIMCommonWordBean> data) {
        super(R.layout.message_tanta_item_common_word, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NIMCommonWordBean bean) {
        holder.setText(R.id.tv_content, bean.getWord());

        holder.setGone(R.id.iv_custom_icon, bean.getIs_system() == 1);
    }
}
