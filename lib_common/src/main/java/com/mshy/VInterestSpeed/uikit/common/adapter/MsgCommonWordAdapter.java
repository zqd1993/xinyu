package com.mshy.VInterestSpeed.uikit.common.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean;

import java.util.List;

/**
 * FileName: com.live.vquonline.im.uikit.business.session.adapter
 * Author: Reisen
 * Date: 2022/3/8 15:25
 * Description: 聊天界面的常用语适配器
 * History:
 */
public class MsgCommonWordAdapter extends BaseQuickAdapter<NIMCommonWordBean, com.chad.library.adapter.base.viewholder.BaseViewHolder> {

    public MsgCommonWordAdapter(@Nullable @org.jetbrains.annotations.Nullable List<NIMCommonWordBean> data) {
        super(R.layout.common_tanta_nim_item_msg_common_word,data);
    }


    @Override
    protected void convert(@NonNull  com.chad.library.adapter.base.viewholder.BaseViewHolder holder, NIMCommonWordBean bean) {
        holder.setText(R.id.tv_content,bean.getWord());
    }
}
